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

public class RotateRightTest {

    @Test
    public void rotateRight2d() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 10, 10);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D, 10, 10);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Right", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLImage inputCL = clij.convert(testImage2, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(new long[]{inputCL.getHeight(), inputCL.getWidth(), inputCL.getDepth()}, inputCL.getChannelDataType());

        Kernels.rotateRight(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));
    }

    @Test
    public void rotateRight2d_Buffers() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 10, 10);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D, 10, 10);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Right", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLBuffer inputCL = clij.convert(testImage2, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(new long[]{inputCL.getHeight(), inputCL.getWidth(), inputCL.getDepth()}, inputCL.getNativeType());

        Kernels.rotateRight(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));
    }

    @Test
    public void rotateRight3d() throws InterruptedException {

        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Right", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLImage inputCL = clij.convert(testImage2, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(new long[]{inputCL.getHeight(), inputCL.getWidth(), inputCL.getDepth()}, inputCL.getChannelDataType());

        Kernels.rotateRight(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));
    }

    @Test
    public void rotateRight3d_Buffers() throws InterruptedException {

        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Right", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLBuffer inputCL = clij.convert(testImage2, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(new long[]{inputCL.getHeight(), inputCL.getWidth(), inputCL.getDepth()}, inputCL.getNativeType());

        Kernels.rotateRight(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));
    }

}