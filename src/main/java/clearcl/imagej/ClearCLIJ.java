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
import java.util.Map;

/**
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class ClearCLIJ
{

  protected ClearCLContext mClearCLContext;
  private final ClearCLDevice mClearCLDevice;
  private ClearCL mClearCL;
  private static ClearCLIJ sInstance = null;

  private FastFusionEngine mFastFusionEngine;

  public static ClearCLIJ getInstance()
  {
    if (sInstance == null)
    {
      sInstance = new ClearCLIJ();
    }
    return sInstance;
  }

  private ClearCLIJ()
  {
    ClearCLBackendInterface
        lClearCLBackend =
        new ClearCLBackendJavaCL();

    mClearCL = new ClearCL(lClearCLBackend);
    mClearCLDevice = mClearCL.getFastestGPUDeviceForImages();
    mClearCLContext = mClearCLDevice.createContext();

    mFastFusionEngine = new FastFusionEngine(mClearCLContext);

    FastFusionMemoryPool.getInstance(mClearCLContext);
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

  private ImageChannelDataType determineType(NativeTypeEnum dataType)
  {
    if (dataType == NativeTypeEnum.Byte)
    {
      return ImageChannelDataType.UnsignedInt8;
    }
    if (dataType == NativeTypeEnum.UnsignedShort)
    {
      return ImageChannelDataType.UnsignedInt16;
    }
    if (dataType == NativeTypeEnum.Float)
    {
      return ImageChannelDataType.Float;
    }
    // todo: complete conversion list

    return null;
  }

  private ClearCLProgram initializeProgram(Class pAnchorClass,
                                           String pProgramFilename) throws
                                                                    IOException
  {
    return mClearCLContext.createProgram(pAnchorClass,
                                         pProgramFilename);
  }

  public void dispose()
  {
    mClearCLContext.close();
    if (sInstance == this)
    {
      sInstance = null;
    }
  }

  public ClearCLContext getClearCLContext()
  {
    return mClearCLContext;
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

}
