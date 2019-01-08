package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import ij.plugin.GaussianBlur3D;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Blur3DTest {
    private final static double relativeTolerance = 0.05;

    @Test
    public void blur3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/main/resources/flybrain.tif");

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testImp1);
        GaussianBlur3D.blur(gauss, 2, 2, 2);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.blur(clij, src, dst, 9, 9, 9, 2f, 2f, 2f);
        ImagePlus gaussFromCL = clij.convert(dst, ImagePlus.class);

        double tolerance = relativeTolerance * Kernels.maximumOfAllPixels(clij, dst);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, tolerance));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void blur3d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/main/resources/flybrain.tif");
        IJ.run(testImp1, "32-bit","");

        // do operation with ImageJ
        ImagePlus gauss = new Duplicator().run(testImp1);
        GaussianBlur3D.blur(gauss, 2, 2, 2);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.blur(clij, src, dst, 9, 9, 9, 2f, 2f, 2f);
        ImagePlus gaussFromCL = clij.convert(dst, ImagePlus.class);

        // ignore borders
        int bordersize = 5;
        gauss.setRoi(new Roi(bordersize, bordersize, gauss.getWidth() - bordersize * 2, gauss.getHeight() - bordersize * 2));
        gaussFromCL.setRoi(new Roi(bordersize, bordersize, gaussFromCL.getWidth() - bordersize * 2, gaussFromCL.getHeight() - bordersize * 2));
        gauss = new Duplicator().run(gauss, bordersize, gauss.getNSlices() - bordersize);
        gaussFromCL = new Duplicator().run(gaussFromCL, bordersize, gaussFromCL.getNSlices() - bordersize);

        double tolerance = relativeTolerance * Kernels.maximumOfAllPixels(clij, dst);

        assertTrue(TestUtilities.compareImages(gauss, gaussFromCL, tolerance));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

}