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

public class SetTest {

    @Test
    public void set3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        ClearCLImage imageCL = clij.convert(mask3d, ClearCLImage.class);

        Kernels.set(clij, imageCL, 2f);

        double sum = Kernels.sumPixels(clij, imageCL);

        assertTrue(sum
                == imageCL.getWidth()
                * imageCL.getHeight()
                * imageCL.getDepth()
                * 2);

        imageCL.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void set3d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        ClearCLBuffer imageCL = clij.convert(mask3d, ClearCLBuffer.class);

        Kernels.set(clij, imageCL, 2f);

        double sum = Kernels.sumPixels(clij, imageCL);

        assertTrue(sum
                == imageCL.getWidth()
                * imageCL.getHeight()
                * imageCL.getDepth()
                * 2);

        imageCL.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void set2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        ClearCLImage imageCL = clij.convert(testImp2, ClearCLImage.class);;

        Kernels.set(clij, imageCL, 2f);

        double sum = Kernels.sumPixels(clij, imageCL);

        assertTrue(sum == imageCL.getWidth() * imageCL.getHeight() * 2);

        imageCL.close();
        IJ.exit();
        clij.close();
    }


    @Test
    public void set2d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        ClearCLBuffer imageCL = clij.convert(testImp2, ClearCLBuffer.class);;

        Kernels.set(clij, imageCL, 2f);

        double sum = Kernels.sumPixels(clij, imageCL);

        assertTrue(sum == imageCL.getWidth() * imageCL.getHeight() * 2);

        imageCL.close();
        IJ.exit();
        clij.close();
    }

}