package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Crop2DTest {

    @Test
    public void crop2d() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);

        // do operation with ImageJ
        Roi roi = new Roi(2, 2, 10, 10);
        testImp2D1.setRoi(roi);
        ImagePlus crop = new Duplicator().run(testImp2D1);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage
                dst =
                clij.createCLImage(new long[]{10, 10},
                        src.getChannelDataType());

        Kernels.crop(clij, src, dst, 2, 2);
        ImagePlus cropFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(crop, cropFromCL));

        src.close();
        dst.close();
    }

    @Test
    public void cropBuffer2d() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);

        // do operation with ImageJ
        Roi roi = new Roi(2, 2, 10, 10);
        testImp2D1.setRoi(roi);
        ImagePlus crop = new Duplicator().run(testImp2D1);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer
                dst =
                clij.createCLBuffer(new long[]{10, 10},
                        src.getNativeType());

        Kernels.crop(clij, src, dst, 2, 2);
        ImagePlus cropFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(crop, cropFromCL));

        src.close();
        dst.close();
    }

}