package net.haesleinhuepf.clij.utilities;

import net.haesleinhuepf.clij.clearcl.*;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.exceptions.OpenCLException;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.CLIJ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This executor can call OpenCL files. It
 * uses some functionality adapted from FastFuse, to make .cl file handling
 * easier. For example, it ensures that the right
 * image_read/image_write methods are called depending on the image
 * type.
 * <p>
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class CLKernelExecutor {
    public static int MAX_ARRAY_SIZE = 1000;
    ClearCLContext context;
    Class anchorClass;
    String programFilename;
    String kernelName;
    Map<String, Object> parameterMap;
    long[] globalSizes;

    private HashMap<String, ClearCLProgram> programCacheMap = new HashMap();
    ClearCLProgram currentProgram = null;

    public CLKernelExecutor(ClearCLContext context,
                            Class anchorClass,
                            String programFilename,
                            String kernelName,
                            long[] globalSizes) throws
            IOException {
        super();
        this.programFilename = programFilename;
        this.anchorClass = anchorClass;
        this.kernelName = kernelName;
        this.context = context;
        this.globalSizes = globalSizes;
    }

    public static void getOpenCLDefines(Map<String, Object> defines, ImageChannelDataType imageChannelDataType, boolean isInputImage) {
        if (isInputImage) {
            defines.put("DTYPE_IMAGE_IN_3D", "__read_only image3d_t");
            defines.put("DTYPE_IMAGE_IN_2D", "__read_only image2d_t");
            if (imageChannelDataType.isInteger()) {
                if (imageChannelDataType == ImageChannelDataType.UnsignedInt8 || imageChannelDataType == ImageChannelDataType.SignedInt8) {
                    defines.put("DTYPE_IN", "char");
                } else {
                    defines.put("DTYPE_IN", "ushort");
                }
            } else {
                defines.put("DTYPE_IN", "float");
            }
            defines.put("READ_IMAGE_2D", imageChannelDataType.isInteger() ? "read_imageui" : "read_imagef");
            defines.put("READ_IMAGE_3D", imageChannelDataType.isInteger() ? "read_imageui" : "read_imagef");
        } else {
            defines.put("DTYPE_IMAGE_OUT_3D", "__write_only image3d_t");
            defines.put("DTYPE_IMAGE_OUT_2D", "__write_only image2d_t");
            if (imageChannelDataType.isInteger()) {
                if (imageChannelDataType == ImageChannelDataType.UnsignedInt8 || imageChannelDataType == ImageChannelDataType.SignedInt8) {
                    defines.put("DTYPE_OUT", "char");
                } else {
                    defines.put("DTYPE_OUT", "ushort");
                }
            } else {
                defines.put("DTYPE_OUT", "float");
            }
            defines.put("WRITE_IMAGE_2D", imageChannelDataType.isInteger() ? "write_imageui" : "write_imagef");
            defines.put("WRITE_IMAGE_3D", imageChannelDataType.isInteger() ? "write_imageui" : "write_imagef");
        }
    }

    public static void getOpenCLDefines(Map<String, Object> defines, NativeTypeEnum nativeTypeEnum, boolean isInputImage) {
        String typeName = nativeTypeToOpenCLTypeName(nativeTypeEnum);
        String typeId = nativeTypeToOpenCLTypeShortName(nativeTypeEnum);

        if (isInputImage) {
            defines.put("DTYPE_IN", typeName);
            defines.put("DTYPE_IMAGE_IN_3D", "__global " + typeName + "*");
            defines.put("DTYPE_IMAGE_IN_2D", "__global " + typeName + "*");
            defines.put("READ_IMAGE_2D(a,b,c)", "read_buffer2d" + typeId + "(GET_IMAGE_WIDTH(a),GET_IMAGE_HEIGHT(a),GET_IMAGE_DEPTH(a),a,b,c)");
            defines.put("READ_IMAGE_3D(a,b,c)", "read_buffer3d" + typeId + "(GET_IMAGE_WIDTH(a),GET_IMAGE_HEIGHT(a),GET_IMAGE_DEPTH(a),a,b,c)");
        } else {
            defines.put("DTYPE_OUT", typeName);
            defines.put("DTYPE_IMAGE_OUT_3D", "__global " + typeName + "*");
            defines.put("DTYPE_IMAGE_OUT_2D", "__global " + typeName + "*");
            defines.put("WRITE_IMAGE_2D(a,b,c)", "write_buffer2d" + typeId + "(GET_IMAGE_WIDTH(a),GET_IMAGE_HEIGHT(a),GET_IMAGE_DEPTH(a),a,b,c)");
            defines.put("WRITE_IMAGE_3D(a,b,c)", "write_buffer3d" + typeId + "(GET_IMAGE_WIDTH(a),GET_IMAGE_HEIGHT(a),GET_IMAGE_DEPTH(a),a,b,c)");
        }
    }

    private static String nativeTypeToOpenCLTypeName(NativeTypeEnum pDType) {
        if (pDType == NativeTypeEnum.Byte) {
            return "char";
        } else if (pDType == NativeTypeEnum.UnsignedByte) {
            return "uchar";
        } else if (pDType == NativeTypeEnum.Short) {
            return "short";
        } else if (pDType == NativeTypeEnum.UnsignedShort) {
            return "ushort";
        } else if (pDType == NativeTypeEnum.Float) {
            return "float";
        } else {
            return "";
        }
    }

    private static String nativeTypeToOpenCLTypeShortName(NativeTypeEnum pDType) {
        if (pDType == NativeTypeEnum.Byte) {
            return "c";
        } else if (pDType == NativeTypeEnum.UnsignedByte) {
            return "uc";
        } else if (pDType == NativeTypeEnum.Short) {
            return "i";
        } else if (pDType == NativeTypeEnum.UnsignedShort) {
            return "ui";
        } else if (pDType == NativeTypeEnum.Float) {
            return "f";
        } else {
            return "";
        }
    }

    /**
     * Map of all parameters. It is recommended that input and output
     * images are given with the names "src" and "dst", respectively.
     *
     * @param parameterMap
     */
    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public boolean enqueue(boolean waitToFinish) {


        if (CLIJ.debug) {
            System.out.println("Loading " + kernelName);
        }

        ClearCLImage srcImage = null;
        ClearCLImage dstImage = null;
        ClearCLBuffer srcBuffer = null;
        ClearCLBuffer dstBuffer = null;

        if (parameterMap != null) {
            for (String key : parameterMap.keySet()) {
                if (parameterMap.get(key) instanceof ClearCLImage) {
                    if (key.contains("src") || key.contains("input")) {
                        srcImage = (ClearCLImage) parameterMap.get(key);
                    } else if (key.contains("dst") || key.contains("output")) {
                        dstImage = (ClearCLImage) parameterMap.get(key);
                    }
                } else if (parameterMap.get(key) instanceof ClearCLBuffer) {
                    if (key.contains("src") || key.contains("input")) {
                        srcBuffer = (ClearCLBuffer) parameterMap.get(key);
                    } else if (key.contains("dst") || key.contains("output")) {
                        dstBuffer = (ClearCLBuffer) parameterMap.get(key);
                    }
                }
            }
        }

        if (dstImage == null && dstBuffer == null) {
            if (srcImage != null) {
                dstImage = srcImage;
            } else if (srcBuffer != null) {
                dstBuffer = srcBuffer;
            }
        } else if (srcImage == null && srcBuffer == null) {
            if (dstImage != null) {
                srcImage = dstImage;
            } else if (dstBuffer != null) {
                srcBuffer = dstBuffer;
            }
        }


        Map<String, Object> openCLDefines = new HashMap();
        openCLDefines.put("MAX_ARRAY_SIZE", MAX_ARRAY_SIZE); // needed for median. Median is limited to a given array length to be sorted
        if (srcImage != null) {
            getOpenCLDefines(openCLDefines, srcImage.getChannelDataType(), true);
        }
        if (dstImage != null) {
            getOpenCLDefines(openCLDefines, dstImage.getChannelDataType(), false);
        }
        if (srcBuffer != null) {
            getOpenCLDefines(openCLDefines, srcBuffer.getNativeType(), true);
        }
        if (dstBuffer != null) {
            getOpenCLDefines(openCLDefines, dstBuffer.getNativeType(), false);
        }

        // deal with image width/height/depth for all images and buffers
        ArrayList<String> definedParameterKeys = new ArrayList<String>();
        for (String key : parameterMap.keySet()) {
            if (parameterMap.get(key) instanceof ClearCLImage) {
                ClearCLImage image = (ClearCLImage) parameterMap.get(key);
                openCLDefines.put("IMAGE_SIZE_" + key + "_WIDTH", image.getWidth());
                openCLDefines.put("IMAGE_SIZE_" + key + "_HEIGHT", image.getHeight());
                openCLDefines.put("IMAGE_SIZE_" + key + "_DEPTH", image.getDepth());
            } else if (parameterMap.get(key) instanceof ClearCLBuffer) {
                ClearCLBuffer image = (ClearCLBuffer) parameterMap.get(key);
                openCLDefines.put("IMAGE_SIZE_" + key + "_WIDTH", image.getWidth());
                openCLDefines.put("IMAGE_SIZE_" + key + "_HEIGHT", image.getHeight());
                openCLDefines.put("IMAGE_SIZE_" + key + "_DEPTH", image.getDepth());
            }
            definedParameterKeys.add(key);
        }

        openCLDefines.put("GET_IMAGE_IN_WIDTH(image_key)", "IMAGE_SIZE_ ## image_key ## _WIDTH");
        openCLDefines.put("GET_IMAGE_IN_HEIGHT(image_key)", "IMAGE_SIZE_ ## image_key ## _HEIGHT");
        openCLDefines.put("GET_IMAGE_IN_DEPTH(image_key)", "IMAGE_SIZE_ ## image_key ## _DEPTH");
        openCLDefines.put("GET_IMAGE_OUT_WIDTH(image_key)", "IMAGE_SIZE_ ## image_key ## _WIDTH");
        openCLDefines.put("GET_IMAGE_OUT_HEIGHT(image_key)", "IMAGE_SIZE_ ## image_key ## _HEIGHT");
        openCLDefines.put("GET_IMAGE_OUT_DEPTH(image_key)", "IMAGE_SIZE_ ## image_key ## _DEPTH");
        openCLDefines.put("GET_IMAGE_WIDTH(image_key)", "IMAGE_SIZE_ ## image_key ## _WIDTH");
        openCLDefines.put("GET_IMAGE_HEIGHT(image_key)", "IMAGE_SIZE_ ## image_key ## _HEIGHT");
        openCLDefines.put("GET_IMAGE_DEPTH(image_key)", "IMAGE_SIZE_ ## image_key ## _DEPTH");

        // add undefined parameters to define list
        ArrayList<String> variableNames = getImageVariablesFromSource();
        for (String variableName : variableNames) {

            boolean existsAlready = false;
            for (String key : definedParameterKeys) {
                if(key.compareTo(variableName) == 0) {
                    existsAlready = true;
                    break;
                }
            }
            if (!existsAlready) {
                openCLDefines.put("IMAGE_SIZE_" + variableName + "_WIDTH", 0);
                openCLDefines.put("IMAGE_SIZE_" + variableName + "_HEIGHT", 0);
                openCLDefines.put("IMAGE_SIZE_" + variableName + "_DEPTH", 0);
            }
        }

        if (CLIJ.debug) {
            for (String key : openCLDefines.keySet()) {
                System.out.println(key + " = " + openCLDefines.get(key));
            }
        }



        ClearCLKernel clearCLKernel = null;

        try {

            if (openCLDefines != null) {
                clearCLKernel = getKernel(context, kernelName, openCLDefines);
            } else {
                clearCLKernel = getKernel(context, kernelName);
            }

        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }

        if (clearCLKernel != null) {
            if (globalSizes != null) {
                clearCLKernel.setGlobalSizes(globalSizes);
            } else if (dstImage != null) {
                clearCLKernel.setGlobalSizes(dstImage.getDimensions());
            } else if (dstBuffer != null) {
                clearCLKernel.setGlobalSizes(dstBuffer.getDimensions());
            }
            if (parameterMap != null) {
                for (String key : parameterMap.keySet()) {
                    clearCLKernel.setArgument(key, parameterMap.get(key));
                }
            }
            if (CLIJ.debug) {
                System.out.println("Executing " + kernelName);
            }

            final ClearCLKernel kernel = clearCLKernel;
            double duration = ElapsedTime.measure("Pure kernel execution", () -> {
                try {
                    kernel.run(waitToFinish);
                } catch (Exception e) {
                    e.printStackTrace();

                    System.out.println(kernel.getSourceCode());
                }
            });
            if (CLIJ.debug) {
                System.out.println("Returned from " + kernelName + " after " + duration + " msec" );
            }
            clearCLKernel.close();
        }

        return true;
    }

    private HashMap<String, ArrayList<String>> variableListMap = new HashMap<String, ArrayList<String>>();
    private ArrayList<String> getImageVariablesFromSource() {
        String key = anchorClass.getName() + "_" + programFilename;

        if (variableListMap.containsKey(key)) {
            return variableListMap.get(key);
        }
        ArrayList<String> variableList = new ArrayList<String>();

        String sourceCode = getProgramSource();
        String[] kernels = sourceCode.split("__kernel");

        kernels[0] = "";
        for (String kernel : kernels) {
            if (kernel.length() > 0 ) {
                //System.out.println("Parsing " + kernel.split("\\(")[0]);
                String temp1 = kernel.split("\\(")[1];
                if (temp1.length() > 0) {
                    String parameterText = temp1.split("\\)")[0];
                    parameterText = parameterText.replace("\n", " ");
                    parameterText = parameterText.replace("\t", " ");
                    parameterText = parameterText.replace("\r", " ");

                    //System.out.println("Parsing parameters " + parameterText);

                    String[] parameters = parameterText.split(",");
                    for (String parameter : parameters) {
                        if (parameter.contains("IMAGE")) {
                            String[] temp2 = parameter.trim().split(" ");
                            String variableName = temp2[temp2.length - 1];

                            variableList.add(variableName);

                        }
                    }
                }
            }
        }

        variableListMap.put(key, variableList);
        return variableList;
    }

    private HashMap<String, String> sourceCodeCache = new HashMap<String, String>();
    protected String getProgramSource() {
        String key = anchorClass.getName() + "_" + programFilename;

        if (sourceCodeCache.containsKey(key)) {
            return sourceCodeCache.get(key);
        }
        try {
            ClearCLProgram program = context.createProgram(this.anchorClass, new String[]{this.programFilename});
            String source = program.getSourceCode();
            sourceCodeCache.put(key, source);
            return source;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setAnchorClass(Class anchorClass) {
        this.anchorClass = anchorClass;
    }

    public void setProgramFilename(String programFilename) {
        this.programFilename = programFilename;
    }

    public void setKernelName(String kernelName) {
        this.kernelName = kernelName;
    }

    public void setGlobalSizes(long[] globalSizes) {
        this.globalSizes = globalSizes;
    }

    protected ClearCLKernel getKernel(ClearCLContext context, String kernelName) throws IOException {
        return this.getKernel(context, kernelName, (Map) null);
    }

    protected ClearCLKernel getKernel(ClearCLContext context, String kernelName, Map<String, Object> defines) throws IOException {

        String programCacheKey = anchorClass.getCanonicalName() + " " + programFilename;
        for (String key : defines.keySet()) {
            programCacheKey = programCacheKey + " " + (key + " = " + defines.get(key));
        }
        if (CLIJ.debug) {
            System.out.println("Program cache hash:" + programCacheKey);
        }
        ClearCLProgram clProgram = this.programCacheMap.get(programCacheKey);
        currentProgram = clProgram;
        if (clProgram == null) {
            clProgram = context.createProgram(this.anchorClass, new String[]{this.programFilename});
            if (defines != null) {
                Iterator iterator = defines.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry) iterator.next();
                    if (entry.getValue() instanceof String) {
                        clProgram.addDefine((String) entry.getKey(), (String) entry.getValue());
                    } else if (entry.getValue() instanceof Number) {
                        clProgram.addDefine((String) entry.getKey(), (Number) entry.getValue());
                    } else if (entry.getValue() == null) {
                        clProgram.addDefine((String) entry.getKey());
                    }
                }
            }

            clProgram.addBuildOptionAllMathOpt();
            clProgram.buildAndLog();
            //System.out.println("status: " + mProgram.getBuildStatus());
            //System.out.println("LOG: " + this.mProgram.getBuildLog());

            programCacheMap.put(programCacheKey, clProgram);
        }
        //System.out.println(clProgram.getSourceCode());
        //System.out.println(pKernelName);


        try {
            return clProgram.createKernel(kernelName);
        } catch (OpenCLException e) {
            System.out.println("Error when trying to create kernel " + kernelName);
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        for (String key : programCacheMap.keySet()) {
            ClearCLProgram program = programCacheMap.get(key);
            program.close();
        }
        programCacheMap.clear();
    }
}
