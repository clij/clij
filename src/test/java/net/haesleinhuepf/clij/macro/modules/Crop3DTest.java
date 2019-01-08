package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Crop3DTest {

    @Test
    public void crop3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 20, 32, 0, 100);

        // do operation with ImageJ
        Roi roi = new Roi(2, 2, 10, 10);
        testImp1.setRoi(roi);
        ImagePlus crop = new Duplicator().run(testImp1, 3, 12);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(new long[]{10, 10, 10}, src.getChannelDataType());

        Kernels.crop(clij, src, dst, 2, 2, 2);
        ImagePlus cropFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(crop, cropFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }


    @Test
    public void cropBuffer3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 20, 32, 0, 100);

        // do operation with ImageJ
        Roi roi = new Roi(2, 2, 10, 10);
        testImp1.setRoi(roi);
        ImagePlus crop = new Duplicator().run(testImp1, 3, 12);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(new long[]{10, 10, 10}, src.getNativeType());

        Kernels.crop(clij, src, dst, 2, 2, 2);
        ImagePlus cropFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(crop, cropFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

}