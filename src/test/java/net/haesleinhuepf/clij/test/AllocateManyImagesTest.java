package net.haesleinhuepf.clij.test;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

public class AllocateManyImagesTest {
    @Ignore // this test serves causing a GPU OUT OF MEMORY error. We should not run it in routine. It's made for debugging
    @Test
    public void testIterativeAllocation() {
        System.out.println("test started");
        CLIJ clij = CLIJ.getInstance();


        ImagePlus imp256MB = NewImage.createByteImage("Test1GB", 1024, 1024, 128, NewImage.FILL_RANDOM);

        System.out.println("test imp allocated");

        //ClearCLBuffer buffer = clij.convert(imp, ClearCLBuffer.class);

        // we assume having 8 GB of memory
        // next: allocating 80 images with 0.125GB each.

        HashMap<Integer, ClearCLImage> cache = new HashMap<Integer, ClearCLImage>();

        for (int i = 0; i < 80; i += 2) {
            System.out.println("iteration " + i);
            ClearCLImage input = clij.convert(imp256MB, ClearCLImage.class);
            ClearCLImage output = clij.convert(imp256MB, ClearCLImage.class);

            cache.put(i, input);
            cache.put(i + 1, output);

            Kernels.absolute(clij, input, output);
        }
        IJ.exit();
        clij.close();
    }
}
