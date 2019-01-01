package net.haesleinhuepf.clij.utilities;

import clearcl.*;
import clearcl.enums.ImageChannelDataType;
import clearcl.util.ElapsedTime;
import coremem.enums.NativeTypeEnum;

import java.io.IOException;
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
    ClearCLContext mContext;
    Class mAnchorClass;
    String mProgramFilename;
    String mKernelName;
    Map<String, Object> mParameterMap;
    long[] mGlobalSizes;
    private String mSourceFile;

    private HashMap<String, ClearCLProgram> mProgramCacheMap = new HashMap();

    public CLKernelExecutor(ClearCLContext pContext,
                            Class pAnchorClass,
                            String pProgramFilename,
                            String pKernelName,
                            long[] pGlobalSizes) throws
            IOException {
        super();
        mProgramFilename = pProgramFilename;
        mAnchorClass = pAnchorClass;
        mKernelName = pKernelName;
        mContext = pContext;
        mGlobalSizes = pGlobalSizes;
    }

    public static void getOpenCLDefines(Map<String, Object> lDefines, ImageChannelDataType pDType, boolean pInput) {
        if (pInput) {
            lDefines.put("DTYPE_IMAGE_IN_3D", "__read_only image3d_t");
            lDefines.put("DTYPE_IMAGE_IN_2D", "__read_only image2d_t");
            if (pDType.isInteger()) {
                if (pDType == ImageChannelDataType.UnsignedInt8 || pDType == ImageChannelDataType.SignedInt8) {
                    lDefines.put("DTYPE_IN", "char");
                } else {
                    lDefines.put("DTYPE_IN", "ushort");
                }
            } else {
                lDefines.put("DTYPE_IN", "float");
            }
            lDefines.put("READ_IMAGE_2D", pDType.isInteger() ? "read_imageui" : "read_imagef");
            lDefines.put("READ_IMAGE_3D", pDType.isInteger() ? "read_imageui" : "read_imagef");
            //lDefines.put("GET_IMAGE_IN_WIDTH(a)", "get_image_width(a)");
            //lDefines.put("GET_IMAGE_IN_HEIGHT(a)", "get_image_height(a)");
            //lDefines.put("GET_IMAGE_IN_DEPTH(a)", "get_image_depth(a)");
        } else {
            lDefines.put("DTYPE_IMAGE_OUT_3D", "__write_only image3d_t");
            lDefines.put("DTYPE_IMAGE_OUT_2D", "__write_only image2d_t");
            //lDefines.put("DTYPE_OUT", pDType.isInteger() ? "ushort" : "float");
            if (pDType.isInteger()) {
                if (pDType == ImageChannelDataType.UnsignedInt8 || pDType == ImageChannelDataType.SignedInt8) {
                    lDefines.put("DTYPE_OUT", "char");
                } else {
                    lDefines.put("DTYPE_OUT", "ushort");
                }
            } else {
                lDefines.put("DTYPE_OUT", "float");
            }
            lDefines.put("WRITE_IMAGE_2D", pDType.isInteger() ? "write_imageui" : "write_imagef");
            lDefines.put("WRITE_IMAGE_3D", pDType.isInteger() ? "write_imageui" : "write_imagef");
            //lDefines.put("GET_IMAGE_OUT_WIDTH(a)", "get_image_width(a)");
            //lDefines.put("GET_IMAGE_OUT_HEIGHT(a)", "get_image_height(a)");
            //lDefines.put("GET_IMAGE_OUT_DEPTH(a)", "get_image_depth(a)");
        }
    }

    public static void getOpenCLDefines(Map<String, Object> lDefines, NativeTypeEnum pDType, long width, long height, long depth, long dimension, boolean pInput) {
        String typeName = nativeTypeToOpenCLTypeName(pDType);
        String typeId = nativeTypeToOpenCLTypeShortName(pDType);

        if (pInput) {

            lDefines.put("DTYPE_IN", typeName);
            lDefines.put("DTYPE_IMAGE_IN_3D", "__global " + typeName + "*");
            lDefines.put("DTYPE_IMAGE_IN_2D", "__global " + typeName + "*");
            lDefines.put("READ_IMAGE_2D(a,b,c)", "read_buffer2d" + typeId + "(" + width + "," + height + "," + depth + ",a,b,c)");
            lDefines.put("READ_IMAGE_3D(a,b,c)", "read_buffer3d" + typeId + "(" + width + "," + height + "," + depth + ",a,b,c)");
            //lDefines.put("GET_IMAGE_IN_WIDTH(a)", "get_buffer" + typeId + "_width(" + width + ",a)");
            //lDefines.put("GET_IMAGE_IN_HEIGHT(a)", "get_buffer" + typeId + "_height(" + height + ",a)");
            //lDefines.put("GET_IMAGE_IN_DEPTH(a)", "get_buffer" + typeId + "_depth(" + depth + ",a)");
        } else {
            lDefines.put("DTYPE_OUT", typeName);
            lDefines.put("DTYPE_IMAGE_OUT_3D", "__global " + typeName + "*");
            lDefines.put("DTYPE_IMAGE_OUT_2D", "__global " + typeName + "*");
            lDefines.put("WRITE_IMAGE_2D(a,b,c)", "write_buffer2d" + typeId + "(" + width + "," + height + "," + depth + ",a,b,c)");
            lDefines.put("WRITE_IMAGE_3D(a,b,c)", "write_buffer3d" + typeId + "(" + width + "," + height + "," + depth + ",a,b,c)");
            //lDefines.put("GET_IMAGE_OUT_WIDTH(a)", "get_buffer" + typeId + "_width(" + width + ",a)");
            //lDefines.put("GET_IMAGE_OUT_HEIGHT(a)", "get_buffer" + typeId + "_height(" + height + ",a)");
            //lDefines.put("GET_IMAGE_OUT_DEPTH(a)", "get_buffer" + typeId + "_depth(" + depth + ",a)");
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
     * @param pParameterMap
     */
    public void setParameterMap(Map<String, Object> pParameterMap) {
        mParameterMap = pParameterMap;
    }

    public boolean enqueue(boolean pWaitToFinish) {

        ClearCLImage lSrcImage = null;
        ClearCLImage lDstImage = null;
        ClearCLBuffer lSrcBuffer = null;
        ClearCLBuffer lDstBuffer = null;

        if (mParameterMap != null) {
            for (String key : mParameterMap.keySet()) {
                if (mParameterMap.get(key) instanceof ClearCLImage) {
                    if (key.contains("src") || key.contains("input")) {
                        lSrcImage = (ClearCLImage) mParameterMap.get(key);
                    } else if (key.contains("dst") || key.contains("output")) {
                        lDstImage = (ClearCLImage) mParameterMap.get(key);
                    }
                } else if (mParameterMap.get(key) instanceof ClearCLBuffer) {
                    if (key.contains("src") || key.contains("input")) {
                        lSrcBuffer = (ClearCLBuffer) mParameterMap.get(key);
                    } else if (key.contains("dst") || key.contains("output")) {
                        lDstBuffer = (ClearCLBuffer) mParameterMap.get(key);
                    }
                }
            }
        }

        if (lDstImage == null && lDstBuffer == null) {
            if (lSrcImage != null) {
                lDstImage = lSrcImage;
            } else if (lSrcBuffer != null) {
                lDstBuffer = lSrcBuffer;
            }
        } else if (lSrcImage == null && lSrcBuffer == null) {
            if (lDstImage != null) {
                lSrcImage = lDstImage;
            } else if (lDstBuffer != null) {
                lSrcBuffer = lDstBuffer;
            }
        }


        Map<String, Object> lOpenCLDefines = new HashMap();
        lOpenCLDefines.put("MAX_ARRAY_SIZE", MAX_ARRAY_SIZE);
        if (lSrcImage != null) {
            getOpenCLDefines(lOpenCLDefines, lSrcImage.getChannelDataType(), true);
        }
        if (lDstImage != null) {
            getOpenCLDefines(lOpenCLDefines, lDstImage.getChannelDataType(), false);
        }
        if (lSrcBuffer != null) {
            getOpenCLDefines(lOpenCLDefines, lSrcBuffer.getNativeType(), lSrcBuffer.getWidth(), lSrcBuffer.getHeight(), lSrcBuffer.getDepth(), lSrcBuffer.getDimension(), true);
        }
        if (lDstBuffer != null) {
            getOpenCLDefines(lOpenCLDefines, lDstBuffer.getNativeType(), lDstBuffer.getWidth(), lDstBuffer.getHeight(), lDstBuffer.getDepth(), lDstBuffer.getDimension(), false);
        }

        // deal with image width/height/depth for all images and buffers

        for (String key : mParameterMap.keySet()) {
            if (mParameterMap.get(key) instanceof ClearCLImage) {
                ClearCLImage image = (ClearCLImage) mParameterMap.get(key);
                lOpenCLDefines.put("IMAGE_SIZE_" + mKernelName + "_" + key + "_WIDTH", image.getWidth());
                lOpenCLDefines.put("IMAGE_SIZE_" + mKernelName + "_" + key + "_HEIGHT", image.getHeight());
                lOpenCLDefines.put("IMAGE_SIZE_" + mKernelName + "_" + key + "_DEPTH", image.getDepth());
            } else if (mParameterMap.get(key) instanceof ClearCLBuffer) {
                ClearCLBuffer image = (ClearCLBuffer) mParameterMap.get(key);
                lOpenCLDefines.put("IMAGE_SIZE_" + mKernelName + "_" + key + "_WIDTH", image.getWidth());
                lOpenCLDefines.put("IMAGE_SIZE_" + mKernelName + "_" + key + "_HEIGHT", image.getHeight());
                lOpenCLDefines.put("IMAGE_SIZE_" + mKernelName + "_" + key + "_DEPTH", image.getDepth());
            }
        }

        lOpenCLDefines.put("GET_IMAGE_IN_WIDTH(image_key)", "IMAGE_SIZE_" + mKernelName + "_ ## image_key ## _WIDTH");
        lOpenCLDefines.put("GET_IMAGE_IN_HEIGHT(image_key)", "IMAGE_SIZE_" + mKernelName + "_ ## image_key ## _HEIGHT");
        lOpenCLDefines.put("GET_IMAGE_IN_DEPTH(image_key)", "IMAGE_SIZE_" + mKernelName + "_ ## image_key ## _DEPTH");
        lOpenCLDefines.put("GET_IMAGE_OUT_WIDTH(image_key)", "IMAGE_SIZE_" + mKernelName + "_ ## image_key ## _WIDTH");
        lOpenCLDefines.put("GET_IMAGE_OUT_HEIGHT(image_key)", "IMAGE_SIZE_" + mKernelName + "_ ## image_key ## _HEIGHT");
        lOpenCLDefines.put("GET_IMAGE_OUT_DEPTH(image_key)", "IMAGE_SIZE_" + mKernelName + "_ ## image_key ## _DEPTH");
        lOpenCLDefines.put("GET_IMAGE_WIDTH(image_key)", "IMAGE_SIZE_" + mKernelName + "_ ## image_key ## _WIDTH");
        lOpenCLDefines.put("GET_IMAGE_HEIGHT(image_key)", "IMAGE_SIZE_" + mKernelName + "_ ## image_key ## _HEIGHT");
        lOpenCLDefines.put("GET_IMAGE_DEPTH(image_key)", "IMAGE_SIZE_" + mKernelName + "_ ## image_key ## _DEPTH");

        //for (String define : lOpenCLDefines.keySet()) {
        //  System.out.println(define + " = " + lOpenCLDefines.get(define));
        //}

        ClearCLKernel lClearCLKernel = null;

        try {

            if (lOpenCLDefines != null) {
                lClearCLKernel =
                        getKernel(mContext, mKernelName, lOpenCLDefines);
            } else {
                lClearCLKernel =
                        getKernel(mContext, mKernelName);
            }

        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }

        if (lClearCLKernel != null) {
            if (mGlobalSizes != null) {
                lClearCLKernel.setGlobalSizes(mGlobalSizes);
            } else if (lDstImage != null) {
                lClearCLKernel.setGlobalSizes(lDstImage.getDimensions());
            } else if (lDstBuffer != null) {
                lClearCLKernel.setGlobalSizes(lDstBuffer.getDimensions());
            }
            if (mParameterMap != null) {
                for (String key : mParameterMap.keySet()) {
                    lClearCLKernel.setArgument(key, mParameterMap.get(key));
                }
            }
            //System.out.println("Exec " + mProgramCacheMap.size());

            final ClearCLKernel kernel = lClearCLKernel;
            ElapsedTime.measureForceOutput("Pure kernel execution", () -> {
                try {
                    kernel.run(pWaitToFinish);
                } catch (Exception e) {
                    e.printStackTrace();

                    System.out.println(kernel.getSourceCode());
                }
            });

            //System.out.println("Ret");
            lClearCLKernel.close();
        }

        //for (ClearCLProgram program : mProgramCacheMap.values()) {
        //  program.close();
        //}
        //mProgramCacheMap.clear();

        return true;
    }

    public void setAnchorClass(Class mAnchorClass) {
        this.mAnchorClass = mAnchorClass;
    }

    public void setProgramFilename(String mProgramFilename) {
        this.mProgramFilename = mProgramFilename;
    }

    public void setKernelName(String mKernelName) {
        this.mKernelName = mKernelName;
    }

    public void setGlobalSizes(long[] pGlobalSizes) {
        mGlobalSizes = pGlobalSizes;
    }

    protected ClearCLKernel getKernel(ClearCLContext pContext, String pKernelName) throws IOException {
        return this.getKernel(pContext, pKernelName, (Map) null);
    }

    protected ClearCLKernel getKernel(ClearCLContext pContext, String pKernelName, Map<String, Object> pDefines) throws IOException {

        String lProgramCacheKey = mAnchorClass.getCanonicalName() + " " + mProgramFilename;
        for (String key : pDefines.keySet()) {
            lProgramCacheKey = lProgramCacheKey + " " + (key + " = " + pDefines.get(key));
        }

        //System.out.println("Cache key:" + lProgramCacheKey);
        ClearCLProgram clProgram = this.mProgramCacheMap.get(lProgramCacheKey);
        if (clProgram == null) {
            clProgram = pContext.createProgram(this.mAnchorClass, new String[]{this.mProgramFilename});
            if (pDefines != null) {
                Iterator var4 = pDefines.entrySet().iterator();

                while (var4.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry) var4.next();
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

            mProgramCacheMap.put(lProgramCacheKey, clProgram);
        }
        //System.out.println(clProgram.getSourceCode());
        //System.out.println(pKernelName);
        ClearCLKernel lKernel = clProgram.createKernel(pKernelName);
        return lKernel;
    }
}
