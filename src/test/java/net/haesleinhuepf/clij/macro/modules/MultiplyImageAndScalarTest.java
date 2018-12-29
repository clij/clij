package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiplyImageAndScalarTest {

    @Test
    public void multiplyScalar3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp1);
        IJ.run(added, "Multiply...", "value=2 stack");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.multiplyScalar(clij, src, dst, 2f);
        ImagePlus addedFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
    }


    @Test
    public void multiplyScalar3d_Buffer() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp1);
        IJ.run(added, "Multiply...", "value=2 stack");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.multiplyScalar(clij, src, dst, 2f);
        ImagePlus addedFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
    }

    @Test
    public void multiplyScalar2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp2D1);
        IJ.run(added, "Multiply...", "value=2");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.multiplyScalar(clij, src, dst, 2f);
        ImagePlus addedFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
    }

    @Test
    public void multiplyScalar2d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);

        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp2D1);
        IJ.run(added, "Multiply...", "value=2");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.multiplyScalar(clij, src, dst, 2f);
        ImagePlus addedFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
    }

    @Test
    public void multiplySliceBySliceWithScalars() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        ClearCLImage maskCL = clij.convert(mask3d, ClearCLImage.class);
        ClearCLImage multipliedBy2 = clij.createCLImage(maskCL);

        float[] factors = new float[(int) maskCL.getDepth()];
        for (int i = 0; i < factors.length; i++) {
            factors[i] = 2;
        }
        Kernels.multiplySliceBySliceWithScalars(clij, maskCL, multipliedBy2, factors);

        assertEquals(Kernels.sumPixels(clij, maskCL) * 2, Kernels.sumPixels(clij, multipliedBy2), 0.001);

        multipliedBy2.close();
        maskCL.close();

    }

    @Test
    public void multiplySliceBySliceWithScalars_Buffer() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

        ClearCLBuffer maskCL = clij.convert(mask3d, ClearCLBuffer.class);
        ClearCLBuffer multipliedBy2 = clij.createCLBuffer(maskCL);

        float[] factors = new float[(int) maskCL.getDepth()];
        for (int i = 0; i < factors.length; i++) {
            factors[i] = 2;
        }
        Kernels.multiplySliceBySliceWithScalars(clij, maskCL, multipliedBy2, factors);

        assertEquals(Kernels.sumPixels(clij, maskCL) * 2, Kernels.sumPixels(clij, multipliedBy2), 0.001);

        multipliedBy2.close();
        maskCL.close();

    }

}