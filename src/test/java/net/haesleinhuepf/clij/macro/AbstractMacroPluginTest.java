package net.haesleinhuepf.clij.macro;

import clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;

/**
 * AbstractMacroPluginTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
public class AbstractMacroPluginTest {
    protected boolean clBuffersEqual(CLIJ clij, ClearCLBuffer buffer1, ClearCLBuffer buffer2) {
        clBuffersEqual(clij, buffer1, buffer2, 0);
        return true;
    }

    protected boolean clBuffersEqual(CLIJ clij, ClearCLBuffer buffer1, ClearCLBuffer buffer2, double tolerance) {
        if (buffer1.getWidth() != buffer2.getWidth() ||
                buffer1.getHeight() != buffer2.getHeight() ||
                buffer1.getDepth() != buffer2.getDepth()
        ) {
            System.out.println("Sizes different");
            return false;
        }

        //if (Kernels.sumPixels(clij, buffer1) != Kernels.sumPixels(clij, buffer2)) {
        //    System.out.println("Sums different");
        //    return false;
        //}

        ClearCLBuffer diffBuffer = clij.createCLBuffer(buffer1);
        Kernels.addImagesWeighted(clij, buffer1, buffer2, diffBuffer, 1f, -1f);

        double maxDifference = Kernels.maximumOfAllPixels(clij, diffBuffer);
        double minDifference = Kernels.minimumOfAllPixels(clij, diffBuffer);
        diffBuffer.close();

        if (Math.abs(maxDifference) > tolerance || Math.abs(minDifference) > tolerance ) {
            System.out.println("Difference unequal to zero!");
            return false;
        }


        return true;
    }
}
