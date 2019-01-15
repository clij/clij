package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.junit.Test;

import static org.junit.Assert.*;

public class ErodeSphereTest {

    @Test
    public void erode3d() {
        CLIJ clij = CLIJ.getInstance();
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
        IJ.exit();
        clij.close();
    }

    @Test
    public void erode3d_Buffer() {
        CLIJ clij = CLIJ.getInstance();
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
        //new ImageJ();
        //clij.show(maskCLafter,"mask");
        //new WaitForUserDialog("wait").show();

        double sum = Kernels.sumPixels(clij, maskCLafter);

        assertTrue(sum == 1);

        maskCL.close();
        maskCLafter.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void erode2d() {
        CLIJ clij = CLIJ.getInstance();
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
        IJ.exit();
        clij.close();
    }

    @Test
    public void erode2d_Buffers() {

        CLIJ clij = CLIJ.getInstance();
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
        IJ.exit();
        clij.close();
    }


}