package clearcl.imagej;

import clearcl.*;
import clearcl.backend.ClearCLBackendBase;
import clearcl.backend.ClearCLBackendInterface;
import clearcl.backend.ClearCLBackends;
import clearcl.backend.javacl.ClearCLBackendJavaCL;
import clearcl.enums.*;
import clearcl.imagej.utilities.CLInfo;
import clearcl.imagej.utilities.GenericBinaryFastFuseTask;
import clearcl.imagej.utilities.ImageTypeConverter;
import clearcl.util.ElapsedTime;
import clearcontrol.core.variable.Variable;
import clearcontrol.stack.OffHeapPlanarStack;
import clearcontrol.stack.StackInterface;
import coremem.enums.NativeTypeEnum;
import fastfuse.FastFusionEngine;
import fastfuse.FastFusionMemoryPool;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.RealType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ClearCLIJ is an entry point for ImageJ/OpenCL compatibility.
 * Simply create an instance using the SingleTon implementation:
 *
 * clij = ClearCLIJ.getInstance();
 *
 * Alternatively, you can get an instance associated to a particular
 * OpenCL device by handing over its name to the constructor
 *
 * clji = new ClearCLIJ("geforce");
 *
 * To get a list of available devices, call
 * ClearCLIJ.getAvailableDevices()  to learn more about these devices,
 * call ClearCLIJ.clinfo();
 *
 *
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
  private GenericBinaryFastFuseTask
            mGenericUnaryFastFuseTask = null;


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
        lClearCLBackend = ClearCLBackends.getBestBackend();
        //new ClearCLBackendJavaCL();

    mClearCL = new ClearCL(lClearCLBackend);
    if (pDeviceNameMustContain == null)
    {
      mClearCLDevice = mClearCL.getFastestGPUDeviceForImages();
    } else {
      mClearCLDevice = mClearCL.getDeviceByName(pDeviceNameMustContain);
    }

    if (mClearCLDevice == null) {
      System.out.println("Warning: Optimal ClearCL device determination failed. Retrying using first device found.");
      mClearCLDevice = mClearCL.getAllDevices().get(0);
    }
    System.out.println("Using OpenCL device: " + mClearCLDevice.getName());


    mClearCLContext = mClearCLDevice.createContext();

    mFastFusionEngine = new FastFusionEngine(mClearCLContext);

    FastFusionMemoryPool.getInstance(mClearCLContext);
  }

  public boolean execute(String pProgramFilename,
                         String pKernelname,
                         Map<String, Object> pParameterMap)
  {
    return execute(Object.class, pProgramFilename, pKernelname, pParameterMap);
  }

  public boolean execute(Class pAnchorClass,
                         String pProgramFilename,
                         String pKernelname,
                         Map<String, Object> pParameterMap)
  {
    return execute(pAnchorClass, pProgramFilename, pKernelname, null, pParameterMap);
  }

  public boolean execute(Class pAnchorClass,
                         String pProgramFilename,
                         String pKernelname,
                         long[] pGlobalsizes,
                         Map<String, Object> pParameterMap)
  {
    final Variable<Boolean> result = new Variable<Boolean>("", true);

    ElapsedTime.measure("kernel + build " + pKernelname, () -> {
        if (mGenericUnaryFastFuseTask == null) {
            try {
                mGenericUnaryFastFuseTask = new GenericBinaryFastFuseTask(mFastFusionEngine,
                        pAnchorClass,
                        pProgramFilename,
                        pKernelname,
                        pGlobalsizes);
            } catch (IOException e) {
                e.printStackTrace();
                result.set(false);
                return;
            }
        } else {
            mGenericUnaryFastFuseTask.setProgramFilename(pProgramFilename);
            mGenericUnaryFastFuseTask.setKernelName(pKernelname);
            mGenericUnaryFastFuseTask.setAnchorClass(pAnchorClass);
            mGenericUnaryFastFuseTask.setParameterMap(pParameterMap);
            mGenericUnaryFastFuseTask.setGlobalSizes(pGlobalsizes);
        }
        mGenericUnaryFastFuseTask.setParameterMap(pParameterMap);
        result.set(mGenericUnaryFastFuseTask.enqueue(null, true));
    });
    return result.get();
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

  public ImageTypeConverter converter(StackInterface pStack) {
    return new ImageTypeConverter(this, pStack);
  }

  public <T extends RealType<T>> ImageTypeConverter<T> converter(RandomAccessibleInterval<T> pRandomAccessibleInterval) {
    return new ImageTypeConverter<T>(this, pRandomAccessibleInterval);
  }

  public ImageTypeConverter converter(ImagePlus pImagePlus) {
    return new ImageTypeConverter(this, pImagePlus);
  }

  public <T extends RealType<T>> ImageTypeConverter<T> converter(ClearCLImage pClearCLImage) {
    return new ImageTypeConverter<T>(this, pClearCLImage);
  }

  public <T extends RealType<T>> ImageTypeConverter<T> converter(ClearCLBuffer pClearCLBuffer) {
    return new ImageTypeConverter<T>(this, pClearCLBuffer);
  }

  public static String clinfo() {
    return CLInfo.clinfo();
  }

  public static ArrayList<String> getAvailableDeviceNames() {
    ArrayList<String> lResultList = new ArrayList<>();

    ClearCLBackendInterface lClearCLBackend = ClearCLBackends.getBestBackend();
    ClearCL lClearCL = new ClearCL(lClearCLBackend);
    for (ClearCLDevice lDevice : lClearCL.getAllDevices()) {
      lResultList.add(lDevice.getName());
    }
    return lResultList;
  }

  public ClearCLImage createCLImage(ClearCLImage pInputImage) {
    return mClearCLContext.createImage(pInputImage);
  }

  public ClearCLImage createCLImage(long[] dimensions, ImageChannelDataType pImageChannelType) {

    return mClearCLContext.createImage(HostAccessType.ReadWrite,
            KernelAccessType.ReadWrite,
            ImageChannelOrder.R,
            pImageChannelType,
            dimensions);
  }

  public ClearCLBuffer createCLBuffer(long[] dimensions, NativeTypeEnum pNativeType) {
    return mClearCLContext.createBuffer(
            MemAllocMode.Best,
            HostAccessType.ReadWrite,
            KernelAccessType.ReadWrite,
            1L,
            pNativeType,
            dimensions
            );
  }

  public void show(ImagePlus input, String title) {
    show(converter(input), title);
  }
  public void show(RandomAccessibleInterval input, String title) {
    show(converter(input), title);
  }
  public void show(ClearCLImage input, String title) {
    show(converter(input), title);
  }
  public void show(ClearCLBuffer input, String title) {
    show(converter(input), title);
  }
  public void show(StackInterface input, String title) {
    show(converter(input), title);
  }
  public void show(ImageTypeConverter input, String title) {
    ImagePlus imp = new Duplicator().run(input.getImagePlus());
    imp.setTitle(title);
    imp.setZ(imp.getNSlices() / 2);
    imp.setC(imp.getNChannels() / 2);
    IJ.run(imp,"Enhance Contrast", "saturated=0.35");
    if (imp.getNChannels() > 1 && imp.getNSlices() == 1) {
        IJ.run(imp, "Properties...", "channels=1 slices=" + imp.getNChannels() + " frames=1 unit=pixel pixel_width=1.0000 pixel_height=1.0000 voxel_depth=1.0000");
    }
    imp.show();
  }

  public boolean close()
  {
    mClearCLContext.close();
    mClearCLContext.getDevice().close();
    return true;
  }
}
