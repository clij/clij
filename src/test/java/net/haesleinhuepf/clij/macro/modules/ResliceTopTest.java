package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResliceTopTest {
    @Test
    public void resliceTop() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Reslice [/]...", "output=1.0 start=Top avoid");
        Thread.sleep(500);
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(new long[]{inputCL.getWidth(), inputCL.getDepth(), inputCL.getHeight()}, inputCL.getChannelDataType());

        Kernels.resliceTop(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));

        IJ.exit();
        clij.close();
    }

    @Test
    public void resliceTop_Buffers() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Reslice [/]...", "output=1.0 start=Top avoid");
        Thread.sleep(500);
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(new long[]{inputCL.getWidth(), inputCL.getDepth(), inputCL.getHeight()}, inputCL.getNativeType());

        Kernels.resliceTop(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));

        IJ.exit();
        clij.close();
    }

}