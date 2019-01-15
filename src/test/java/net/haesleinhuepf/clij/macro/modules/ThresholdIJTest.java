package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.Prefs;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractMacroPluginTest;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class ThresholdIJTest extends AbstractMacroPluginTest {
    @Test
    public void testIfIdentialWithImageJ(){
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImage = IJ.openImage("src/test/resources/blobs.gif");
        Double threshold = 127.0;

        ClearCLBuffer bufferIn = clij.convert(testImage, ClearCLBuffer.class);
        ClearCLBuffer bufferOutCL = clij.createCLBuffer(bufferIn);
        ClearCLBuffer bufferOutIJ = clij.createCLBuffer(bufferIn);

        Object[] argsCL = {bufferIn, bufferOutCL, threshold};
        makeThresholdIJ(clij, argsCL).executeCL();

        Object[] argsIJ = {bufferIn, bufferOutIJ, threshold};
        makeThresholdIJ(clij, argsIJ).executeIJ();

        //clij.show(bufferOutCL, "cl " + bufferOutCL);
        //clij.show(bufferOutIJ, "ij");
        //new WaitForUserDialog("wait").show();

        ClearCLBuffer bufferOutCLBinary = clij.createCLBuffer(bufferIn);
        ClearCLBuffer bufferOutIJBinary = clij.createCLBuffer(bufferIn);

        Kernels.threshold(clij, bufferOutCL, bufferOutCLBinary, 1f);
        Kernels.threshold(clij, bufferOutIJ, bufferOutIJBinary, 1f);

        assertTrue(clBuffersEqual(clij, bufferOutIJBinary, bufferOutCLBinary));
        bufferIn.close();
        bufferOutCL.close();
        bufferOutIJ.close();
        bufferOutCLBinary.close();
        bufferOutIJBinary.close();
        IJ.exit();
        clij.close();
    }

    private ThresholdIJ makeThresholdIJ(CLIJ clij, Object[] args) {
        ThresholdIJ thresholdIJ = new ThresholdIJ();
        thresholdIJ.setClij(clij);
        thresholdIJ.setArgs(args);
        return thresholdIJ;
    }


    @Test
    public void threshold3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);

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
        Kernels.multiplyImageAndScalar(clij, dst, src, 255f);

        ImagePlus thresholdedCL = clij.convert(src, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(thresholded,
                thresholdedCL));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void threshold3d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 1, 100);
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
        Kernels.multiplyImageAndScalar(clij, dst, src, 255f);

        ImagePlus thresholdedCL = clij.convert(src, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(thresholded,
                thresholdedCL));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void threshold2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
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
        Kernels.multiplyImageAndScalar(clij, dst, src, 255f);

        ImagePlus thresholdedCL = clij.convert(src, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(thresholded,
                thresholdedCL));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }


    @Test
    public void threshold2d_Buffer() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 1, 100);
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
        Kernels.multiplyImageAndScalar(clij, dst, src, 255f);

        ImagePlus thresholdedCL = clij.convert(src, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(thresholded,
                thresholdedCL));

        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void threshold2d_Buffer_blobs() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
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

        Kernels.threshold(clij, src, dst, 128f);
        Kernels.copy(clij, dst, src);
        Kernels.multiplyImageAndScalar(clij, dst, src, 255f);

        ImagePlus thresholdedCL = clij.convert(src, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(thresholded,
                thresholdedCL));


        src.close();
        dst.close();
        IJ.exit();
        clij.close();
    }
}