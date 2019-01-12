package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Blur3DFastTest {

    @Test
    public void blur3dSeparable() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        ClearCLImage src = clij.convert(testFlyBrain3D, ClearCLImage.class);;
        ClearCLImage dstBlur = clij.createCLImage(src);
        ClearCLImage dstBlurSeparable = clij.createCLImage(src);

        Kernels.blur(clij, src, dstBlur, 9, 9, 9, 2f, 2f, 2f);
        Kernels.blurFast(clij, src, dstBlurSeparable, 2f, 2f, 2f);

        //ClearCLBuffer srcB = clij.convert(testFlyBrain3D, ClearCLBuffer.class);
        //ClearCLBuffer dstBlurSeparableB = clij.createCLBuffer(srcB);
        //Kernels.blurFast(clij, srcB, dstBlurSeparableB, 2, 2, 2);

        ImagePlus gaussBlur = clij.convert(dstBlur, ImagePlus.class);
        ImagePlus gaussBlurSeparable = clij.convert(dstBlurSeparable, ImagePlus.class);
        //ImagePlus gaussBlurSeparableB = clij.converter(dstBlurSeparableB).getImagePlus();

        src.close();
        dstBlur.close();
        dstBlurSeparable.close();
        //srcB.close();
        //dstBlurSeparableB.close();

        assertTrue(TestUtilities.compareImages(gaussBlurSeparable, gaussBlur, 2.0));
        //assertTrue(TestUtilities.compareImages(gaussBlurSeparable, gaussBlurSeparableB, 2.0));
        IJ.exit();
        clij.close();
    }

    @Test
    public void blur3dSeparable_Buffer() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        ClearCLBuffer src = clij.convert(testFlyBrain3D, ClearCLBuffer.class);
        ClearCLBuffer dstBlur = clij.createCLBuffer(src);
        ClearCLBuffer dstBlurSeparable = clij.createCLBuffer(src);

        Kernels.blur(clij, src, dstBlur, 9, 9, 9, 2f, 2f, 2f);
        Kernels.blurFast(clij, src, dstBlurSeparable, 2f, 2f, 2f);

        //ClearCLBuffer srcB = clij.convert(testFlyBrain3D, ClearCLBuffer.class);
        //ClearCLBuffer dstBlurSeparableB = clij.createCLBuffer(srcB);
        //Kernels.blurFast(clij, srcB, dstBlurSeparableB, 2, 2, 2);

        ImagePlus gaussBlur = clij.convert(dstBlur, ImagePlus.class);
        ImagePlus gaussBlurSeparable = clij.convert(dstBlurSeparable, ImagePlus.class);
        //ImagePlus gaussBlurSeparableB = clij.converter(dstBlurSeparableB).getImagePlus();

        src.close();
        dstBlur.close();
        dstBlurSeparable.close();
        //srcB.close();
        //dstBlurSeparableB.close();

        assertTrue(TestUtilities.compareImages(gaussBlurSeparable, gaussBlur, 2.0));
        //assertTrue(TestUtilities.compareImages(gaussBlurSeparable, gaussBlurSeparableB, 2.0));
        IJ.exit();
        clij.close();
    }

}