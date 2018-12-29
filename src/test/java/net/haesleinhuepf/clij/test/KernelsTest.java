package net.haesleinhuepf.clij.test;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import ij.*;
import ij.gui.NewImage;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import ij.plugin.ImageCalculator;
import ij.process.ImageProcessor;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
 * Test class for all OpenCL kernels accessible via the Kernels class
 *
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * March 2018
 */
public class KernelsTest {
    //static {
    //    LegacyInjector.preinit();
    //}

    ImagePlus testFlyBrain3D;
    ImagePlus testFlyBrain2D;
    ImagePlus testImp1;
    ImagePlus testImp2;
    ImagePlus testImp2D1;
    ImagePlus testImp2D2;
    ImagePlus mask3d;
    ImagePlus mask2d;
    CLIJ clij;

    @Before
    public void initTest() {
        testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");
        testFlyBrain2D = new Duplicator().run(testFlyBrain3D, 1, 1);


        testImp1 =
                NewImage.createImage("",
                        100,
                        100,
                        12,
                        16,
                        NewImage.FILL_BLACK);
        testImp2 =
                NewImage.createImage("",
                        100,
                        100,
                        12,
                        16,
                        NewImage.FILL_BLACK);
        mask3d =
                NewImage.createImage("",
                        100,
                        100,
                        12,
                        16,
                        NewImage.FILL_BLACK);

        for (int z = 0; z < 5; z++) {
            testImp1.setZ(z + 1);
            ImageProcessor ip1 = testImp1.getProcessor();
            ip1.set(5, 5, 1);
            ip1.set(6, 6, 1);
            ip1.set(7, 7, 1);

            testImp2.setZ(z + 1);
            ImageProcessor ip2 = testImp2.getProcessor();
            ip2.set(7, 5, 2);
            ip2.set(6, 6, 2);
            ip2.set(5, 7, 2);

            if (z < 3) {
                mask3d.setZ(z + 3);
                ImageProcessor ip3 = mask3d.getProcessor();
                ip3.set(2, 2, 1);
                ip3.set(2, 3, 1);
                ip3.set(2, 4, 1);
                ip3.set(3, 2, 1);
                ip3.set(3, 3, 1);
                ip3.set(3, 4, 1);
                ip3.set(4, 2, 1);
                ip3.set(4, 3, 1);
                ip3.set(4, 4, 1);
            }
        }

        testImp2D1 = new Duplicator().run(testImp1, 1, 1);
        testImp2D2 = new Duplicator().run(testImp1, 1, 1);
        mask2d = new Duplicator().run(mask3d, 3, 3);

        if (clij == null) {
            clij = CLIJ.getInstance();
            //new CLIJ("Geforce");
            //new CLIJ("HD");
        }
    }

    @Test
    public void differenceOfGaussian3d() {
        // do operation with ImageJ
        System.out.println("Todo: implement test for DoG");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp1, ClearCLImage.class);

        Kernels.differenceOfGaussian(clij, src, dst, 6, 1.1f, 3.3f);

