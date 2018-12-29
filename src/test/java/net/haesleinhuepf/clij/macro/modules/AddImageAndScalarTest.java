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

public class AddImageAndScalarTest {
    @Test
    public void addScalar3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 8, 0, 15);

        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp1);
        IJ.run(added, "Add...", "value=1 stack");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp1, ClearCLImage.class);

        Kernels.addScalar(clij, src, dst, 1f);
        ImagePlus addedFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
    }


    @Test
    public void addScalar3d_Buffer() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 8, 0, 15);

        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp1);
        IJ.run(added, "Add...", "value=1 stack");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.convert(testImp1, ClearCLBuffer.class);

        Kernels.addScalar(clij, src, dst, 1f);
        ImagePlus addedFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
    }

    @Test
    public void addScalar2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 8, 0, 15);

        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp2D1);
        IJ.run(added, "Add...", "value=1");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp2D1, ClearCLImage.class);

        Kernels.addScalar(clij, src, dst, 1f);
        ImagePlus addedFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
    }

    @Test
    public void addScalar2d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 8, 0, 15);
        // do operation with ImageJ
        ImagePlus added = new Duplicator().run(testImp2D1);
        IJ.run(added, "Add...", "value=1");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.convert(testImp2D1, ClearCLBuffer.class);

        Kernels.addScalar(clij, src, dst, 1f);
        ImagePlus addedFromCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(added, addedFromCL));

        src.close();
        dst.close();
    }

}