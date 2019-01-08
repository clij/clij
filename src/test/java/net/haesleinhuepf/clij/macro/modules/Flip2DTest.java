package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Flip2DTest {

    @Test
    public void flip2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        ClearCLImage testCL = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage flip = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage flop = clij.convert(testImp2D1, ClearCLImage.class);

        Kernels.flip(clij, testCL, flip, true, false);

        ImagePlus testFlipped = clij.convert(flip, ImagePlus.class);

        Kernels.flip(clij, flip, flop, true, false);
        ImagePlus testFlippedTwice = clij.convert(flop, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(testImp2D1, testFlippedTwice));
        assertFalse(TestUtilities.compareImages(testImp2D1, testFlipped));

        testCL.close();
        flip.close();
        flop.close();
        IJ.exit();
        clij.close();
    }


    @Test
    public void flipBuffer2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        ClearCLBuffer testCL = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer flip = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer flop = clij.convert(testImp2D1, ClearCLBuffer.class);

        Kernels.flip(clij, testCL, flip, true, false);

        ImagePlus testFlipped = clij.convert(flip, ImagePlus.class);

        Kernels.flip(clij, flip, flop, true, false);
        ImagePlus testFlippedTwice = clij.convert(flop, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(testImp2D1, testFlippedTwice));
        assertFalse(TestUtilities.compareImages(testImp2D1, testFlipped));

        testCL.close();
        flip.close();
        flop.close();
        IJ.exit();
        clij.close();
    }

}