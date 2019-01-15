package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiplyImagesTest {
    @Test
    public void multiplyPixelwise3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage src1 = clij.convert(testImp2, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp1, ClearCLImage.class);

        Kernels.multiplyImages(clij, src, src1, dst);

        ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Ignore
    @Test
    public void multiplyPixelwise3dathousandtimes() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        for (int i = 0; i < 1000; i++) {
            // do operation with ClearCL
            ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
            ClearCLImage src1 = clij.convert(testImp2, ClearCLImage.class);
            ClearCLImage dst = clij.convert(testImp1, ClearCLImage.class);

            Kernels.multiplyImages(clij, src, src1, dst);

            ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

            assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

            src.close();
            src1.close();
            dst.close();
        }
        IJ.exit();
        clij.close();
    }

    @Test
    public void multiplyPixelwise3d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clij.convert(testImp2, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.convert(testImp1, ClearCLBuffer.class);

        Kernels.multiplyImages(clij, src, src1, dst);

        ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Ignore
    @Test
    public void multiplyPixelwise3d_Buffers_athousandtimes() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        for (int i = 0; i < 1000; i++) {
            // do operation with ClearCL
            ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
            ClearCLBuffer src1 = clij.convert(testImp2, ClearCLBuffer.class);
            ClearCLBuffer dst = clij.convert(testImp1, ClearCLBuffer.class);

            Kernels.multiplyImages(clij, src, src1, dst);

            ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

            assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

            src.close();
            src1.close();
            dst.close();
        }
        IJ.exit();
        clij.close();
    }



    @Test
    public void multiplyPixelwise2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D2);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage src1 = clij.convert(testImp2D2, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp2D1, ClearCLImage.class);

        Kernels.multiplyImages(clij, src, src1, dst);

        ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void multiplyPixelwise2d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D2);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clij.convert(testImp2D2, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.convert(testImp2D1, ClearCLBuffer.class);

        Kernels.multiplyImages(clij, src, src1, dst);

        ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

}