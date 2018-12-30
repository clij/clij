package net.haesleinhuepf.clij.macro.benchmarking;

import clearcl.ClearCLBuffer;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.AbstractMacroPluginTest;
import net.haesleinhuepf.clij.macro.modules.Mean2DSphere;
import net.haesleinhuepf.clij.macro.modules.Mean2DIJ;
import net.haesleinhuepf.clij.macro.modules.Mean2DBox;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Mean2DBenchmark extends AbstractMacroPluginTest {
    @Test
    public void testMean2DIJ(){
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();
        for (int radius = 1; radius < 50; radius++) {
            ImagePlus testImage = NewImage.createImage("", 200, 200, 1, 8, NewImage.FILL_BLACK);

            testImage.getProcessor().set(100, 100, 255);

            ClearCLBuffer bufferIn = clij.convert(testImage, ClearCLBuffer.class);
            ClearCLBuffer bufferOutCL = clij.createCLBuffer(bufferIn);
            ClearCLBuffer bufferOutIJ = clij.createCLBuffer(bufferIn);

            long time = System.currentTimeMillis();
            Object[] argsCL = {bufferIn, bufferOutCL, new Double(radius)};
            initPlugin(clij, new Mean2DIJ(), argsCL).executeCL();
            long clMean2DIJDuration = System.currentTimeMillis() - time;

            time = System.currentTimeMillis();
            Object[] argsIJ = {bufferIn, bufferOutIJ, new Double(radius)};
            initPlugin(clij, new Mean2DIJ(), argsIJ).executeIJ();
            long ijDuration = System.currentTimeMillis() - time;

            time = System.currentTimeMillis();
            Object[] argsMeanEllipse = {bufferIn, bufferOutIJ, new Double(radius), new Double(radius)};
            initPlugin(clij, new Mean2DSphere(), argsMeanEllipse).executeCL();
            long clMean2DEllipseDuration = System.currentTimeMillis() - time;

            time = System.currentTimeMillis();
            Object[] argsMeanBox = {bufferIn, bufferOutIJ, new Double(radius), new Double(radius)};
            initPlugin(clij, new Mean2DBox(), argsMeanBox).executeCL();
            long clMean2DBoxDuration = System.currentTimeMillis() - time;

            System.out.println("Radius " + radius);
            System.out.println("IJ meanSphere   duration " + ijDuration + " msec");
            System.out.println("CL mean2DIJ duration " + clMean2DIJDuration + " msec");
            System.out.println("CL mean2DEllipse duration " + clMean2DEllipseDuration + " msec");
            System.out.println("CL mean2DBox duration " + clMean2DBoxDuration + " msec");

            bufferIn.close();
            bufferOutCL.close();
            bufferOutIJ.close();
        }
    }

    private <T extends AbstractCLIJPlugin> T initPlugin(CLIJ clij, T plugin, Object[] args) {
        plugin.setClij(clij);
        plugin.setArgs(args);
        return plugin;
    }
}