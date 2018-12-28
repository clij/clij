package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class DilateSphereTest {

    @Test
    public void dilate3d() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2 = NewImage.createImage("", 100, 100, 12, 16, NewImage.FILL_BLACK);

        for (int z = 0; z < 5; z++) {
            testImp2.setZ(z + 1);
            ImageProcessor ip2 = testImp2.getProcessor();
            ip2.set(7, 5, 1);
            ip2.set(6, 6, 1);
            ip2.set(5, 7, 1);
        }
        ClearCLImage maskCL = clij.convert(testImp2, ClearCLImage.class);
        ClearCLImage maskCLafter = clij.createCLImage(maskCL);

        Kernels.dilateSphere(clij, maskCL, maskCLafter);

        double sum = Kernels.sumPixels(clij, maskCLafter);

        assertEquals(58, sum, 0);

        maskCL.close();
        maskCLafter.close();
    }

    @Test
    public void dilate3d_Buffers() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2 = NewImage.createImage("", 100, 100, 12, 16, NewImage.FILL_BLACK);

        for (int z = 0; z < 5; z++) {
            testImp2.setZ(z + 1);
            ImageProcessor ip2 = testImp2.getProcessor();
            ip2.set(7, 5, 1);
            ip2.set(6, 6, 1);
            ip2.set(5, 7, 1);
        }

        ClearCLBuffer maskCL = clij.convert(testImp2, ClearCLBuffer.class);
        ClearCLBuffer maskCLafter = clij.createCLBuffer(maskCL);

        Kernels.dilateSphere(clij, maskCL, maskCLafter);

        double sum = Kernels.sumPixels(clij, maskCLafter);

        assertEquals(58, sum, 0);

        maskCL.close();
        maskCLafter.close();
    }

    @Test
    public void dilate2d() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2 = NewImage.createImage("", 100, 100, 12, 16, NewImage.FILL_BLACK);

        ImageProcessor ip2 = testImp2.getProcessor();
        ip2.set(7, 5, 1);
        ip2.set(6, 6, 1);
        ip2.set(5, 7, 1);

        ClearCLImage maskCL = clij.convert(testImp2, ClearCLImage.class);;
        ClearCLImage
                maskCLafter = clij.createCLImage(maskCL);

        Kernels.dilateSphere(clij, maskCL, maskCLafter);

        double sum = Kernels.sumPixels(clij, maskCLafter);

        assertEquals(14, sum, 0);

        maskCL.close();
        maskCLafter.close();
    }

    @Test
    public void dilate2d_Buffers() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2 = NewImage.createImage("", 100, 100, 12, 16, NewImage.FILL_BLACK);

        ImageProcessor ip2 = testImp2.getProcessor();
        ip2.set(7, 5, 1);
        ip2.set(6, 6, 1);
        ip2.set(5, 7, 1);

        ClearCLBuffer maskCL = clij.convert(testImp2, ClearCLBuffer.class);;
        ClearCLBuffer maskCLafter = clij.createCLBuffer(maskCL);

        Kernels.dilateSphere(clij, maskCL, maskCLafter);

        double sum = Kernels.sumPixels(clij, maskCLafter);

        assertEquals(14, sum, 0);

        maskCL.close();
        maskCLafter.close();
    }

}