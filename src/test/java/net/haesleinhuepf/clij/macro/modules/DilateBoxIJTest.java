package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.gui.WaitForUserDialog;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractMacroPluginTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DilateBoxIJTest extends AbstractMacroPluginTest {
    @Test
    public void testIfIdentialWithImageJ(){
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImage = NewImage.createImage("", 200, 200, 1, 8, NewImage.FILL_BLACK);

        // box; should become a bigger box
        testImage.getProcessor().set(100, 100, 255);
        testImage.getProcessor().set(101, 100, 255);
        testImage.getProcessor().set(102, 100, 255);
        testImage.getProcessor().set(100, 101, 255);
        testImage.getProcessor().set(101, 101, 255);
        testImage.getProcessor().set(102, 101, 255);
        testImage.getProcessor().set(100, 102, 255);
        testImage.getProcessor().set(101, 102, 255);
        testImage.getProcessor().set(102, 102, 255);

        // cross; should become a circle like thing
        testImage.getProcessor().set(110, 110, 255);
        testImage.getProcessor().set(111, 110, 255);
        testImage.getProcessor().set(110, 111, 255);
        testImage.getProcessor().set(110, 109, 255);
        testImage.getProcessor().set(109, 110, 255);

        // dot; should become a box
        testImage.getProcessor().set(120, 120, 255);


        ClearCLBuffer bufferIn = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer bufferOutCL = clij.createCLBuffer(bufferIn);
        ClearCLBuffer bufferOutIJ = clij.createCLBuffer(bufferIn);

        Object[] argsCL = {bufferIn, bufferOutCL};
        makeDilateBoxIJ(clij, argsCL).executeCL();

        Object[] argsIJ = {bufferIn, bufferOutIJ};
        makeDilateBoxIJ(clij, argsIJ).executeIJ();

        //clij.show(bufferOutCL, "cl " + bufferOutCL);
        //clij.show(bufferOutIJ, "ij");
        //new WaitForUserDialog("wait").show();

        ClearCLBuffer bufferOutCLBinary = clij.createCLBuffer(bufferIn);
        ClearCLBuffer bufferOutIJBinary = clij.createCLBuffer(bufferIn);

        Kernels.threshold(clij, bufferOutCL, bufferOutCLBinary, 1f);
        Kernels.threshold(clij, bufferOutIJ, bufferOutIJBinary, 1f);

        assertTrue(clBuffersEqual(clij, bufferOutIJBinary, bufferOutCLBinary));
        bufferIn.close();
        bufferOutCL.close();
        bufferOutIJ.close();
        bufferOutCLBinary.close();
        bufferOutIJBinary.close();

        IJ.exit();
        clij.close();
    }

    private DilateBoxIJ makeDilateBoxIJ(CLIJ clij, Object[] args) {
        DilateBoxIJ dilateBoxIJ = new DilateBoxIJ();
        dilateBoxIJ.setClij(clij);
        dilateBoxIJ.setArgs(args);
        return dilateBoxIJ;
    }

    @Test
    public void test_dilateBox_3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = NewImage.createByteImage("Test", 10, 10, 10, NewImage.FILL_BLACK);
        mask3d.setZ(5);
        ImageProcessor ip = mask3d.getProcessor();
        ip.set(5,5, 1);

        ClearCLBuffer maskCL = clij.convert(mask3d, ClearCLBuffer.class);
        double sum = Kernels.sumPixels(clij, maskCL);
        assertEquals(1, sum, 0.0);

        ClearCLBuffer maskCL2 = clij.createCLBuffer(maskCL);
        Kernels.dilateBox(clij, maskCL, maskCL2);


        double sum2 = Kernels.sumPixels(clij, maskCL2);

        assertEquals(27, sum2, 0.0);

        ClearCLBuffer maskCL3 = clij.createCLBuffer(maskCL);
        Kernels.dilateBox(clij, maskCL2, maskCL3);
        double sum3 = Kernels.sumPixels(clij, maskCL3);
        assertEquals(125, sum3, 0.0);


        maskCL.close();
        maskCL2.close();
        maskCL3.close();
        IJ.exit();
        clij.close();

    }
}