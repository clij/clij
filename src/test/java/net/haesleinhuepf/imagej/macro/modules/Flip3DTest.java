package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImagePlus;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Flip3DTest {
    @Test
    public void flip3d() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        ClearCLImage testCL = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage flip = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage flop = clij.convert(testImp1, ClearCLImage.class);

        Kernels.flip(clij, testCL, flip, true, false, false);

        ImagePlus testFlipped = clij.convert(flip, ImagePlus.class);

        Kernels.flip(clij, flip, flop, true, false, false);
        ImagePlus testFlippedTwice = clij.convert(flop, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(testImp1, testFlippedTwice));
        assertFalse(TestUtilities.compareImages(testImp1, testFlipped));

        testCL.close();
        flip.close();
        flop.close();
    }


    @Test
    public void flipBuffer3d() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        ClearCLBuffer testCL = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer flip = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer flop = clij.convert(testImp1, ClearCLBuffer.class);

        Kernels.flip(clij, testCL, flip, true, false, false);

        ImagePlus testFlipped = clij.convert(flip, ImagePlus.class);

        Kernels.flip(clij, flip, flop, true, false, false);
        ImagePlus testFlippedTwice = clij.convert(flop, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(testImp1, testFlippedTwice));
        assertFalse(TestUtilities.compareImages(testImp1, testFlipped));

        testCL.close();
        flip.close();
        flop.close();
    }

}