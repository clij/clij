package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImagePlus;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.test.TestUtilities;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.ByteType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ErodeSphereTest {

    @Test
    public void erode3d() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus mask3d = ImageJFunctions.wrap(ArrayImgs.bytes(new byte[]{

                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0
        }, new long[]{5, 5, 5}), "");


        ClearCLImage maskCL = clij.convert(mask3d, ClearCLImage.class);
        ClearCLImage
                maskCLafter = clij.createCLImage(maskCL);

        Kernels.erodeSphere(clij, maskCL, maskCLafter);

        double sum = Kernels.sumPixels(clij, maskCLafter);

        assertTrue(sum == 1);

        maskCL.close();
        maskCLafter.close();
    }

    @Test
    public void erode3d_Buffer() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus mask3d = ImageJFunctions.wrap(ArrayImgs.bytes(new byte[]{

                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0,

                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0
        }, new long[]{5, 5, 5}), "");

        ClearCLBuffer maskCL = clij.convert(mask3d, ClearCLBuffer.class);
        ClearCLBuffer
                maskCLafter = clij.createCLBuffer(maskCL);

        Kernels.erodeSphere(clij, maskCL, maskCLafter);

        double sum = Kernels.sumPixels(clij, maskCLafter);

        assertTrue(sum == 1);

        maskCL.close();
        maskCLafter.close();
    }

    @Test
    public void erode2d() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2 = ImageJFunctions.wrap(ArrayImgs.bytes(new byte[]{
                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0
        }, new long[]{5, 5, 1}), "");

        ClearCLImage maskCL = clij.convert(testImp2, ClearCLImage.class);;
        ClearCLImage
                maskCLafter = clij.createCLImage(maskCL);

        Kernels.erodeSphere(clij, maskCL, maskCLafter);

        double sum = Kernels.sumPixels(clij, maskCLafter);

        assertTrue(sum == 1);

        maskCL.close();
        maskCLafter.close();
    }

    @Test
    public void erode2d_Buffers() {

        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2 = ImageJFunctions.wrap(ArrayImgs.bytes(new byte[]{
                0, 0, 0, 0, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 1, 1, 1, 0,
                0, 0, 0, 0, 0
        }, new long[]{5, 5, 1}), "");

        ClearCLBuffer maskCL = clij.convert(testImp2, ClearCLBuffer.class);;
        ClearCLBuffer
                maskCLafter = clij.createCLBuffer(maskCL);

        Kernels.erodeSphere(clij, maskCL, maskCLafter);

        double sum = Kernels.sumPixels(clij, maskCLafter);

        assertTrue(sum == 1);

        maskCL.close();
        maskCLafter.close();
    }


}