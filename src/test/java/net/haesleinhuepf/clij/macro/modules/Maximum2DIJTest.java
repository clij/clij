package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.AbstractMacroPluginTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Maximum2DIJTest extends AbstractMacroPluginTest {
    @Test
    public void testIfIdentialWithImageJ(){
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();
        for (int radius = 1; radius < 50; radius++) {
            ImagePlus testImage = NewImage.createImage("", 200, 200, 1, 8, NewImage.FILL_BLACK);

            testImage.getProcessor().set(100, 100, 255);

            ClearCLBuffer bufferIn = clij.convert(testImage, ClearCLBuffer.class);
            ClearCLBuffer bufferOutCL = clij.createCLBuffer(bufferIn);
            ClearCLBuffer bufferOutIJ = clij.createCLBuffer(bufferIn);

            Object[] argsCL = {bufferIn, bufferOutCL, new Double(radius)};
            makeMaximum2DIJ(clij, argsCL).executeCL();

            Object[] argsIJ = {bufferIn, bufferOutIJ, new Double(radius)};
            makeMaximum2DIJ(clij, argsIJ).executeIJ();

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

    private Maximum2DIJ makeMaximum2DIJ(CLIJ clij, Object[] args) {
        Maximum2DIJ maximum2DIJ = new Maximum2DIJ();
        maximum2DIJ.setClij(clij);
        maximum2DIJ.setArgs(args);
        return maximum2DIJ;
    }
}