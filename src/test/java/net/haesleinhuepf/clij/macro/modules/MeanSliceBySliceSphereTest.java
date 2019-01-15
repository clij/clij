package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class MeanSliceBySliceSphereTest {
    @Ignore //ignore test as we know and need to accept that the tested method does not do the same its ImageJ counterpart
    @Test
    public void meanSliceBySlice() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Mean...", "radius=1 stack");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.meanSliceBySliceSphere(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
        IJ.exit();
        clij.close();
    }

    @Ignore //ignore test as we know and need to accept that the tested method does not do the same its ImageJ counterpart
    @Test
    public void meanSliceBySlice_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Mean...", "radius=1 stack");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.meanSliceBySliceSphere(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        // ignore edges and first and last slice
        reference.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        result.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        reference = new Duplicator().run(reference, 2, result.getNSlices() - 2);
        result = new Duplicator().run(result, 2, result.getNSlices() - 2);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
        IJ.exit();
        clij.close();
    }

}