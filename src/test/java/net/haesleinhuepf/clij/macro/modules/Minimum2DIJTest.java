package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.AbstractMacroPluginTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Minimum2DIJTest extends AbstractMacroPluginTest {
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
            makeMinimum2DIJ(clij, argsCL).executeCL();

            Object[] argsIJ = {bufferIn, bufferOutIJ, new Double(radius)};
            makeMinimum2DIJ(clij, argsIJ).executeIJ();

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

    private Minimum2DIJ makeMinimum2DIJ(CLIJ clij, Object[] args) {
        Minimum2DIJ minimum2DIJ = new Minimum2DIJ();
        minimum2DIJ.setClij(clij);
        minimum2DIJ.setArgs(args);
        return minimum2DIJ;
    }
}