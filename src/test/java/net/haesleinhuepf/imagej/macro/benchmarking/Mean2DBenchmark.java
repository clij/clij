package net.haesleinhuepf.imagej.macro.benchmarking;

import clearcl.ClearCLBuffer;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.macro.AbstractMacroPluginTest;
import net.haesleinhuepf.imagej.macro.modules.Mean2DIJ;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Mean2DBenchmark extends AbstractMacroPluginTest {
    @Test
    public void testMean2DIJ(){
        new ImageJ();

        ClearCLIJ clij = ClearCLIJ.getInstance();
        for (int radius = 1; radius < 50; radius++) {
            ImagePlus testImage = NewImage.createImage("", 200, 200, 1, 8, NewImage.FILL_BLACK);

            testImage.getProcessor().set(100, 100, 255);

            ClearCLBuffer bufferIn = clij.convert(testImage, ClearCLBuffer.class);
            ClearCLBuffer bufferOutCL = clij.createCLBuffer(bufferIn);
            ClearCLBuffer bufferOutIJ = clij.createCLBuffer(bufferIn);

            long time = System.currentTimeMillis();
            Object[] argsCL = {bufferIn, bufferOutCL, new Double(radius)};
            makeMean2DIJ(clij, argsCL).executeCL();
            long clDuration = System.currentTimeMillis() - time;

            time = System.currentTimeMillis();
            Object[] argsIJ = {bufferIn, bufferOutIJ, new Double(radius)};
            makeMean2DIJ(clij, argsIJ).executeIJ();
            long ijDuration = System.currentTimeMillis() - time;

            System.out.println("Radius " + radius);
            System.out.println("IJ duration " + ijDuration + " msec");
            System.out.println("CL duration " + clDuration + " msec");

            bufferIn.close();
            bufferOutCL.close();
            bufferOutIJ.close();
        }
    }

    private Mean2DIJ makeMean2DIJ(ClearCLIJ clij, Object[] args) {
        Mean2DIJ mean2DIJ = new Mean2DIJ();
        mean2DIJ.setClij(clij);
        mean2DIJ.setArgs(args);
        return mean2DIJ;
    }
}