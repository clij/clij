package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Downsample2DTest {
    @Test
    public void downsample2d() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        new ImageJ(); // the menu command 'Scale...' can only be executed successfully if the ImageJ UI is visible; apparently
        testImp2D1.show();
        IJ.run(testImp2D1, "Scale...", "x=0.5 y=0.5 width=50 height=50 interpolation=None create");
        ImagePlus downsampled = IJ.getImage();

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage
                dst =
                clij.createCLImage(new long[]{src.getWidth() / 2,
                                src.getHeight() / 2},
                        src.getChannelDataType());

        Kernels.downsample(clij, src, dst, 0.5f, 0.5f);

        ImagePlus downsampledCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(downsampled, downsampledCL, 1.0));

        IJ.exit();
        clij.close();
    }


    @Test
    public void downsample2d_Buffers() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        new ImageJ(); // the menu command 'Scale...' can only be executed successfully if the ImageJ UI is visible; apparently
        testImp2D1.show();
        IJ.run(testImp2D1, "Scale...", "x=0.5 y=0.5 width=50 height=50 interpolation=None create");
        ImagePlus downsampled = IJ.getImage();

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer
                dst =
                clij.createCLBuffer(new long[]{src.getWidth() / 2,
                                src.getHeight() / 2},
                        src.getNativeType());

        Kernels.downsample(clij, src, dst, 0.5f, 0.5f);

        ImagePlus downsampledCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(downsampled, downsampledCL, 1.0));

        IJ.exit();
        clij.close();
    }

}