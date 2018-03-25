package clearcl.imagej.utilities;

import clearcl.ClearCLContext;
import clearcl.ClearCLImage;
import clearcl.ClearCLKernel;
import clearcl.enums.ImageChannelDataType;
import fastfuse.FastFusionEngine;
import fastfuse.FastFusionEngineInterface;
import fastfuse.tasks.TaskBase;

import fastfuse.tasks.TaskHelper;

import java.io.IOException;
import java.util.HashMap;
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
                                   boolean pWaitToFinish)
  {
    setupProgram(mAnchorClass, mProgramFilename);

    ClearCLImage lSrcImage = null;
    ClearCLImage lDstImage = null;

    if (mParameterMap != null) {
      for (String key : mParameterMap.keySet()) {
        if (key.contains("src") || key.contains("input")) {
          lSrcImage = (ClearCLImage) mParameterMap.get(key);
        } else if (key.contains("dst") || key.contains("output")) {
          lDstImage = (ClearCLImage) mParameterMap.get(key);
        }
      }
    }

    ClearCLKernel lClearCLKernel = null;
    if (lSrcImage != null && lDstImage != null) {

      try
      {
        lClearCLKernel =
            getKernel(mContext, mKernelName,
                      TaskHelper.getOpenCLDefines(lSrcImage,
                                                  lDstImage));
      }
      catch (IOException e1)
      {
        e1.printStackTrace();
        return false;
      }

    } else
    {
      try
      {
        lClearCLKernel =
        getKernel(mContext,
                  mKernelName);
      }
      catch (IOException e1)
      {
        e1.printStackTrace();
        return false;
      }
    }

    if (lClearCLKernel != null)
    {
      if (mGlobalSizes != null) {
          lClearCLKernel.setGlobalSizes(mGlobalSizes);
      } else {
          lClearCLKernel.setGlobalSizes(lDstImage.getDimensions());
      }
      if (mParameterMap != null)
      {
        for (String key : mParameterMap.keySet())
        {
          lClearCLKernel.setArgument(key, mParameterMap.get(key));
        }
      }
      runKernel(lClearCLKernel, pWaitToFinish);
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
}
