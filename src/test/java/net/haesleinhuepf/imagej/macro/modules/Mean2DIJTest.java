package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCL;
import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.gui.WaitForUserDialog;
import ij.plugin.Duplicator;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.macro.AbstractMacroPluginTest;
import net.haesleinhuepf.imagej.test.KernelsTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class Mean2DIJTest extends AbstractMacroPluginTest {
    @Test
    public void testMean2DIJ(){
        new ImageJ();

        ClearCLIJ clij = ClearCLIJ.getInstance();
        
        ImagePlus testImage = NewImage.createImage("", 20, 20, 1, 8, NewImage.FILL_BLACK);

        testImage.getProcessor().set(10,10,255);

        ClearCLBuffer bufferIn = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer bufferOutCL = clij.createCLBuffer(bufferIn);
        ClearCLBuffer bufferOutIJ = clij.createCLBuffer(bufferIn);


        Object[] argsCL = {bufferIn, bufferOutCL, new Double(5)};
        makeMean2DIJ(clij, argsCL).executeCL();

        Object[] argsIJ = {bufferIn, bufferOutIJ, new Double(5)};
        makeMean2DIJ(clij, argsIJ).executeIJ();

        clij.show(bufferOutCL, "cl " + bufferOutCL);
        clij.show(bufferOutIJ, "ij");

        new WaitForUserDialog("wait").show();

        assertTrue(clBuffersEqual(clij, bufferOutIJ, bufferOutCL));
        bufferIn.close();
        bufferOutCL.close();
        bufferOutIJ.close();
    }

    private Mean2DIJ makeMean2DIJ(ClearCLIJ clij, Object[] args) {
        Mean2DIJ mean2DIJ = new Mean2DIJ();
        mean2DIJ.setClij(clij);
        mean2DIJ.setArgs(args);
        return mean2DIJ;
    }
}