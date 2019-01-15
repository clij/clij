package net.haesleinhuepf.clij.utilities;

import net.haesleinhuepf.clij.clearcl.*;
import net.haesleinhuepf.clij.clearcl.backend.ClearCLBackendInterface;
import net.haesleinhuepf.clij.clearcl.backend.ClearCLBackends;
import net.haesleinhuepf.clij.clearcl.enums.*;

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

            for (ClearCLBackendInterface backend : ClearCLBackends.getBackendList()) {
                output.append("  * " + backend + "\n");
            }

            output.append("    Functional backend:" + ClearCLBackends.getFunctionalBackend() + "\n");
            output.append("    Best backend:" + ClearCLBackends.getBestBackend() + "\n");

            ClearCLBackendInterface clearCLBackend = ClearCLBackends.getBestBackend();
            //new ClearCLBackendJavaCL();

            output.append("Used CL backend: " + clearCLBackend + "\n");

            ClearCL clearCL = new ClearCL(clearCLBackend);

            output.append("ClearCL: " + clearCL + "\n");
            output.append("  Number of platforms:" + clearCL.getNumberOfPlatforms() + "\n");
            for (int p = 0; p < clearCL.getNumberOfPlatforms(); p++) {
                ClearCLPlatform clearCLPlatform = clearCL.getPlatform(p);
                output.append("  [" + p + "] " + clearCLPlatform.getName() + "\n");
                output.append("     Number of devices: " + clearCLPlatform.getNumberOfDevices() + "\n");

                output.append("     Available devices: \n");
                for (int d = 0; d < clearCLPlatform.getNumberOfDevices(); d++) {
                    ClearCLDevice device = clearCLPlatform.getDevice(d);
                    output.append("     [" + d + "] " + device.getName() + " \n");
                    output.append("        NumberOfComputeUnits: "
                            + device.getNumberOfComputeUnits()
                            + " \n");
                    output.append("        Clock frequency: "
                            + device.getClockFrequency()
                            + " \n");
                    output.append("        Version: " + device.getVersion() + " \n");
                    output.append("        Extensions: " + device.getExtensions() + " \n");
                    output.append("        GlobalMemorySizeInBytes: "
                            + device.getGlobalMemorySizeInBytes()
                            + " \n");
                    output.append("        LocalMemorySizeInBytes: "
                            + device.getLocalMemorySizeInBytes()
                            + " \n");
                    output.append("        MaxMemoryAllocationSizeInBytes: "
                            + device.getMaxMemoryAllocationSizeInBytes()
                            + " \n");
                    output.append("        MaxWorkGroupSize: "
                            + device.getMaxWorkGroupSize()
                            + " \n");

                    ClearCLContext context = device.createContext();
                    ArrayList<ImageChannelDataType> listAvailableTypes = listSupportedTypes(context);
                    ImageChannelDataType[] arrayTypes = new ImageChannelDataType[listAvailableTypes.size()];
                    listAvailableTypes.toArray(arrayTypes);
                    output.append("        Compatible image types: " + Arrays.toString(arrayTypes) + "\n");
                }
            }

            output.append("Best GPU device for images: " + clearCL.getFastestGPUDeviceForImages().getName() + "\n");
            output.append("Best largest GPU device: " + clearCL.getLargestGPUDevice().getName() + "\n");
            output.append("Best CPU device: " + clearCL.getBestCPUDevice().getName() + "\n");
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

        for (ImageChannelDataType type : ImageChannelDataType.values()) {
            try {
                        lContext.createImage(
                                MemAllocMode.Best,
                                HostAccessType.ReadWrite,
                                KernelAccessType.ReadWrite,
                                ImageChannelOrder.R,
                                type,
                                new long[]{16, 16, 16});

            } catch (Exception e) {
                System.out.println("Type " + type + " didn't work because " + e.toString());
                continue;
            }
            lTypeNameList.add(type);
        }
        return lTypeNameList;
    }
}
