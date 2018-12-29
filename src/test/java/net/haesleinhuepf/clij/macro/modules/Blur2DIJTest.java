package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.AbstractMacroPluginTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Blur2DIJTest extends AbstractMacroPluginTest {

    private final static double tolerance = 0.5;

    @Test
    public void testIfIdentialWithImageJ(){
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();
        for (int sigma = 1; sigma < 50; sigma++) {
            ImagePlus testImage = NewImage.createImage("", 200, 200, 1, 32, NewImage.FILL_BLACK);

            testImage.getProcessor().setf(100, 100, 100);
            testImage.show();

            ClearCLBuffer bufferIn = clij.convert(testImage, ClearCLBuffer.class);
            ClearCLBuffer bufferOutCL = clij.createCLBuffer(bufferIn);
            ClearCLBuffer bufferOutIJ = clij.createCLBuffer(bufferIn);

            Object[] argsCL = {bufferIn, bufferOutCL, new Double(sigma)};
            makeBlur2DIJ(clij, argsCL).executeCL();

            Object[] argsIJ = {bufferIn, bufferOutIJ, new Double(sigma)};
            makeBlur2DIJ(clij, argsIJ).executeIJ();

            //clij.show(bufferOutCL, "cl " + bufferOutCL);
            //clij.show(bufferOutIJ, "ij");
            //new WaitForUserDialog("wait").show();

            System.out.println("Sigma " + sigma);
            assertTrue(clBuffersEqual(clij, bufferOutIJ, bufferOutCL, tolerance));
            bufferIn.close();
            bufferOutCL.close();
            bufferOutIJ.close();
        }
    }

    private Blur2DIJ makeBlur2DIJ(CLIJ clij, Object[] args) {
        Blur2DIJ blur2DIJ = new Blur2DIJ();
        blur2DIJ.setClij(clij);
        blur2DIJ.setArgs(args);
        return blur2DIJ;
    }
}