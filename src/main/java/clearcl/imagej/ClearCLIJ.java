package clearcl.imagej;

import clearcl.*;
import clearcl.backend.ClearCLBackendInterface;
import clearcl.backend.javacl.ClearCLBackendJavaCL;
import clearcl.enums.ImageChannelDataType;
import clearcl.imagej.utilities.GenericBinaryFastFuseTask;
import clearcl.imagej.utilities.ImageTypeConverter;
import clearcontrol.stack.OffHeapPlanarStack;
import coremem.enums.NativeTypeEnum;
import fastfuse.FastFusionEngine;
import fastfuse.FastFusionMemoryPool;
import ij.ImagePlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class ClearCLIJ
{

  protected ClearCLContext mClearCLContext;
  private ClearCLDevice mClearCLDevice;
  private ClearCL mClearCL;
  private static ClearCLIJ sInstance = null;

  private FastFusionEngine mFastFusionEngine;

  public static ClearCLIJ getInstance()
  {
    if (sInstance == null)
    {
      sInstance = new ClearCLIJ(null);
    }
    return sInstance;
  }

  public ClearCLIJ(String pDeviceNameMustContain)
  {
    ClearCLBackendInterface
        lClearCLBackend =
        new ClearCLBackendJavaCL();

    mClearCL = new ClearCL(lClearCLBackend);
    if (pDeviceNameMustContain == null)
    {
      mClearCLDevice = mClearCL.getFastestGPUDeviceForImages();
    } else {
      mClearCLDevice = mClearCL.getDeviceByName(pDeviceNameMustContain);
    }

    if (mClearCLDevice == null) {
      System.err.println("Warning: Optimal ClearCL device determination failed. Retrying using first device found.");
      mClearCLDevice = mClearCL.getAllDevices().get(0);
    }

    mClearCLContext = mClearCLDevice.createContext();

    mFastFusionEngine = new FastFusionEngine(mClearCLContext);

    FastFusionMemoryPool.getInstance(mClearCLContext);
  }

  public boolean execute(String pProgramFilename,
                         String pKernelname,
                         Map<String, Object> pParameterMap) throws
                                                            IOException
  {
    return execute(Object.class, pProgramFilename, pKernelname, pParameterMap);
  }

  public boolean execute(Class pAnchorClass,
                         String pProgramFilename,
                         String pKernelname,
                         Map<String, Object> pParameterMap) throws
                                                            IOException
  {
    GenericBinaryFastFuseTask
        lGenericUnaryFastFuseTask =
        new GenericBinaryFastFuseTask(mFastFusionEngine,
                                      pAnchorClass,
                                      pProgramFilename,
                                      pKernelname);
    lGenericUnaryFastFuseTask.setParameterMap(pParameterMap);

    //mFastFusionEngine.addTask(lGenericUnaryFastFuseTask);

    //ImageChannelDataType lType = determineType(pInputImageStack.getDataType());

    //mFastFusionEngine.passImage(lGenericSrcImageName, pInputImageStack.getContiguousMemory(), lType, pInputImageStack.getDimensions());

    //mFastFusionEngine.executeAllTasks();

    return lGenericUnaryFastFuseTask.enqueue(null, true);

    //if (mFastFusionEngine.isImageAvailable(lGenericDstImageName)) {
    //  return convertToOffHeapPlanarStack(mFastFusionEngine.getImage(lGenericDstImageName));
    //} else {
    //  return null;
    //}

  }

  public void dispose()
  {
    mClearCLContext.close();
    if (sInstance == this)
    {
      sInstance = null;
    }
  }

  /**
   * Deprecated because it should not be neccessary to get the context.
   * The context should be shadowed inside.
   *
   * @return
   */
  @Deprecated
  public ClearCLContext getClearCLContext()
  {
    return mClearCLContext;
  }

  public Map<String, Object> parameters(Object... pParameterList) {
    Map<String, Object> lResultMap = new HashMap<String, Object>();
    for (int i = 0; i < pParameterList.length; i+=2) {
      lResultMap.put((String)pParameterList[i], pParameterList[i+1]);
    }
    return lResultMap;
  }

  public ImageTypeConverter converter(OffHeapPlanarStack pStack) {
    return new ImageTypeConverter(mClearCLContext, pStack);
  }

  public <T extends RealType<T>> ImageTypeConverter<T> converter(RandomAccessibleInterval<T> pRandomAccessibleInterval) {
    return new ImageTypeConverter<T>(mClearCLContext, pRandomAccessibleInterval);
  }

  public ImageTypeConverter converter(ImagePlus pImagePlus) {
    return new ImageTypeConverter(mClearCLContext, pImagePlus);
  }

  public <T extends RealType<T>> ImageTypeConverter<T> converter(ClearCLImage pClearCLImage) {
    return new ImageTypeConverter<T>(mClearCLContext, pClearCLImage);
  }

  public static String clinfo() {
    StringBuffer output = new StringBuffer();

    try
    {
      ClearCLBackendInterface lClearCLBackend = new ClearCLBackendJavaCL();

      output.append("CL backend: " + lClearCLBackend + "\n");

      ClearCL lClearCL = new ClearCL(lClearCLBackend);

      output.append("ClearCL: " + lClearCL + "\n");
      output.append("  Number of platforms:" + lClearCL.getNumberOfPlatforms() + "\n");
      for (int p = 0; p < lClearCL.getNumberOfPlatforms(); p++)
      {
        ClearCLPlatform lClearCLPlatform = lClearCL.getPlatform(p);
        output.append("  [" + p + "] " + lClearCLPlatform.getName() + "\n");
        output.append("     Number of devices: " + lClearCLPlatform.getNumberOfDevices() + "\n");

        output.append("     Available devices: \n");
        for (int d = 0; d < lClearCLPlatform.getNumberOfDevices(); d++)
        {
          ClearCLDevice lDevice = lClearCLPlatform.getDevice(d);
          output.append("     [" + d + "] " + lDevice.getName() + " \n");
          output.append("        NumberOfComputeUnits: "
                        + lDevice.getNumberOfComputeUnits()
                        + " \n");
          output.append("        Clock frequency: "
                        + lDevice.getClockFrequency()
                        + " \n");
          output.append("        Version: " + lDevice.getVersion() + " \n");
          output.append("        Extensions: " + lDevice.getExtensions() + " \n");
          output.append("        GlobalMemorySizeInBytes: "
                        + lDevice.getGlobalMemorySizeInBytes()
                        + " \n");
          output.append("        LocalMemorySizeInBytes: "
                        + lDevice.getLocalMemorySizeInBytes()
                        + " \n");
          output.append("        MaxMemoryAllocationSizeInBytes: "
                        + lDevice.getMaxMemoryAllocationSizeInBytes()
                        + " \n");
          output.append("        MaxWorkGroupSize: "
                        + lDevice.getMaxWorkGroupSize()
                        + " \n");
        }
      }

      output.append("Best GPU device for images: " + lClearCL.getFastestGPUDeviceForImages().getName() + "\n");
      output.append("Best largest GPU device: " + lClearCL.getLargestGPUDevice().getName() + "\n");
      output.append("Best CPU device: " + lClearCL.getBestCPUDevice().getName() + "\n");
    }
    catch (Exception e) {
      output.append("\n\nException: " + e.toString());
      return output.toString();
    }
    return output.toString();
  }

}
