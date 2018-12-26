package net.haesleinhuepf.imagej.utilities;

/**
 * CLIJUtilities
 * <p>
 * Author: @haesleinhuepf
 * December 2018
 */
public class CLIJUtilities {
    public static int radiusToKernelSize(int radius) {
        int kernelSize = radius * 2 + 1;
        return kernelSize;
    }

    public static int sigmaToKernelSize(float sigma) {
        int n = (int)(sigma * 4.5);
        if (n % 2 == 0) {
            n++;
        }
        return n;
    }

}