        src.close();
        dst.close();
    }

    @Test
    public void differenceOfGaussian3dSliceBySlice() {
        // do operation with ImageJ
        System.out.println("Todo: implement test for DoG slice by slice");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp1, ClearCLImage.class);

        Kernels.differenceOfGaussianSliceBySlice(clij, src, dst, 6, 1.1f, 3.3f);

        src.close();
        dst.close();
    }


    @Test
    public void differenceOfGaussian2d() {
        // do operation with ImageJ
        System.out.println("Todo: implement test for DoG");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp2D1, ClearCLImage.class);

        Kernels.differenceOfGaussian(clij, src, dst, 6, 1.1f, 3.3f);

        src.close();
        dst.close();
    }


    @Test
    public void downsample3d() throws InterruptedException {
        // do operation with ImageJ
        new ImageJ(); // the menu command 'Scale...' can only be executed successfully if the ImageJ UI is visible; apparently
        testImp1.show();
        IJ.run(testImp1, "Scale...", "x=0.5 y=0.5 z=0.5 width=512 height=1024 depth=5 interpolation=None process create");
        ImagePlus downsampled = IJ.getImage();


        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst =
                clij.createCLImage(new long[]{src.getWidth() / 2,
                                src.getHeight() / 2,
                                (long) (src.getDepth() - 0.5) / 2},
                        src.getChannelDataType());


        Kernels.downsample(clij, src, dst, 0.5f, 0.5f, 0.5f);

        ImagePlus downsampledCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(downsampled, downsampledCL, 1.0));
    }

    @Test
    public void downsample3d_Buffers() throws InterruptedException {
        // do operation with ImageJ
        new ImageJ(); // the menu command 'Scale...' can only be executed successfully if the ImageJ UI is visible; apparently
        testImp1.show();
        IJ.run(testImp1, "Scale...", "x=0.5 y=0.5 z=0.5 width=512 height=1024 depth=5 interpolation=None process create");
        ImagePlus downsampled = IJ.getImage();


        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer dst =
                clij.createCLBuffer(new long[]{src.getWidth() / 2,
                                src.getHeight() / 2,
                                (long) (src.getDepth() - 0.5) / 2},
                        src.getNativeType());


        Kernels.downsample(clij, src, dst, 0.5f, 0.5f, 0.5f);

        ImagePlus downsampledCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(downsampled, downsampledCL, 1.0));
    }

    @Test
    public void maximum2d() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Maximum...", "radius=1");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.maximum(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void maximum2d_Buffers() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Maximum...", "radius=1");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.maximum(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        // ignore edges
        reference.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        result.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        reference = new Duplicator().run(reference);
        result = new Duplicator().run(result);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void maximum3d() {
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Maximum 3D...", "x=1 y=1 z=1");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.maximum(clij, inputCL, outputCL, 3, 3, 3);

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
    }

    @Test
    public void maximum3d_Buffers() {
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Maximum 3D...", "x=1 y=1 z=1");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.maximum(clij, inputCL, outputCL, 3, 3, 3);

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
    }

    @Test
    public void maximumSliceBySlice() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Maximum...", "radius=1 stack");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.maximumSliceBySlice(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void maximumSliceBySlice_Buffer() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Maximum...", "radius=1 stack");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.maximumSliceBySlice(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        // ignore edges
        reference.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        result.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        reference = new Duplicator().run(reference);
        result = new Duplicator().run(result);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void maxPixelWise3d() {
        System.out.println("Todo: implement test for maxPixelwise3d");
    }

    @Test
    public void minPixelWise3d() {
        System.out.println("Todo: implement test for minPixelwise3d");
    }

    @Test
    public void maxProjection() throws InterruptedException {
        // do operation with ImageJ
        ImagePlus
                maxProjection =
                NewImage.createShortImage("",
                        testImp1.getWidth(),
                        testImp2.getHeight(),
                        1,
                        NewImage.FILL_BLACK);
        ImageProcessor ipMax = maxProjection.getProcessor();

        ImagePlus testImp1copy = new Duplicator().run(testImp1);
        for (int z = 0; z < testImp1copy.getNSlices(); z++) {
            testImp1copy.setZ(z + 1);
            ImageProcessor ip = testImp1copy.getProcessor();
            for (int x = 0; x < testImp1copy.getWidth(); x++) {
                for (int y = 0; y < testImp1copy.getHeight(); y++) {
                    float value = ip.getf(x, y);
                    if (value > ipMax.getf(x, y)) {
                        ipMax.setf(x, y, value);
                    }
                }
            }
        }

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage
                dst =
                clij.createCLImage(new long[]{src.getWidth(),
                                src.getHeight()},
                        src.getChannelDataType());

        Kernels.maxProjection(clij, src, dst);

        ImagePlus maxProjectionCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(maxProjection,
                maxProjectionCL));

        src.close();
        dst.close();
    }

    @Test
    public void maxProjection_Buffers() throws InterruptedException {
        // do operation with ImageJ
        ImagePlus
                maxProjection =
                NewImage.createShortImage("",
                        testImp1.getWidth(),
                        testImp2.getHeight(),
                        1,
                        NewImage.FILL_BLACK);
        ImageProcessor ipMax = maxProjection.getProcessor();

        ImagePlus testImp1copy = new Duplicator().run(testImp1);
        for (int z = 0; z < testImp1copy.getNSlices(); z++) {
            testImp1copy.setZ(z + 1);
            ImageProcessor ip = testImp1copy.getProcessor();
            for (int x = 0; x < testImp1copy.getWidth(); x++) {
                for (int y = 0; y < testImp1copy.getHeight(); y++) {
                    float value = ip.getf(x, y);
                    if (value > ipMax.getf(x, y)) {
                        ipMax.setf(x, y, value);
                    }
                }
            }
        }

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer
                dst =
                clij.createCLBuffer(new long[]{src.getWidth(),
                                src.getHeight()},
                        src.getNativeType());

        Kernels.maxProjection(clij, src, dst);

        ImagePlus maxProjectionCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(maxProjection,
                maxProjectionCL));

        src.close();
        dst.close();
    }

    @Test
    public void mean2d() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Mean...", "radius=1");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.mean(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }


    @Test
    public void mean2d_Buffers() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Mean...", "radius=1");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.mean(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        // ignore edges
        reference.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        result.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        reference = new Duplicator().run(reference);
        result = new Duplicator().run(result);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void mean3d() {
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Mean 3D...", "x=1 y=1 z=1");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.mean(clij, inputCL, outputCL, 3, 3, 3);

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
    }

    @Test
    public void mean3d_Buffers() {
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Mean 3D...", "x=1 y=1 z=1");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        //Kernels.copy(clij, inputCL, outputCL);

        Kernels.mean(clij, inputCL, outputCL, 3, 3, 3);
        //Kernels.mean(clij, inputCL, outputCL, 3, 3, 3);

        //if (true) return;
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
    }

    @Test
    public void meanSliceBySlice() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Mean...", "radius=1 stack");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.meanSliceBySlice(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void meanSliceBySlice_Buffers() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Mean...", "radius=1 stack");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.meanSliceBySlice(clij, inputCL, outputCL, 3, 3);

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
    }

    @Test
    public void median2d() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Median...", "radius=1");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.median(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void median2d_Buffers() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Median...", "radius=1");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.median(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void median3d() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Median 3D...", "x=1 y=1 z=1");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.median(clij, inputCL, outputCL, 3, 3, 3);

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
    }

    @Test
    public void median3d_Buffers() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Median 3D...", "x=1 y=1 z=1");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.median(clij, inputCL, outputCL, 3, 3, 3);

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
    }

    @Test
    public void medianSliceBySlice() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Median...", "radius=1 stack");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.medianSliceBySlice(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void medianSliceBySlice_Buffer() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Median...", "radius=1 stack");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.medianSliceBySlice(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        // ignore edges
        reference.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        result.setRoi(new Roi(1, 1, reference.getWidth() - 2, reference.getHeight() - 2));
        reference = new Duplicator().run(reference);
        result = new Duplicator().run(result);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void minimum2d() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Minimum...", "radius=1");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.minimum(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }


    @Test
    public void minimum2d_Buffers() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 20, 20);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Minimum...", "radius=1");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.minimum(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void minimum3d() {
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Minimum 3D...", "x=1 y=1 z=1");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.minimum(clij, inputCL, outputCL, 3, 3, 3);

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
    }

    @Test
    public void minimum3d_Buffer() {
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Minimum 3D...", "x=1 y=1 z=1");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.minimum(clij, inputCL, outputCL, 3, 3, 3);

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
    }

    @Test
    public void minimum2dSeparable() throws InterruptedException {
        ClearCLImage src = clij.convert(testFlyBrain2D, ClearCLImage.class);
        ClearCLImage minimumCL = clij.createCLImage(src);
        ClearCLImage minimumSepCL = clij.createCLImage(src);

        Kernels.minimum(clij, src, minimumCL, 7,7);
        Kernels.minimumSeparable(clij, src, minimumSepCL, 3, 3, 3);

        ImagePlus minimumImp = clij.convert(minimumCL, ImagePlus.class);
        ImagePlus minimumSepImp = clij.convert(minimumSepCL, ImagePlus.class);

        // ignore edges
        minimumImp.setRoi(new Roi(1, 1, minimumImp.getWidth() - 2, minimumImp.getHeight() - 2));
        minimumSepImp.setRoi(new Roi(1, 1, minimumSepImp.getWidth() - 2, minimumSepImp.getHeight() - 2));
        minimumImp = new Duplicator().run(minimumImp);
        minimumSepImp = new Duplicator().run(minimumSepImp);

        src.close();
        minimumCL.close();
        minimumSepCL.close();

        assertTrue(TestUtilities.compareImages(minimumSepImp, minimumImp, 2.0));
    }

    @Test
    public void minimum2dSeparable_Buffer() throws InterruptedException {
        ClearCLBuffer src = clij.convert(testFlyBrain2D, ClearCLBuffer.class);
        ClearCLBuffer minimumCL = clij.createCLBuffer(src);
        ClearCLBuffer minimumSepCL = clij.createCLBuffer(src);

        Kernels.minimum(clij, src, minimumCL, 3,3);
        Kernels.minimumSeparable(clij, src, minimumSepCL, 1, 1, 1);

        ImagePlus minimumImp = clij.convert(minimumCL, ImagePlus.class);
        ImagePlus minimumSepImp = clij.convert(minimumSepCL, ImagePlus.class);


        // ignore edges
        minimumImp.setRoi(new Roi(1, 1, minimumImp.getWidth() - 2, minimumImp.getHeight() - 2));
        minimumSepImp.setRoi(new Roi(1, 1, minimumSepImp.getWidth() - 2, minimumSepImp.getHeight() - 2));
        minimumImp = new Duplicator().run(minimumImp);
        minimumSepImp = new Duplicator().run(minimumSepImp);

        src.close();
        minimumCL.close();
        minimumSepCL.close();

        assertTrue(TestUtilities.compareImages(minimumSepImp, minimumImp, 2.0));
    }

    @Test
    public void minimum3dSeparable() throws InterruptedException {
        ClearCLImage src = clij.convert(testFlyBrain3D, ClearCLImage.class);;
        ClearCLImage minimumCL = clij.createCLImage(src);
        ClearCLImage minimumSepCL = clij.createCLImage(src);

        Kernels.minimum(clij, src, minimumCL, 7,7, 7);
        Kernels.minimumSeparable(clij, src, minimumSepCL, 3, 3, 3);

        ImagePlus minimumImp = clij.convert(minimumCL, ImagePlus.class);
        ImagePlus minimumSepImp = clij.convert(minimumSepCL, ImagePlus.class);

        // ignore edges
        minimumImp.setRoi(new Roi(1, 1, minimumImp.getWidth() - 2, minimumImp.getHeight() - 2));
        minimumSepImp.setRoi(new Roi(1, 1, minimumSepImp.getWidth() - 2, minimumSepImp.getHeight() - 2));
        minimumImp = new Duplicator().run(minimumImp);
        minimumSepImp = new Duplicator().run(minimumSepImp);

        src.close();
        minimumCL.close();
        minimumSepCL.close();

        System.out.println("Todo: minimum uses a elipsoid as mask while minimumSeparable uses a cuboid");
        assertTrue(TestUtilities.compareImages(minimumSepImp, minimumImp, 2.0));
    }

    @Test
    public void minimum3dSeparable_Buffer() throws InterruptedException {
        ClearCLBuffer src = clij.convert(testFlyBrain3D, ClearCLBuffer.class);
        ClearCLBuffer minimumCL = clij.createCLBuffer(src);
        ClearCLBuffer minimumSepCL = clij.createCLBuffer(src);

        Kernels.minimum(clij, src, minimumCL, 3,3, 3);
        Kernels.minimumSeparable(clij, src, minimumSepCL, 1, 1, 1);

        ImagePlus minimumImp = clij.convert(minimumCL, ImagePlus.class);
        ImagePlus minimumSepImp = clij.convert(minimumSepCL, ImagePlus.class);


        // ignore edges
        minimumImp.setRoi(new Roi(1, 1, minimumImp.getWidth() - 2, minimumImp.getHeight() - 2));
        minimumSepImp.setRoi(new Roi(1, 1, minimumSepImp.getWidth() - 2, minimumSepImp.getHeight() - 2));
        minimumImp = new Duplicator().run(minimumImp);
        minimumSepImp = new Duplicator().run(minimumSepImp);

        src.close();
        minimumCL.close();
        minimumSepCL.close();

        System.out.println("Todo: minimum uses a elipsoid as mask while minimumSeparable uses a cuboid");
        assertTrue(TestUtilities.compareImages(minimumSepImp, minimumImp, 2.0));
    }

    @Test
    public void minimumSliceBySlice() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Minimum...", "radius=1 stack");

        // do operation with CLIJ
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(inputCL);

        Kernels.minimumSliceBySlice(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void minimumSliceBySlice_Buffer() {

        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        IJ.run(testImage, "32-bit", "");

        // do operation with ImageJ
        ImagePlus reference = new Duplicator().run(testImage);
        IJ.run(reference, "Minimum...", "radius=1 stack");

        // do operation with CLIJ
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(inputCL);

        Kernels.minimumSliceBySlice(clij, inputCL, outputCL, 3, 3);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //new ImageJ();
        //clij.show(inputCL, "inp");
        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();
        assertTrue(TestUtilities.compareImages(reference, result, 0.001));
    }

    @Test
    public void multiplyPixelwise3d() {
        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage src1 = clij.convert(testImp2, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp1, ClearCLImage.class);

        Kernels.multiplyPixelwise(clij, src, src1, dst);

        ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
    }

    @Ignore
    @Test
    public void multiplyPixelwise3dathousandtimes() {
        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        for (int i = 0; i < 1000; i++) {
            // do operation with ClearCL
            ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
            ClearCLImage src1 = clij.convert(testImp2, ClearCLImage.class);
            ClearCLImage dst = clij.convert(testImp1, ClearCLImage.class);

            Kernels.multiplyPixelwise(clij, src, src1, dst);

            ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

            assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

            src.close();
            src1.close();
            dst.close();
        }
    }

    @Test
    public void multiplyPixelwise3d_Buffers() {
        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clij.convert(testImp2, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.convert(testImp1, ClearCLBuffer.class);

        Kernels.multiplyPixelwise(clij, src, src1, dst);

        ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
    }

    @Ignore
    @Test
    public void multiplyPixelwise3d_Buffers_athousandtimes() {
        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create stack",
                        testImp1,
                        testImp2);

        for (int i = 0; i < 1000; i++) {
            // do operation with ClearCL
            ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
            ClearCLBuffer src1 = clij.convert(testImp2, ClearCLBuffer.class);
            ClearCLBuffer dst = clij.convert(testImp1, ClearCLBuffer.class);

            Kernels.multiplyPixelwise(clij, src, src1, dst);

            ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

            assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

            src.close();
            src1.close();
            dst.close();
        }
    }


    @Test
    public void multiplyPixelwise2d() {
        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D2);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage src1 = clij.convert(testImp2D2, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp2D1, ClearCLImage.class);

        Kernels.multiplyPixelwise(clij, src, src1, dst);

        ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
    }

    @Test
    public void multiplyPixelwise2d_Buffers() {
        // do operation with ImageJ
        ImagePlus
                multiplied =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D2);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clij.convert(testImp2D2, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.convert(testImp2D1, ClearCLBuffer.class);

        Kernels.multiplyPixelwise(clij, src, src1, dst);

        ImagePlus multipliedCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

        src.close();
        src1.close();
        dst.close();
    }

    @Test
    public void multiplyScalar3d() {
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

    @Test
    public void multiplyStackWithPlane() {
        // do operation with ImageJ
        System.out.println(
                "Todo: implement test for multiplyStackWithPlane");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage mask = clij.convert(testImp2, ClearCLImage.class);;
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.multiplyStackWithPlane(clij, src, mask, dst);

        mask.close();
        dst.close();

    }

    @Test
    public void multiplyStackWithPlane_Buffers() {
        // do operation with ImageJ
        System.out.println(
                "Todo: implement test for multiplyStackWithPlane");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer mask = clij.convert(testImp2, ClearCLBuffer.class);;
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.multiplyStackWithPlane(clij, src, mask, dst);

        mask.close();
        dst.close();

    }

    @Test
    public void power() {

        // do operation with ImageJ
        ImagePlus
                squared =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D1);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.power(clij, src, dst, 2.0f);

        ImagePlus squaredCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(squared, squaredCL));

        src.close();
        dst.close();


    }

    @Test
    public void power_Buffers() {

        // do operation with ImageJ
        ImagePlus
                squared =
                new ImageCalculator().run("Multiply create",
                        testImp2D1,
                        testImp2D1);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.power(clij, src, dst, 2.0f);

        ImagePlus squaredCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(squared, squaredCL));

        src.close();
        dst.close();


    }

    @Test
    public void resliceBottom() throws InterruptedException {

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Reslice [/]...", "output=1.0 start=Bottom avoid");
        Thread.sleep(500);
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(new long[]{inputCL.getWidth(), inputCL.getDepth(), inputCL.getHeight()}, inputCL.getChannelDataType());

        Kernels.resliceBottom(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));


    }

    @Test
    public void resliceBottom_Buffers() throws InterruptedException {

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Reslice [/]...", "output=1.0 start=Bottom avoid");
        Thread.sleep(500);
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(new long[]{inputCL.getWidth(), inputCL.getDepth(), inputCL.getHeight()}, inputCL.getNativeType());

        Kernels.resliceBottom(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));


    }

    @Test
    public void resliceLeft() throws InterruptedException {

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Reslice [/]...", "output=1.0 start=Left avoid");
        Thread.sleep(500);
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLImage inputCL = clij.convert(testImage, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(new long[]{inputCL.getHeight(), inputCL.getDepth(), inputCL.getWidth()}, inputCL.getChannelDataType());

        Kernels.resliceLeft(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));


    }

    @Test
    public void resliceLeft_Buffers() throws InterruptedException {

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Reslice [/]...", "output=1.0 start=Left avoid");
        Thread.sleep(500);
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLBuffer inputCL = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(new long[]{inputCL.getHeight(), inputCL.getDepth(), inputCL.getWidth()}, inputCL.getNativeType());

        Kernels.resliceLeft(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));


    }

    @Test
    public void resliceRight() throws InterruptedException {

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

    @Test
    public void resliceTop() throws InterruptedException {

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


    }

    @Test
    public void resliceTop_Buffers() throws InterruptedException {

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


    }


    @Test
    public void rotateLeft2d() throws InterruptedException {

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 10, 10);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D, 10, 10);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Left", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLImage inputCL = clij.convert(testImage2, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(new long[]{inputCL.getHeight(), inputCL.getWidth(), inputCL.getDepth()}, inputCL.getChannelDataType());

        Kernels.rotateLeft(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));
    }

    @Test
    public void rotateLeft2d_Buffers() throws InterruptedException {

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D, 10, 10);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D, 10, 10);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Left", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLBuffer inputCL = clij.convert(testImage2, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(new long[]{inputCL.getHeight(), inputCL.getWidth(), inputCL.getDepth()}, inputCL.getNativeType());

        Kernels.rotateLeft(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));
    }

    @Test
    public void rotateLeft3d() throws InterruptedException {

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Left", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLImage inputCL = clij.convert(testImage2, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(new long[]{inputCL.getHeight(), inputCL.getWidth(), inputCL.getDepth()}, inputCL.getChannelDataType());

        Kernels.rotateLeft(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));
    }

    @Test
    public void rotateLeft3d_Buffers() throws InterruptedException {

        testFlyBrain3D.setRoi(0, 0, 256, 128);
        ImagePlus testImage = new Duplicator().run(testFlyBrain3D);
        ImagePlus testImage2 = new Duplicator().run(testFlyBrain3D);
        testImage.show();

        // do operation with ImageJ
        new ImageJ();
        IJ.run(testImage, "Rotate 90 Degrees Left", "");
        ImagePlus reference = IJ.getImage();

        // do operation with OpenCL
        ClearCLBuffer inputCL = clij.convert(testImage2, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(new long[]{inputCL.getHeight(), inputCL.getWidth(), inputCL.getDepth()}, inputCL.getNativeType());

        Kernels.rotateLeft(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);

        //clij.show(reference, "ref");
        //clij.show(result, "res");
        //new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(reference, result));
    }

    @Test
    public void rotateRight2d() throws InterruptedException {

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


    @Test
    public void set3d() {
        ClearCLImage imageCL = clij.convert(mask3d, ClearCLImage.class);

        Kernels.set(clij, imageCL, 2f);

        double sum = Kernels.sumPixels(clij, imageCL);

        assertTrue(sum
                == imageCL.getWidth()
                * imageCL.getHeight()
                * imageCL.getDepth()
                * 2);

        imageCL.close();
    }

    @Test
    public void set3d_Buffers() {
        ClearCLBuffer imageCL = clij.convert(mask3d, ClearCLBuffer.class);

        Kernels.set(clij, imageCL, 2f);

        double sum = Kernels.sumPixels(clij, imageCL);

        assertTrue(sum
                == imageCL.getWidth()
                * imageCL.getHeight()
                * imageCL.getDepth()
                * 2);

        imageCL.close();
    }

    @Test
    public void set2d() {
        ClearCLImage imageCL = clij.convert(testImp2, ClearCLImage.class);;

        Kernels.set(clij, imageCL, 2f);

        double sum = Kernels.sumPixels(clij, imageCL);

        assertTrue(sum == imageCL.getWidth() * imageCL.getHeight() * 2);

        imageCL.close();
    }


    @Test
    public void set2d_Buffers() {
        ClearCLBuffer imageCL = clij.convert(testImp2, ClearCLBuffer.class);;

        Kernels.set(clij, imageCL, 2f);

        double sum = Kernels.sumPixels(clij, imageCL);

        assertTrue(sum == imageCL.getWidth() * imageCL.getHeight() * 2);

        imageCL.close();
    }

    @Test
    public void splitStack() {
        ClearCLImage clearCLImage = clij.convert(testFlyBrain3D, ClearCLImage.class);;
        ClearCLImage split1 = clij.createCLImage(new long[]{clearCLImage.getWidth(), clearCLImage.getHeight(), clearCLImage.getDepth() / 2}, clearCLImage.getChannelDataType());
        ClearCLImage split2 = clij.createCLImage(new long[]{clearCLImage.getWidth(), clearCLImage.getHeight(), clearCLImage.getDepth() / 2}, clearCLImage.getChannelDataType());

        Kernels.splitStack(clij, clearCLImage, split1, split2);

        assertTrue(Kernels.sumPixels(clij, split1) > 0);
        assertTrue(Kernels.sumPixels(clij, split2) > 0);
    }

    @Test
    public void splitStack_Buffers() {
        ClearCLBuffer clearCLImage = clij.convert(testFlyBrain3D, ClearCLBuffer.class);
        ClearCLBuffer split1 = clij.createCLBuffer(new long[]{clearCLImage.getWidth(), clearCLImage.getHeight(), clearCLImage.getDepth() / 2}, clearCLImage.getNativeType());
        ClearCLBuffer split2 = clij.createCLBuffer(new long[]{clearCLImage.getWidth(), clearCLImage.getHeight(), clearCLImage.getDepth() / 2}, clearCLImage.getNativeType());

        Kernels.splitStack(clij, clearCLImage, split1, split2);

        assertTrue(Kernels.sumPixels(clij, split1) > 0);
        assertTrue(Kernels.sumPixels(clij, split2) > 0);
    }

    @Test
    public void sumProjection() throws InterruptedException {
        // do operation with ImageJ
        ImagePlus
                sumProjection =
                NewImage.createShortImage("",
                        testImp1.getWidth(),
                        testImp2.getHeight(),
                        1,
                        NewImage.FILL_BLACK);
        ImageProcessor ipSum = sumProjection.getProcessor();

        ImagePlus testImp1copy = new Duplicator().run(testImp1);
        for (int z = 0; z < testImp1copy.getNSlices(); z++) {
            testImp1copy.setZ(z + 1);
            ImageProcessor ip = testImp1copy.getProcessor();
            for (int x = 0; x < testImp1copy.getWidth(); x++) {
                for (int y = 0; y < testImp1copy.getHeight(); y++) {
                    float value = ip.getf(x, y) + ipSum.getf(x, y);
                    ipSum.setf(x, y, value);
                }
            }
        }

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage
                dst =
                clij.createCLImage(new long[]{src.getWidth(),
                                src.getHeight()},
                        src.getChannelDataType());

        Kernels.sumProjection(clij, src, dst);

        ImagePlus sumProjectionCL = clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(sumProjection,
                sumProjectionCL));

        src.close();
        dst.close();
    }

        @Test
    public void sumPixelsSliceBySlice() {
        ClearCLImage maskCL = clij.convert(mask3d, ClearCLImage.class);

        double sum = Kernels.sumPixels(clij, maskCL);
        double[] sumSliceWise = Kernels.sumPixelsSliceBySlice(clij, maskCL);

        assertTrue(sum == new Sum().evaluate(sumSliceWise));

        maskCL.close();
    }

    @Test
    public void sumPixelsSliceBySlice_Buffers() {
        ClearCLBuffer maskCL = clij.convert(mask3d, ClearCLBuffer.class);

        double sum = Kernels.sumPixels(clij, maskCL);
        double[] sumSliceWise = Kernels.sumPixelsSliceBySlice(clij, maskCL);

        assertTrue(sum == new Sum().evaluate(sumSliceWise));

        maskCL.close();
    }

    @Test
    public void sumPixels3d() {
        ClearCLImage maskCL = clij.convert(mask3d, ClearCLImage.class);

        double sum = Kernels.sumPixels(clij, maskCL);

        assertTrue(sum == 27);

        maskCL.close();
    }

    @Test
    public void sumPixels3d_Buffers() {
        ClearCLBuffer maskCL = clij.convert(mask3d, ClearCLBuffer.class);

        double sum = Kernels.sumPixels(clij, maskCL);

        assertTrue(sum == 27);

        maskCL.close();
    }

    @Test
    public void sumPixels2d() {
        ImagePlus imp = NewImage.createByteImage("test", 10, 10, 1, NewImage.FILL_BLACK);
        imp.getProcessor().set(5,5, 1);
        imp.getProcessor().set(5,6, 2);
        imp.getProcessor().set(5,7, 3);

        ClearCLImage maskCL = clij.convert(imp, ClearCLImage.class);

        double sum = Kernels.sumPixels(clij, maskCL);

        assertTrue(sum == 6);

        maskCL.close();
    }

    @Test
    public void sumPixels2d_Buffers() {
        ImagePlus imp = NewImage.createByteImage("test", 10, 10, 1, NewImage.FILL_BLACK);
        imp.getProcessor().set(5,5, 1);
        imp.getProcessor().set(5,6, 2);
        imp.getProcessor().set(5,7, 3);

        ClearCLBuffer maskCL = clij.convert(imp, ClearCLBuffer.class);

        double sum = Kernels.sumPixels(clij, maskCL);

        assertTrue(sum == 6);

        maskCL.close();
    }


    @Test
    public void maxPixels2d() {
        ImagePlus imp = NewImage.createByteImage("test", 10, 10, 1, NewImage.FILL_BLACK);
        imp.getProcessor().set(5,5, 1);
        imp.getProcessor().set(5,6, 2);
        imp.getProcessor().set(5,7, 3);

        ClearCLImage maskCL = clij.convert(imp, ClearCLImage.class);

        double sum = Kernels.maximumOfAllPixels(clij, maskCL);

        assertTrue(sum == 3);

        maskCL.close();
    }

    @Test
    public void maxPixels2d_Buffers() {
        ImagePlus imp = NewImage.createByteImage("test", 10, 10, 1, NewImage.FILL_BLACK);
        imp.getProcessor().set(5,5, 1);
        imp.getProcessor().set(5,6, 2);
        imp.getProcessor().set(5,7, 3);

        ClearCLBuffer maskCL = clij.convert(imp, ClearCLBuffer.class);

        double sum = Kernels.maximumOfAllPixels(clij, maskCL);

        assertTrue(sum == 3);

        maskCL.close();
    }

    @Test
    public void tenengradWeights() {
        System.out.println("Todo: implement test for Tenengrad weights");
    }

    @Test
    public void tenengradFusion() {
        System.out.println("Todo: implement test for Tenengrad fusion");
    }

    @Test
    public void threshold3d() {
        // do operation with ImageJ
        ImagePlus thresholded = new Duplicator().run(testImp2);
        Prefs.blackBackground = false;
        IJ.setRawThreshold(thresholded, 2, 65535, null);
        IJ.run(thresholded,
                "Convert to Mask",
                "method=Default background=Dark");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.threshold(clij, src, dst, 2f);
        Kernels.multiplyScalar(clij, dst, src, 255f);

        ImagePlus thresholdedCL = clij.convert(src, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(thresholded,
                thresholdedCL));

        src.close();
        dst.close();
    }

    @Test
    public void threshold3d_Buffers() {
        ImagePlus testImage = new Duplicator().run(testImp2);
        IJ.run(testImage, "32-bit", "");
        // do operation with ImageJ
        ImagePlus thresholded = new Duplicator().run(testImage);
        Prefs.blackBackground = false;
        IJ.setRawThreshold(thresholded, 2, 65535, null);
        IJ.run(thresholded,
                "Convert to Mask",
                "method=Default background=Dark");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.threshold(clij, src, dst, 2f);
        Kernels.multiplyScalar(clij, dst, src, 255f);

        ImagePlus thresholdedCL = clij.convert(src, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(thresholded,
                thresholdedCL));

        src.close();
        dst.close();
    }

    @Test
    public void threshold2d() {
        // do operation with ImageJ
        ImagePlus thresholded = new Duplicator().run(testImp2D2);
        Prefs.blackBackground = false;
        IJ.setRawThreshold(thresholded, 2, 65535, null);
        IJ.run(thresholded,
                "Convert to Mask",
                "method=Default background=Dark");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D2, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp2D2, ClearCLImage.class);

        Kernels.threshold(clij, src, dst, 2f);
        Kernels.multiplyScalar(clij, dst, src, 255f);

        ImagePlus thresholdedCL = clij.convert(src, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(thresholded,
                thresholdedCL));

        src.close();
        dst.close();
    }


    @Test
    public void threshold2d_Buffer() {
        // do operation with ImageJ
        ImagePlus thresholded = new Duplicator().run(testImp2D2);
        Prefs.blackBackground = false;
        IJ.setRawThreshold(thresholded, 2, 65535, null);
        IJ.run(thresholded,
                "Convert to Mask",
                "method=Default background=Dark");

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D2, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.convert(testImp2D2, ClearCLBuffer.class);

        Kernels.threshold(clij, src, dst, 2f);
        Kernels.multiplyScalar(clij, dst, src, 255f);

        ImagePlus thresholdedCL = clij.convert(src, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(thresholded,
                thresholdedCL));

        src.close();
        dst.close();
    }

    @Test
    public void threshold2d_Buffer_blobs() throws InterruptedException {
        ImagePlus testImage = IJ.openImage("src/test/resources/blobs.gif");
        //new Duplicator().run(testImp2D2);
        IJ.run(testImage, "8-bit", "");

        // do operation with ImageJ
        ImagePlus thresholded = new Duplicator().run(testImage);
        Prefs.blackBackground = false;
        IJ.setRawThreshold(thresholded, 127, 65535, null);
        IJ.run(thresholded,
                "Convert to Mask",
                "method=Default background=Dark");


        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);


        ByteBuffer buffer = ByteBuffer.allocate((int) src.getSizeInBytes());
        //src.writeTo(buffer, true);
        //System.out.println("src " + Arrays.toString(buffer.array()));

        Kernels.threshold(clij, src, dst, 128f);
        Kernels.copy(clij, dst, src);
        //Kernels.multiplyScalar(clij, dst, src, 255f);

        src.writeTo(buffer, true);
        System.out.println("src " + Arrays.toString(buffer.array()));

        ImagePlus thresholdedCL = clij.convert(src, ImagePlus.class);

        clij.show(thresholded, "thresholded");
        clij.show(thresholdedCL, "thresholded_cl");
        Thread.sleep(5000);
        assertTrue(TestUtilities.compareImages(thresholded,
                thresholdedCL));


        src.close();
        dst.close();
    }
}