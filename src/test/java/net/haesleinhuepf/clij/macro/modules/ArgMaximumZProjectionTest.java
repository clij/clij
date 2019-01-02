package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
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

public class ArgMaximumZProjectionTest {

    @Test
    public void argMaxProjection() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 15);

        // do operation with ImageJ
        ImagePlus
                maxProjection =
                NewImage.createFloatImage("",
                        testImp1.getWidth(),
                        testImp1.getHeight(),
                        1,
                        NewImage.FILL_BLACK);
        ImageProcessor ipMax = maxProjection.getProcessor();
        ImagePlus
                argMaxProjection =
                NewImage.createFloatImage("",
                        testImp1.getWidth(),
                        testImp1.getHeight(),
                        1,
                        NewImage.FILL_BLACK);
        ImageProcessor ipArgMax = argMaxProjection.getProcessor();

        ImagePlus testImp1copy = new Duplicator().run(testImp1);
        for (int z = 0; z < testImp1copy.getNSlices(); z++) {
            testImp1copy.setZ(z + 1);
            ImageProcessor ip = testImp1copy.getProcessor();
            for (int x = 0; x < testImp1copy.getWidth(); x++) {
                for (int y = 0; y < testImp1copy.getHeight(); y++) {
                    float value = ip.getf(x, y);
                    if (value > ipMax.getf(x, y)) {
                        ipMax.setf(x, y, value);
                        ipArgMax.setf(x, y, z);
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
        ClearCLImage
                dst_arg =
                clij.createCLImage(new long[]{src.getWidth(),
                                src.getHeight()},
                        src.getChannelDataType());

        Kernels.argMaximumZProjection(clij, src, dst, dst_arg);

        ImagePlus maxProjectionCL = clij.convert(dst, ImagePlus.class);
        ImagePlus
                argMaxProjectionCL =
                clij.convert(dst_arg, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(maxProjection,
                maxProjectionCL));
        assertTrue(TestUtilities.compareImages(argMaxProjection,
                argMaxProjectionCL));

        src.close();
        dst.close();
        dst_arg.close();
    }

    @Test
    public void argMaxProjection_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 15);

        // do operation with ImageJ
        ImagePlus
                maxProjection =
                NewImage.createFloatImage("",
                        testImp1.getWidth(),
                        testImp1.getHeight(),
                        1,
                        NewImage.FILL_BLACK);
        ImageProcessor ipMax = maxProjection.getProcessor();
        ImagePlus
                argMaxProjection =
                NewImage.createFloatImage("",
                        testImp1.getWidth(),
                        testImp1.getHeight(),
                        1,
                        NewImage.FILL_BLACK);
        ImageProcessor ipArgMax = maxProjection.getProcessor();

        ImagePlus testImp1copy = new Duplicator().run(testImp1);
        for (int z = 0; z < testImp1copy.getNSlices(); z++) {
            testImp1copy.setZ(z + 1);
            ImageProcessor ip = testImp1copy.getProcessor();
            for (int x = 0; x < testImp1copy.getWidth(); x++) {
                for (int y = 0; y < testImp1copy.getHeight(); y++) {
                    float value = ip.getf(x, y);
                    if (value > ipMax.getf(x, y)) {
                        ipMax.setf(x, y, value);
                        ipArgMax.setf(x, y, z);
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
        ClearCLBuffer
                dst_arg =
                clij.createCLBuffer(new long[]{src.getWidth(),
                                src.getHeight()},
                        src.getNativeType());

        Kernels.argMaximumZProjection(clij, src, dst, dst_arg);

        ImagePlus maxProjectionCL = clij.convert(dst, ImagePlus.class);
        ImagePlus
                argMaxProjectionCL =
                clij.convert(dst_arg, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(maxProjection,
                maxProjectionCL));
        assertTrue(TestUtilities.compareImages(argMaxProjection,
                argMaxProjectionCL));

        src.close();
        dst.close();
        dst_arg.close();
    }


}