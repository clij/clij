package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import ij.plugin.ImageCalculator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddImagesWeightedTest {

    @Test
    public void addWeightedPixelwise3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 16, 0, 15);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 16, 0, 15);

        float factor1 = 3f;
        float factor2 = 2;

        // do operation with ImageJ
        ImagePlus testImp1copy = new Duplicator().run(testImp1);
        ImagePlus testImp2copy = new Duplicator().run(testImp2);
        IJ.run(testImp1copy,
                "Multiply...",
                "value=" + factor1 + " stack");
        IJ.run(testImp2copy,
                "Multiply...",
                "value=" + factor2 + " stack");

        ImageCalculator ic = new ImageCalculator();
        ImagePlus
                sumImp =
                ic.run("Add create stack", testImp1copy, testImp2copy);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage src1 = clij.convert(testImp2, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp1, ClearCLImage.class);

        Kernels.addImagesWeighted(clij,
                src,
                src1,
                dst,
                factor1,
                factor2);
        ImagePlus sumImpFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

        src.close();
        src1.close();
        dst.close();
    }

    @Test
    public void addWeightedPixelwise3d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 16, 0, 15);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 16, 0, 15);

        float factor1 = 3f;
        float factor2 = 2;

        // do operation with ImageJ
        ImagePlus testImp1copy = new Duplicator().run(testImp1);
        ImagePlus testImp2copy = new Duplicator().run(testImp2);
        IJ.run(testImp1copy,
                "Multiply...",
                "value=" + factor1 + " stack");
        IJ.run(testImp2copy,
                "Multiply...",
                "value=" + factor2 + " stack");

        ImageCalculator ic = new ImageCalculator();
        ImagePlus
                sumImp =
                ic.run("Add create stack", testImp1copy, testImp2copy);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clij.convert(testImp2, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.convert(testImp1, ClearCLBuffer.class);

        Kernels.addImagesWeighted(clij,
                src,
                src1,
                dst,
                factor1,
                factor2);
        ImagePlus sumImpFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void addWeightedPixelwise2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 16, 0, 15);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 16, 0, 15);

        float factor1 = 3f;
        float factor2 = 2;

        // do operation with ImageJ
        ImagePlus testImp1copy = new Duplicator().run(testImp2D1);
        ImagePlus testImp2copy = new Duplicator().run(testImp2D2);
        IJ.run(testImp1copy, "Multiply...", "value=" + factor1 + " ");
        IJ.run(testImp2copy, "Multiply...", "value=" + factor2 + " ");

        ImageCalculator ic = new ImageCalculator();
        ImagePlus
                sumImp =
                ic.run("Add create ", testImp1copy, testImp2copy);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage src1 = clij.convert(testImp2D2, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp2D1, ClearCLImage.class);

        Kernels.addImagesWeighted(clij,
                src,
                src1,
                dst,
                factor1,
                factor2);
        ImagePlus sumImpFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void addWeightedPixelwise2d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 16, 0, 15);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 16, 0, 15);

        float factor1 = 3f;
        float factor2 = 2;

        // do operation with ImageJ
        ImagePlus testImp1copy = new Duplicator().run(testImp2D1);
        ImagePlus testImp2copy = new Duplicator().run(testImp2D2);
        IJ.run(testImp1copy, "Multiply...", "value=" + factor1 + " ");
        IJ.run(testImp2copy, "Multiply...", "value=" + factor2 + " ");

        ImageCalculator ic = new ImageCalculator();
        ImagePlus
                sumImp =
                ic.run("Add create ", testImp1copy, testImp2copy);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clij.convert(testImp2D2, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.convert(testImp2D1, ClearCLBuffer.class);

        Kernels.addImagesWeighted(clij,
                src,
                src1,
                dst,
                factor1,
                factor2);
        ImagePlus sumImpFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

        src.close();
        src1.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

}