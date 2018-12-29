package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.Duplicator;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.junit.Test;

import static org.junit.Assert.*;

public class SumOfAllPixelsTest {

    @Test
    public void sumPixelsSliceBySlice() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ClearCLImage maskCL = clij.convert(mask3d, ClearCLImage.class);

        double sum = Kernels.sumPixels(clij, maskCL);
        double[] sumSliceWise = Kernels.sumPixelsSliceBySlice(clij, maskCL);

        assertTrue(sum == new Sum().evaluate(sumSliceWise));

        maskCL.close();
    }

    @Test
    public void sumPixelsSliceBySlice_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ClearCLBuffer maskCL = clij.convert(mask3d, ClearCLBuffer.class);

        double sum = Kernels.sumPixels(clij, maskCL);
        double[] sumSliceWise = Kernels.sumPixelsSliceBySlice(clij, maskCL);

        assertTrue(sum == new Sum().evaluate(sumSliceWise));

        maskCL.close();
    }

    @Test
    public void sumPixels3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ClearCLImage maskCL = clij.convert(mask3d, ClearCLImage.class);

        double sum = Kernels.sumPixels(clij, maskCL);

        assertTrue(sum == 27);

        maskCL.close();
    }

    @Test
    public void sumPixels3d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ClearCLBuffer maskCL = clij.convert(mask3d, ClearCLBuffer.class);

        double sum = Kernels.sumPixels(clij, maskCL);

        assertTrue(sum == 27);

        maskCL.close();
    }

    @Test
    public void sumPixels2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus imp = NewImage.createByteImage("test", 10, 10, 1, NewImage.FILL_BLACK);
        imp.getProcessor().set(5,5, 1);
        imp.getProcessor().set(5,6, 2);
        imp.getProcessor().set(5,7, 3);

        ClearCLImage maskCL = clij.convert(imp, ClearCLImage.class);

        double sum = Kernels.sumPixels(clij, maskCL);

        assertTrue(sum == 6);

        maskCL.close();
    }

    @Test
    public void sumPixels2d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus imp = NewImage.createByteImage("test", 10, 10, 1, NewImage.FILL_BLACK);
        imp.getProcessor().set(5,5, 1);
        imp.getProcessor().set(5,6, 2);
        imp.getProcessor().set(5,7, 3);

        ClearCLBuffer maskCL = clij.convert(imp, ClearCLBuffer.class);

        double sum = Kernels.sumPixels(clij, maskCL);

        assertTrue(sum == 6);

        maskCL.close();
    }

}