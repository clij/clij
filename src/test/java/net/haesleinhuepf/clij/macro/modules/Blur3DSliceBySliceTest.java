package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Blur3DSliceBySliceTest {

    @Test
    public void blurSliceBySlice() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testFlyBrain3D);
        IJ.run(gauss, "Gaussian Blur...", "sigma=2 stack");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testFlyBrain3D, ClearCLImage.class);;
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.blurSliceBySlice(clij, src, dst, 15, 15, 2f, 2f);
        ImagePlus gaussFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, 2));

        src.close();
        dst.close();
    }

    @Test public void blurSliceBySlice_Buffers()
    {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testFlyBrain3D);
        IJ.run(gauss, "Gaussian Blur...", "sigma=2 stack");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testFlyBrain3D, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.blurSliceBySlice(clij, src, dst, 15, 15, 2, 2);
        ImagePlus gaussFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, 2));

        src.close();
        dst.close();
    }

}