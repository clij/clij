package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Downsample3DTest {

    @Test
    public void downsample3d() throws InterruptedException {

        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/test/resources/flybrain.tif");

        // do operation with ImageJ
        new ImageJ(); // the menu command 'Scale...' can only be executed successfully if the ImageJ UI is visible; apparently
        testImp1.show();
        IJ.run(testImp1, "Scale...", "x=0.5 y=0.5 z=0.5 interpolation=None process create");
        ImagePlus downsampled = IJ.getImage();


        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst =
                clij.createCLImage(new long[]{src.getWidth() / 2,
                                src.getHeight() / 2,
                                (long) (src.getDepth() - 0.5) / 2},
                        src.getChannelDataType());


        Kernels.downsample(clij, src, dst, 0.5f, 0.5f, 0.5f);

        ImagePlus downsampledCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(downsampled, downsampledCL, 1.0));
        IJ.exit();
        clij.close();
    }

    @Test
    public void downsample3d_Buffers() throws InterruptedException {

        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/test/resources/flybrain.tif");


        // do operation with ImageJ
        new ImageJ(); // the menu command 'Scale...' can only be executed successfully if the ImageJ UI is visible; apparently
        testImp1.show();
        IJ.run(testImp1, "Scale...", "x=0.5 y=0.5 z=0.5 interpolation=None process create");
        //Thread.sleep(1000);
        ImagePlus downsampled = IJ.getImage();


        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer dst =
                clij.createCLBuffer(new long[]{src.getWidth() / 2,
                                src.getHeight() / 2,
                                (long) (src.getDepth() - 0.5) / 2},
                        src.getNativeType());


        Kernels.downsample(clij, src, dst, 0.5f, 0.5f, 0.5f);

        ImagePlus downsampledCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(downsampled, downsampledCL, 1.0));
        IJ.exit();
        clij.close();
    }
}