package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.Duplicator;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class MaximumZProjectionTest {

    @Test
    public void maxProjection() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/main/resources/flybrain.tif");

        // do operation with ImageJ
        ImagePlus
                maxProjection =
                NewImage.createShortImage("",
                        testImp1.getWidth(),
                        testImp1.getHeight(),
                        1,
                        NewImage.FILL_BLACK);
        ImageProcessor ipMax = maxProjection.getProcessor();

        ImagePlus testImp1copy = new Duplicator().run(testImp1);
        for (int z = 0; z < testImp1copy.getNSlices(); z++) {
            testImp1copy.setZ(z + 1);
            ImageProcessor ip = testImp1copy.getProcessor();
            for (int x = 0; x < testImp1copy.getWidth(); x++) {
                for (int y = 0; y < testImp1copy.getHeight(); y++) {
                    float value = ip.getf(x, y);
                    if (value > ipMax.getf(x, y)) {
                        ipMax.setf(x, y, value);
                    }
                }
            }
        }

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage
                dst =
                clij.createCLImage(new long[]{src.getWidth(),
                                src.getHeight()},
                        src.getChannelDataType());

        Kernels.maximumZProjection(clij, src, dst);

        ImagePlus maxProjectionCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(maxProjection,
                maxProjectionCL));

        src.close();
        dst.close();
    }

    @Test
    public void maxProjection_Buffers() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = IJ.openImage("src/main/resources/flybrain.tif");

        // do operation with ImageJ
        ImagePlus
                maxProjection =
                NewImage.createShortImage("",
                        testImp1.getWidth(),
                        testImp1.getHeight(),
                        1,
                        NewImage.FILL_BLACK);
        ImageProcessor ipMax = maxProjection.getProcessor();

        ImagePlus testImp1copy = new Duplicator().run(testImp1);
        for (int z = 0; z < testImp1copy.getNSlices(); z++) {
            testImp1copy.setZ(z + 1);
            ImageProcessor ip = testImp1copy.getProcessor();
            for (int x = 0; x < testImp1copy.getWidth(); x++) {
                for (int y = 0; y < testImp1copy.getHeight(); y++) {
                    float value = ip.getf(x, y);
                    if (value > ipMax.getf(x, y)) {
                        ipMax.setf(x, y, value);
                    }
                }
            }
        }

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer
                dst =
                clij.createCLBuffer(new long[]{src.getWidth(),
                                src.getHeight()},
                        src.getNativeType());

        Kernels.maximumZProjection(clij, src, dst);

        ImagePlus maxProjectionCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(maxProjection,
                maxProjectionCL));

        src.close();
        dst.close();
    }

}