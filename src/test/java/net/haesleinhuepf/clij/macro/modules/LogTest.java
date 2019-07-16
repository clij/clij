package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class LogTest {
    private final static double tolerance = 0.01;

    @Test
    public void log() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        IJ.run(testImp2D1, "Abs", "");

        // do operation with ImageJ
        ImagePlus logIJ = testImp2D1.duplicate();
        IJ.run(logIJ, "Log", "");
        
        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.log(clij, src, dst);

        ImagePlus logCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(logIJ, logCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clij.close();

    }

    @Test
    public void log_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        IJ.run(testImp2D1, "Abs", "");

        // do operation with ImageJ
        ImagePlus logIJ = testImp2D1.duplicate();
        IJ.run(logIJ, "Log", "");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.log(clij, src, dst);

        ImagePlus logCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(logIJ, logCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clij.close();
    }

}