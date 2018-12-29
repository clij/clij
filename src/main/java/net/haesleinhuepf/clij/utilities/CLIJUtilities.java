package net.haesleinhuepf.clij.utilities;

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
        int n = (int)(sigma * 4.0);
        if (n % 2 == 0) {
            n++;
        }
        return n;
    }

    public static String classToName(Class aClass) {
        String name = aClass.getSimpleName();
        return "CLIJ_" + name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
    }
}
