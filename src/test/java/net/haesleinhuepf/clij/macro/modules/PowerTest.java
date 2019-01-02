package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class PowerTest {
    private final static double tolerance = 0.01;
    
    @Test
    public void power() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                squared =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D1);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.power(clij, src, dst, 2.0f);

        ImagePlus squaredCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(squared, squaredCL, tolerance));

        src.close();
        dst.close();


    }

    @Test
    public void power_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                squared =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D1);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.power(clij, src, dst, 2.0f);

        ImagePlus squaredCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(squared, squaredCL, tolerance));

        src.close();
        dst.close();


    }

}