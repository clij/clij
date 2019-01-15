package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.AbstractMacroPluginTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class Mean2DIJTest extends AbstractMacroPluginTest {
    @Test
    public void testIfIdenticalWithImageJ(){
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();
        clij.close();
        clij = CLIJ.getInstance();

        for (int radius = 1; radius < 50; radius++) {
            ImagePlus testImage = NewImage.createImage("", 200, 200, 1, 8, NewImage.FILL_BLACK);

            testImage.getProcessor().set(100, 100, 255);

            ClearCLBuffer bufferIn = clij.convert(testImage, ClearCLBuffer.class);
            ClearCLBuffer bufferOutCL = clij.createCLBuffer(bufferIn);
            ClearCLBuffer bufferOutIJ = clij.createCLBuffer(bufferIn);

            Object[] argsCL = {bufferIn, bufferOutCL, new Double(radius)};
            makeMean2DIJ(clij, argsCL).executeCL();

            Object[] argsIJ = {bufferIn, bufferOutIJ, new Double(radius)};
            makeMean2DIJ(clij, argsIJ).executeIJ();

            //clij.show(bufferOutCL, "cl " + bufferOutCL);
            //clij.show(bufferOutIJ, "ij");
            //new WaitForUserDialog("wait").show();

            System.out.println("Radius " + radius);
            assertTrue(clBuffersEqual(clij, bufferOutIJ, bufferOutCL));
            bufferIn.close();
            bufferOutCL.close();
            bufferOutIJ.close();
        }
        IJ.exit();
        clij.close();
    }

    private Mean2DIJ makeMean2DIJ(CLIJ clij, Object[] args) {
        Mean2DIJ mean2DIJ = new Mean2DIJ();
        mean2DIJ.setClij(clij);
        mean2DIJ.setArgs(args);
        return mean2DIJ;
    }
}