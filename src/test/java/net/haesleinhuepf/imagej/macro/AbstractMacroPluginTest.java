package net.haesleinhuepf.imagej.macro;

import clearcl.ClearCLBuffer;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import org.jruby.RubyProcess;
import org.renjin.gnur.api.S;

/**
 * AbstractMacroPluginTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
public class AbstractMacroPluginTest {
    protected boolean clBuffersEqual(ClearCLIJ clij, ClearCLBuffer buffer1, ClearCLBuffer buffer2) {
        if (buffer1.getWidth() != buffer2.getWidth() ||
            buffer1.getHeight() != buffer2.getHeight() ||
            buffer1.getDepth() != buffer2.getDepth()
        ) {
            System.out.println("Sizes different");
            return false;
        }

        if (Kernels.sumPixels(clij, buffer1) != Kernels.sumPixels(clij, buffer2)) {
            System.out.println("Sums different");
            return false;
        }

        ClearCLBuffer diffBuffer = clij.createCLBuffer(buffer1);
        Kernels.addWeightedPixelwise(clij, buffer1, buffer2, diffBuffer, 1f, -1f);

        double difference = Kernels.sumPixels(clij, diffBuffer);
        diffBuffer.close();

        if (Math.abs(difference) > 0) {
            System.out.println("Difference unequal to zero!");
            return false;
        }


        return true;
    }
}
