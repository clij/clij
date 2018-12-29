package net.haesleinhuepf.clij.utilities;

import clearcl.*;
import clearcl.backend.ClearCLBackendInterface;
import clearcl.backend.ClearCLBackends;
import clearcl.enums.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * CLInfo is thought to help depugging OpenCL devices. E.g. it lists
 * the available devices and supported pixel types.
 * <p>
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class CLInfo {
    /**
     * Information on available OpenCL devices
     *
     * @return String containing  list of devices and information known
     * about them.
     */
    public static String clinfo() {

        StringBuffer output = new StringBuffer();

        try {
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
            for (int p = 0; p < lClearCL.getNumberOfPlatforms(); p++) {
                ClearCLPlatform lClearCLPlatform = lClearCL.getPlatform(p);
                output.append("  [" + p + "] " + lClearCLPlatform.getName() + "\n");
                output.append("     Number of devices: " + lClearCLPlatform.getNumberOfDevices() + "\n");

                output.append("     Available devices: \n");
                for (int d = 0; d < lClearCLPlatform.getNumberOfDevices(); d++) {
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
        } catch (Exception e) {
            output.append("\n\nException: " + e.toString());
            return output.toString();
        }
        return output.toString();
    }

    /**
     * Checks for a given context, if all image pixel types are
     * supported.
     *
     * @param lContext OpenCL context to use to create some small test
     *                 images
     * @return the list of supported types
     */
    public static ArrayList<ImageChannelDataType> listSupportedTypes(ClearCLContext lContext) {
        ArrayList<ImageChannelDataType> lTypeNameList = new ArrayList<ImageChannelDataType>();

        for (ImageChannelDataType lType : ImageChannelDataType.values()) {
            try {
                ClearCLImage
                        lImage =
                        lContext.createImage(
                                MemAllocMode.Best,
                                HostAccessType.ReadWrite,
                                KernelAccessType.ReadWrite,
                                ImageChannelOrder.R,
                                lType,
                                new long[]{16, 16, 16});

            } catch (Exception e) {
                System.out.println("Type " + lType + " didn't work because " + e.toString());
                continue;
            }
            lTypeNameList.add(lType);
        }
        return lTypeNameList;
    }
}
