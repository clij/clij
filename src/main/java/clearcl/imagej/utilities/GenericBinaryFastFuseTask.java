package clearcl.imagej.utilities;

import clearcl.ClearCLContext;
import clearcl.ClearCLImage;
import clearcl.ClearCLKernel;
import fastfuse.FastFusionEngine;
import fastfuse.FastFusionEngineInterface;
import fastfuse.tasks.TaskBase;

import fastfuse.tasks.TaskHelper;

import java.io.IOException;
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

  String mKernelName;
  Map<String, Object> mParameterMap;

  public GenericBinaryFastFuseTask(FastFusionEngine pFastFusionEngine,
                                   Class pAnchorClass,
                                   String pProgramFilename,
                                   String pKernelName) throws
                                                           IOException
  {
    super();
    setupProgram(pAnchorClass, pProgramFilename);
    mKernelName = pKernelName;
    mContext = pFastFusionEngine.getContext();
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
    ClearCLImage lSrcImage = null;
    ClearCLImage lDstImage = null;

    if (mParameterMap != null) {
      for (String key : mParameterMap.keySet()) {
        if (key == "src") {
          lSrcImage = (ClearCLImage) mParameterMap.get(key);
        } else if (key == "dst") {
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
      lClearCLKernel.setGlobalSizes(lDstImage.getDimensions());

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
}
