package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResliceRightTest {
    @Test
    public void resliceRight() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Reslice [/]...", "output=1.0 start=Right avoid");
        Thread.sleep(500);
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(new long[]{inputCL.getHeight(), inputCL.getDepth(), inputCL.getWidth()}, inputCL.getChannelDataType());

        Kernels.resliceRight(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));


    }

    @Test
    public void resliceRight_Buffers() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Reslice [/]...", "output=1.0 start=Right avoid");
        Thread.sleep(500);
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(new long[]{inputCL.getHeight(), inputCL.getDepth(), inputCL.getWidth()}, inputCL.getNativeType());

        Kernels.resliceRight(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));


    }

}