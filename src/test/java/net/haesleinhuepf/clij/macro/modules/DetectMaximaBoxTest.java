package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.Duplicator;
import ij.plugin.filter.MaximumFinder;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class DetectMaximaBoxTest {

    @Test
    public void detectMaxima3d() {
        CLIJ clij = CLIJ.getInstance();

        // do operation with ImageJ
        ImagePlus
                spotsImage =
                NewImage.createImage("",
                        100,
                        100,
                        3,
                        16,
                        NewImage.FILL_BLACK);

        spotsImage.setZ(2);
        ImageProcessor ip1 = spotsImage.getProcessor();
        ip1.set(50, 50, 10);
        ip1.set(60, 60, 10);
        ip1.set(70, 70, 10);

        //spotsImage.show();
        //IJ.run(spotsImage, "Find Maxima...", "noise=2 output=[Single Points]");

        ByteProcessor
                byteProcessor =
                new MaximumFinder().findMaxima(spotsImage.getProcessor(),
                        2,
                        MaximumFinder.SINGLE_POINTS,
                        true);
        ImagePlus maximaImp = new ImagePlus("A", byteProcessor);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(spotsImage, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.detectOptima(clij, src, dst, 1, true);
        ImagePlus maximaFromCL = clij.convert(dst, ImagePlus.class);
        maximaFromCL = new Duplicator().run(maximaFromCL, 2, 2);

        IJ.run(maximaImp, "Divide...", "value=255");

        assertTrue(TestUtilities.compareImages(maximaImp, maximaFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void detectMaxima3d_Buffers() {
        CLIJ clij = CLIJ.getInstance();

        // do operation with ImageJ
        ImagePlus
                spotsImage =
                NewImage.createImage("",
                        100,
                        100,
                        3,
                        16,
                        NewImage.FILL_BLACK);

        spotsImage.setZ(2);
        ImageProcessor ip1 = spotsImage.getProcessor();
        ip1.set(50, 50, 10);
        ip1.set(60, 60, 10);
        ip1.set(70, 70, 10);

        //spotsImage.show();
        //IJ.run(spotsImage, "Find Maxima...", "noise=2 output=[Single Points]");

        ByteProcessor
                byteProcessor =
                new MaximumFinder().findMaxima(spotsImage.getProcessor(),
                        2,
                        MaximumFinder.SINGLE_POINTS,
                        true);
        ImagePlus maximaImp = new ImagePlus("A", byteProcessor);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(spotsImage, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.detectOptima(clij, src, dst, 1, true);
        ImagePlus maximaFromCL = clij.convert(dst, ImagePlus.class);
        maximaFromCL = new Duplicator().run(maximaFromCL, 2, 2);

        IJ.run(maximaImp, "Divide...", "value=255");

        assertTrue(TestUtilities.compareImages(maximaImp, maximaFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }


    @Test
    public void detectMaxima2d() {
        CLIJ clij = CLIJ.getInstance();

        // do operation with ImageJ
        ImagePlus
                spotsImage =
                NewImage.createImage("",
                        100,
                        100,
                        1,
                        16,
                        NewImage.FILL_BLACK);

        spotsImage.setZ(1);
        ImageProcessor ip1 = spotsImage.getProcessor();
        ip1.set(50, 50, 10);
        ip1.set(60, 60, 10);
        ip1.set(70, 70, 10);

        //spotsImage.show();
        //IJ.run(spotsImage, "Find Maxima...", "noise=2 output=[Single Points]");

        ByteProcessor
                byteProcessor =
                new MaximumFinder().findMaxima(spotsImage.getProcessor(),
                        2,
                        MaximumFinder.SINGLE_POINTS,
                        true);
        ImagePlus maximaImp = new ImagePlus("A", byteProcessor);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(spotsImage, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        Kernels.detectOptima(clij, src, dst, 1, true);
        ImagePlus maximaFromCL = clij.convert(dst, ImagePlus.class);
        //    maximaFromCL = new Duplicator().run(maximaFromCL, 2, 2);

        IJ.run(maximaImp, "Divide...", "value=255");

        assertTrue(TestUtilities.compareImages(maximaImp, maximaFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void detectMaxima2d_Buffers() {
        CLIJ clij = CLIJ.getInstance();

        // do operation with ImageJ
        ImagePlus
                spotsImage =
                NewImage.createImage("",
                        100,
                        100,
                        1,
                        16,
                        NewImage.FILL_BLACK);

        spotsImage.setZ(1);
        ImageProcessor ip1 = spotsImage.getProcessor();
        ip1.set(50, 50, 10);
        ip1.set(60, 60, 10);
        ip1.set(70, 70, 10);

        //spotsImage.show();
        //IJ.run(spotsImage, "Find Maxima...", "noise=2 output=[Single Points]");

        ByteProcessor
                byteProcessor =
                new MaximumFinder().findMaxima(spotsImage.getProcessor(),
                        2,
                        MaximumFinder.SINGLE_POINTS,
                        true);
        ImagePlus maximaImp = new ImagePlus("A", byteProcessor);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(spotsImage, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        Kernels.detectOptima(clij, src, dst, 1, true);
        ImagePlus maximaFromCL = clij.convert(dst, ImagePlus.class);
        //    maximaFromCL = new Duplicator().run(maximaFromCL, 2, 2);

        IJ.run(maximaImp, "Divide...", "value=255");

        assertTrue(TestUtilities.compareImages(maximaImp, maximaFromCL));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }


}