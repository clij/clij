package clearcl.imagej.utilities;

import clearcl.*;
import clearcl.backend.ClearCLBackendInterface;
import clearcl.backend.ClearCLBackends;
import clearcl.enums.HostAccessType;
import clearcl.enums.ImageChannelDataType;
import clearcl.enums.KernelAccessType;
import coremem.enums.NativeTypeEnum;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class CLInfo
{
  public static String clinfo()
  {

    StringBuffer output = new StringBuffer();

    try
    {
      output.append("Available CL backends:\n");

      for (ClearCLBackendInterface lBackend : ClearCLBackends.getBackendList()) {
        output.append("  * " + lBackend + "\n");
      }

      output.append("    Functional backend:" + ClearCLBackends.getFunctionalBackend() + "\n");
      output.append("    Best backend:" + ClearCLBackends.getBestBackend() + "\n");

      ClearCLBackendInterface lClearCLBackend = ClearCLBackends.getBestBackend();
      //new ClearCLBackendJavaCL();

      output.append("Used CL backend: " + lClearCLBackend + "\n");

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

          ClearCLContext lContext = lDevice.createContext();
          ArrayList<ImageChannelDataType> lListAvailableTypes = listSupportedTypes(lContext);
          ImageChannelDataType[] lArrayTypes = new ImageChannelDataType[lListAvailableTypes.size()];
          lListAvailableTypes.toArray(lArrayTypes);
          output.append("        Compatible image types: " + Arrays.toString(lArrayTypes) + "\n");
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

  public static ArrayList<ImageChannelDataType> listSupportedTypes(ClearCLContext lContext)
  {
    ArrayList<ImageChannelDataType> lTypeNameList = new ArrayList<ImageChannelDataType>();

    for (ImageChannelDataType lType : ImageChannelDataType.values())
    {
      try
      {
        ClearCLImage
            lImage =
            lContext.createSingleChannelImage(HostAccessType.ReadWrite,
                                              KernelAccessType.ReadWrite,
                                              lType,
                                              new long[] { 2, 3, 4 });
      } catch (Exception e) {
        continue;
      }
      lTypeNameList.add(lType);
    }
    return lTypeNameList;
  }
}
