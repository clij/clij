package clearcl.imagej.utilities;

import clearcl.*;
import clearcl.enums.ImageChannelDataType;
import coremem.enums.NativeTypeEnum;
import fastfuse.FastFusionEngine;
import fastfuse.FastFusionEngineInterface;
import fastfuse.tasks.TaskBase;

import fastfuse.tasks.TaskHelper;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This generic task class is wrapped around all given .cl files. It
 * uses some functionality from FastFuse, to make .cl file handling
 * easier. For example, it ensures that the right
 * image_read/image_write methods are called depending on the image
 * type.
 *
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class GenericBinaryFastFuseTask extends TaskBase
{
  ClearCLContext mContext;

  Class mAnchorClass;
  String mProgramFilename;
  String mKernelName;
  Map<String, Object> mParameterMap;
  long[] mGlobalSizes;
  private ClearCLProgram mProgram;

  private HashMap<String, ClearCLKernel> mKernelMap = new HashMap();

  public GenericBinaryFastFuseTask(FastFusionEngine pFastFusionEngine,
                                   Class pAnchorClass,
                                   String pProgramFilename,
                                   String pKernelName,
                                   long[] pGlobalSizes) throws
                                                           IOException {
    super();
    mProgramFilename = pProgramFilename;
    mAnchorClass = pAnchorClass;
    mKernelName = pKernelName;
    mContext = pFastFusionEngine.getContext();
    mGlobalSizes = pGlobalSizes;
  }

  /**
   * Map of all parameters. It is recommended that input and output
   * images are given with the names "src" and "dst", respectively.
   * @param pParameterMap
   */
  public void setParameterMap(Map<String, Object> pParameterMap)
  {
    mParameterMap = pParameterMap;
  }

  @Override public boolean enqueue(FastFusionEngineInterface pFastFusionEngine,
                                   boolean pWaitToFinish) {
    setupProgram(mAnchorClass, mProgramFilename);

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

    Map<String, Object> lOpenCLDefines  = new HashMap();
    if (lSrcImage != null) {
      getOpenCLDefines(lOpenCLDefines, lSrcImage.getChannelDataType(), true);
    }
    if (lDstImage != null) {
      getOpenCLDefines(lOpenCLDefines, lDstImage.getChannelDataType(), false);
    }
    if (lSrcBuffer != null) {
      getOpenCLDefines(lOpenCLDefines, lSrcBuffer.getNativeType(), lSrcBuffer.getWidth(), lSrcBuffer.getHeight(), true);
    }
    if (lDstBuffer != null) {
      getOpenCLDefines(lOpenCLDefines, lDstBuffer.getNativeType(), lDstBuffer.getWidth(), lDstBuffer.getHeight(), false);
    }

    String lKernelCacheKey = mKernelName;
    for (String key : lOpenCLDefines.keySet()) {
        lKernelCacheKey = lKernelCacheKey + " " + (key + " = " + lOpenCLDefines.get(key));
    }

    ClearCLKernel lClearCLKernel = null;

    try {

      if (lOpenCLDefines != null) {
        lClearCLKernel =
                getKernel(mContext, mKernelName, lKernelCacheKey,
                        lOpenCLDefines);
      } else {
        lClearCLKernel =
                getKernel(mContext, mKernelName,
                        lKernelCacheKey);
      }

    } catch (IOException e1) {
      e1.printStackTrace();
      return false;
    }

    if (lClearCLKernel != null)
    {
      if (mGlobalSizes != null) {
          lClearCLKernel.setGlobalSizes(mGlobalSizes);
      } else if (lDstImage != null){
          lClearCLKernel.setGlobalSizes(lDstImage.getDimensions());
      } else if (lDstBuffer != null){
          lClearCLKernel.setGlobalSizes(lDstBuffer.getDimensions());
      }
      if (mParameterMap != null)
      {
        for (String key : mParameterMap.keySet())
        {
          lClearCLKernel.setArgument(key, mParameterMap.get(key));
        }
      }
      try
      {
        runKernel(lClearCLKernel, pWaitToFinish);
      } catch (Exception e) {
        e.printStackTrace();

        System.out.println(lClearCLKernel.getSourceCode());
      }
    }

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

  public static void getOpenCLDefines(Map<String, Object> lDefines, ImageChannelDataType pDType, boolean pInput) {
    if (pInput) {
      lDefines.put("DTYPE_IMAGE_IN_3D", "__read_only image3d_t");
      lDefines.put("DTYPE_IMAGE_IN_2D", "__read_only image2d_t");
      lDefines.put("DTYPE_IN", pDType.isInteger() ? "ushort" : "float");
      lDefines.put("READ_IMAGE", pDType.isInteger() ? "read_imageui" : "read_imagef");
    } else {
      lDefines.put("DTYPE_IMAGE_OUT_3D", "__write_only image3d_t");
      lDefines.put("DTYPE_IMAGE_OUT_2D", "__write_only image2d_t");
      lDefines.put("DTYPE_OUT", pDType.isInteger() ? "ushort" : "float");
      lDefines.put("WRITE_IMAGE", pDType.isInteger() ? "write_imageui" : "write_imagef");
    }
  }

  public static void getOpenCLDefines(Map<String, Object> lDefines, NativeTypeEnum pDType, long width, long height, boolean pInput) {
    if (pInput) {
      lDefines.put("DTYPE_IMAGE_IN_3D", pDType != NativeTypeEnum.Float ? "__global uint*" : "__global float*");
      lDefines.put("DTYPE_IMAGE_IN_2D", pDType != NativeTypeEnum.Float ? "__global uint*" : "__global float*");
      lDefines.put("DTYPE_IN", pDType != NativeTypeEnum.Float ? "ushort" : "float");
      lDefines.put("READ_IMAGE(a,b,c)", pDType != NativeTypeEnum.Float ? "read_bufferui(" + width + "," + height + ",a,b,c)" : "read_bufferf(" + width + "," + height + ",a,b,c)");
    } else {
      lDefines.put("DTYPE_IMAGE_OUT_3D", pDType != NativeTypeEnum.Float ? "__global uint*" : "__global float*");
      lDefines.put("DTYPE_IMAGE_OUT_2D", pDType != NativeTypeEnum.Float ? "__global uint*" : "__global float*");
      lDefines.put("DTYPE_OUT", pDType != NativeTypeEnum.Float ? "uint" : "float");
      lDefines.put("WRITE_IMAGE(a,b,c)", pDType != NativeTypeEnum.Float ? "write_bufferui(" + width + "," + height + ",a,b,c)" : "write_bufferf(" + width + "," + height + ",a,b,c)");
    }
  }

    protected ClearCLKernel getKernel(ClearCLContext pContext, String pKernelName, String pKernelCacheKey) throws IOException {
        return this.getKernel(pContext, pKernelName, pKernelCacheKey, (Map)null);
    }

    protected ClearCLKernel getKernel(ClearCLContext pContext, String pKernelName, String pKernelCacheKey, Map<String, Object> pDefines) throws IOException {
        if (this.mKernelMap.get(pKernelCacheKey) != null) {
            return (ClearCLKernel)this.mKernelMap.get(pKernelCacheKey);
        } else {
            this.mProgram = pContext.createProgram(this.mAnchorClass, new String[]{this.mProgramFilename});
            if (pDefines != null) {
                Iterator var4 = pDefines.entrySet().iterator();

                while(var4.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry)var4.next();
                    if (entry.getValue() instanceof String) {
                        this.mProgram.addDefine((String)entry.getKey(), (String)entry.getValue());
                    } else if (entry.getValue() instanceof Number) {
                        this.mProgram.addDefine((String)entry.getKey(), (Number)entry.getValue());
                    } else if (entry.getValue() == null) {
                        this.mProgram.addDefine((String)entry.getKey());
                    }
                }
            }

            this.mProgram.addBuildOptionAllMathOpt();
            this.mProgram.buildAndLog();
            System.out.println("status: " + mProgram.getBuildStatus());
            System.out.println("LOG: " + this.mProgram.getBuildLog());
            ClearCLKernel lKernel = this.mProgram.createKernel(pKernelName);
            this.mKernelMap.put(pKernelName, lKernel);
            return lKernel;
        }
    }


}
