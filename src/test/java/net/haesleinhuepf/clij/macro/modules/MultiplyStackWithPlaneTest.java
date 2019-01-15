package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiplyStackWithPlaneTest {
    @Test
    public void multiplyStackWithPlane() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage mask = clij.convert(testImp2, ClearCLImage.class);;
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.multiplyStackWithPlane(clij, src, mask, dst);

        mask.close();
        dst.close();

        IJ.exit();
        clij.close();
    }

    @Test
    public void multiplyStackWithPlane_Buffers() {

        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer mask = clij.convert(testImp2, ClearCLBuffer.class);;
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.multiplyStackWithPlane(clij, src, mask, dst);

        mask.close();
        dst.close();

        IJ.exit();
        clij.close();
    }

}