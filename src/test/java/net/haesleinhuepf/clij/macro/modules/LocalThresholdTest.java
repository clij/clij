package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLImage;
import clearcl.util.ElapsedTime;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocalThresholdTest {
    //@Ignore // because it makes the JVM crash
    @Test
    public void localThreshold3D() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = NewImage.createImage("", 100, 100, 12, 16, NewImage.FILL_BLACK);
        for (int z = 0; z < 5; z++) {
            testImp1.setZ(z + 1);
            ImageProcessor ip1 = testImp1.getProcessor();
            ip1.set(5, 5, 1);
            ip1.set(6, 6, 1);
            ip1.set(7, 7, 1);
        }

        ClearCLImage input = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage output1 = clij.createCLImage(input);
        ClearCLImage output2 = clij.createCLImage(input);
        ClearCLImage temp = clij.createCLImage(input);
        ClearCLImage blurred = clij.createCLImage(input);

        Kernels.blurFast(clij, input, blurred, 2, 2, 2);

        // usual way: blur, subtract, threshold
        ElapsedTime.measureForceOutput("traditional thresholding", () -> {
            Kernels.addImagesWeighted(clij, input, blurred, temp, 1f, -1f);
            Kernels.threshold(clij, temp, output1, 0f);
        });

        // short cut: blur, local threshold
        ElapsedTime.measureForceOutput("local threshold", () -> {
            Kernels.localThreshold(clij, input, output2, blurred);
        });

        System.out.println("O1: " + Kernels.sumPixels(clij, output1));
        System.out.println("O2: " + Kernels.sumPixels(clij, output2));

        assertTrue(Kernels.sumPixels(clij, output1) > 0);
        assertTrue(Kernels.sumPixels(clij, output1) == Kernels.sumPixels(clij, output2));

        Kernels.addImagesWeighted(clij, output1, output2, temp, 1f, -1f);

        assertTrue(Kernels.sumPixels(clij, temp) == 0);

        input.close();
        output1.close();
        output2.close();
        temp.close();
        blurred.close();
    }
}