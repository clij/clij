package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractMacroPluginTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ErodeBoxIJTest extends AbstractMacroPluginTest {
    @Test
    public void testIfIdentialWithImageJ(){
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImage = NewImage.createImage("", 200, 200, 1, 8, NewImage.FILL_BLACK);

        // box; a dot should stay
        testImage.getProcessor().set(100, 100, 255);
        testImage.getProcessor().set(101, 100, 255);
        testImage.getProcessor().set(102, 100, 255);
        testImage.getProcessor().set(100, 101, 255);
        testImage.getProcessor().set(101, 101, 255);
        testImage.getProcessor().set(102, 101, 255);
        testImage.getProcessor().set(100, 102, 255);
        testImage.getProcessor().set(101, 102, 255);
        testImage.getProcessor().set(102, 102, 255);

        // cross; should disappear
        testImage.getProcessor().set(110, 110, 255);
        testImage.getProcessor().set(111, 110, 255);
        testImage.getProcessor().set(110, 111, 255);
        testImage.getProcessor().set(110, 109, 255);
        testImage.getProcessor().set(109, 110, 255);

        // single dot; should disappear
        testImage.getProcessor().set(120, 120, 255);

        ClearCLBuffer bufferIn = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer bufferOutCL = clij.createCLBuffer(bufferIn);
        ClearCLBuffer bufferOutIJ = clij.createCLBuffer(bufferIn);

        Object[] argsCL = {bufferIn, bufferOutCL};
        makeErodeBoxIJ(clij, argsCL).executeCL();

        Object[] argsIJ = {bufferIn, bufferOutIJ};
        makeErodeBoxIJ(clij, argsIJ).executeIJ();

        // clij.show(bufferOutCL, "cl " + bufferOutCL);
        // clij.show(bufferOutIJ, "ij");
        // new WaitForUserDialog("wait").show();

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

    private ErodeBoxIJ makeErodeBoxIJ(CLIJ clij, Object[] args) {
        ErodeBoxIJ erodeBoxIJ = new ErodeBoxIJ();
        erodeBoxIJ.setClij(clij);
        erodeBoxIJ.setArgs(args);
        return erodeBoxIJ;
    }
}