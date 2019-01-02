package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLImage;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.Duplicator;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class SumZProjectionTest {
    private final static double tolerance = 2.0;

    @Test
    public void sumProjectionUnsignedShort() throws InterruptedException {

        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 16, 1, 100);

        // do operation with ImageJ
        ImagePlus
                sumProjection =
                NewImage.createShortImage("",
                        testImp1.getWidth(),
                        testImp1.getHeight(),
                        1,
                        NewImage.FILL_BLACK);
        ImageProcessor ipSum = sumProjection.getProcessor();

        ImagePlus testImp1copy = new Duplicator().run(testImp1);
        for (int z = 0; z < testImp1copy.getNSlices(); z++) {
            testImp1copy.setZ(z + 1);
            ImageProcessor ip = testImp1copy.getProcessor();
            for (int x = 0; x < testImp1copy.getWidth(); x++) {
                for (int y = 0; y < testImp1copy.getHeight(); y++) {
                    float value = ip.getf(x, y) + ipSum.getf(x, y);
                    ipSum.setf(x, y, value);
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

        Kernels.sumZProjection(clij, src, dst);

        ImagePlus sumProjectionCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(sumProjection,
                sumProjectionCL, tolerance));

        src.close();
        dst.close();
    }

    @Test
    public void sumProjectionUnsignedFloat() throws InterruptedException {

        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                sumProjection =
                NewImage.createShortImage("",
                        testImp1.getWidth(),
                        testImp1.getHeight(),
                        1,
                        NewImage.FILL_BLACK);
        ImageProcessor ipSum = sumProjection.getProcessor();

        ImagePlus testImp1copy = new Duplicator().run(testImp1);
        for (int z = 0; z < testImp1copy.getNSlices(); z++) {
            testImp1copy.setZ(z + 1);
            ImageProcessor ip = testImp1copy.getProcessor();
            for (int x = 0; x < testImp1copy.getWidth(); x++) {
                for (int y = 0; y < testImp1copy.getHeight(); y++) {
                    float value = ip.getf(x, y) + ipSum.getf(x, y);
                    ipSum.setf(x, y, value);
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

        Kernels.sumZProjection(clij, src, dst);

        ImagePlus sumProjectionCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(sumProjection,
                sumProjectionCL, tolerance));

        src.close();
        dst.close();
    }
}