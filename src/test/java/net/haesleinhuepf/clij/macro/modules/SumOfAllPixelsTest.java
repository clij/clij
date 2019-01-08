package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.junit.Test;

import static org.junit.Assert.*;

public class SumOfAllPixelsTest {
    private static double relativeTolerance = 0.001;

    @Test
    public void sumPixelsSliceBySlice() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ClearCLImage maskCL = clij.convert(mask3d, ClearCLImage.class);

        double sum = Kernels.sumPixels(clij, maskCL);
        double[] sumSliceWise = Kernels.sumPixelsSliceBySlice(clij, maskCL);

        assertEquals(1.0, sum / new Sum().evaluate(sumSliceWise), relativeTolerance);

        maskCL.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void sumPixelsSliceBySlice_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ClearCLBuffer maskCL = clij.convert(mask3d, ClearCLBuffer.class);

        double sum = Kernels.sumPixels(clij, maskCL);
        double[] sumSliceWise = Kernels.sumPixelsSliceBySlice(clij, maskCL);

        assertEquals(1.0, sum / new Sum().evaluate(sumSliceWise), relativeTolerance);

        maskCL.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void sumPixels3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = NewImage.createByteImage("Test", 10, 10, 5, NewImage.FILL_BLACK);
        mask3d.setZ(2);
        ImageProcessor ip = mask3d.getProcessor();
        ip.set(3,3, 1);

        ClearCLImage maskCL = clij.convert(mask3d, ClearCLImage.class);
        double sum = Kernels.sumPixels(clij, maskCL);
        assertEquals(1, sum, 0.0);

        ClearCLImage maskCL2 = clij.createCLImage(maskCL);
        Kernels.dilateBox(clij, maskCL, maskCL2);
        double sum2 = Kernels.sumPixels(clij, maskCL2);
        assertEquals(27, sum2, 0.0);

        ClearCLImage maskCL3 = clij.createCLImage(maskCL);
        Kernels.dilateSphere(clij, maskCL, maskCL3);
        double sum3 = Kernels.sumPixels(clij, maskCL3);
        assertEquals(7, sum3, 0.0);


        maskCL.close();
        maskCL2.close();
        maskCL3.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void sumPixels3d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = NewImage.createByteImage("Test", 10, 10, 5, NewImage.FILL_BLACK);
        mask3d.setZ(2);
        ImageProcessor ip = mask3d.getProcessor();
        ip.set(3,3, 1);

        ClearCLBuffer maskCL = clij.convert(mask3d, ClearCLBuffer.class);
        double sum = Kernels.sumPixels(clij, maskCL);
        assertEquals(1, sum, 0.0);

        ClearCLBuffer maskCL2 = clij.createCLBuffer(maskCL);
        Kernels.dilateBox(clij, maskCL, maskCL2);
        double sum2 = Kernels.sumPixels(clij, maskCL2);
        assertEquals(27, sum2, 0.0);

        ClearCLBuffer maskCL3 = clij.createCLBuffer(maskCL);
        Kernels.dilateSphere(clij, maskCL, maskCL3);
        double sum3 = Kernels.sumPixels(clij, maskCL3);
        assertEquals(7, sum3, 0.0);


        maskCL.close();
        maskCL2.close();
        maskCL3.close();
        IJ.exit();
        clij.close();
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
        IJ.exit();
        clij.close();
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
        IJ.exit();
        clij.close();
    }

}