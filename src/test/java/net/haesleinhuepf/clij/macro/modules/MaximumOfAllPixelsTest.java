package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import org.junit.Test;

import static org.junit.Assert.*;

public class MaximumOfAllPixelsTest {
    @Test
    public void maxPixels2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus imp = NewImage.createByteImage("test", 10, 10, 1, NewImage.FILL_BLACK);
        imp.getProcessor().set(5,5, 1);
        imp.getProcessor().set(5,6, 2);
        imp.getProcessor().set(5,7, 3);

        ClearCLImage maskCL = clij.convert(imp, ClearCLImage.class);

        double sum = Kernels.maximumOfAllPixels(clij, maskCL);

        assertTrue(sum == 3);

        maskCL.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void maxPixels2d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus imp = NewImage.createByteImage("test", 10, 10, 1, NewImage.FILL_BLACK);
        imp.getProcessor().set(5,5, 1);
        imp.getProcessor().set(5,6, 2);
        imp.getProcessor().set(5,7, 3);

        ClearCLBuffer maskCL = clij.convert(imp, ClearCLBuffer.class);

        double sum = Kernels.maximumOfAllPixels(clij, maskCL);

        assertTrue(sum == 3);

        maskCL.close();
        IJ.exit();
        clij.close();
    }

}