package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class DivideImagesTest {

    @Test
    public void dividePixelwise3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus divided = new ImageCalculator().run("Divide create stack", testImp1, testImp2);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage src1 = clij.convert(testImp2, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.divideImages(clij, src, src1, dst);

        ImagePlus dividedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(divided, dividedCL, 0.0001));

        src.close();
        src1.close();
        dst.close();
    }


    @Test
    public void dividePixelwise3d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                divided =
                new ImageCalculator().run("Divide create stack",
                        testImp1,
                        testImp2);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clij.convert(testImp2, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.divideImages(clij, src, src1, dst);

        ImagePlus dividedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(divided, dividedCL, 0.0001));

        src.close();
        src1.close();
        dst.close();
    }

    @Test
    public void dividePixelwise2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus
                divided =
                new ImageCalculator().run("Divide create",
                        testImp2D1,
                        testImp2D2);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage src1 = clij.convert(testImp2D2, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.divideImages(clij, src, src1, dst);

        ImagePlus dividedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(divided, dividedCL, 0.0001));

        src.close();
        src1.close();
        dst.close();
    }

    @Test
    public void dividePixelwise2d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus divided = new ImageCalculator().run("Divide create", testImp2D1, testImp2D2);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clij.convert(testImp2D2, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.divideImages(clij, src, src1, dst);

        ImagePlus dividedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(divided, dividedCL, 0.0001));

        src.close();
        src1.close();
        dst.close();
    }

}