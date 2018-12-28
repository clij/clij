package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import ij.plugin.GaussianBlur3D;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Blur3DTest {


    @Test
    public void blur3d() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/main/resources/flybrain.tif");

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testImp1);
        GaussianBlur3D.blur(gauss, 2, 2, 2);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.blur(clij, src, dst, 6, 6, 6, 2f, 2f, 2f);
        ImagePlus gaussFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL));

        src.close();
        dst.close();
    }

    @Test
    public void blur3d_Buffers() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/main/resources/flybrain.tif");

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testImp1);
        GaussianBlur3D.blur(gauss, 2, 2, 2);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.blur(clij, src, dst, 6, 6, 6, 2f, 2f, 2f);
        ImagePlus gaussFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL));

        src.close();
        dst.close();
    }

}