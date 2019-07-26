package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.imglib2.realtransform.AffineTransform2D;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;


public class Rotate2DTest {
    private final static float tolerance = 1f;
    private final static double angle = 45.0;


    @Test
    public void rotate2d() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance("1070");
        
        ImagePlus test2D = IJ.openImage("src/main/resources/blobs.tif");
        IJ.run(test2D, "Invert LUT", "");
        IJ.run(test2D, "32-bit", "");

        test2D.show();

        // do operation with ImageJ
        ImagePlus rotIJ = test2D.duplicate();
        IJ.run(rotIJ, "Rotate... ", "angle=" + angle + " interpolation=Bilinear");
        rotIJ.show();

        // do operation with OpenCL
        ClearCLImage src = clij.convert(test2D, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);

        float angleRad = (float) (-angle / 180.0 * Math.PI);
        
        AffineTransform2D at = new AffineTransform2D();
        at.translate(-src.getWidth() / 2, -src.getHeight() / 2);
        at.rotate(angleRad);
        at.translate(src.getWidth() / 2, src.getHeight() / 2);
        
        Kernels.affineTransform2D(clij, src, dst, AffineTransform.matrixToFloatArray2D(at));
     
        ImagePlus rotCL = clij.convert(dst, ImagePlus.class);

        ClearCLImage imageJResult = clij.convert(rotIJ, ClearCLImage.class);
        long countNonZeroPixels = TestUtilities.countPixelsWithDifferenceAboveTolerance(clij, imageJResult, dst, tolerance);

        // differences are an edge-artefact. Check that only a number of pixels is affected that's
        // smaller than all edge pixels
        System.out.println("pixels with huge differences: " + countNonZeroPixels);

        assertTrue( countNonZeroPixels < 2 * imageJResult.getWidth() + 2 * imageJResult.getHeight());

        imageJResult.close();

        clij.show(rotCL, "rotCL");

        src.close();
        dst.close();

        IJ.exit();
        clij.close();
    }


    @Test
    public void rotate2d_Buffers() throws InterruptedException {

        CLIJ clij = CLIJ.getInstance();
        
        ImagePlus test2D = IJ.openImage("src/main/resources/blobs.tif");
        IJ.run(test2D, "Invert LUT", "");
        IJ.run(test2D, "32-bit", "");

        test2D.show();

        // do operation with ImageJ
        ImagePlus rotIJ = test2D.duplicate();
        IJ.run(rotIJ, "Rotate... ", "angle=" + angle + " interpolation=None");
        rotIJ.show();

        // do operation with OpenCL
        ClearCLBuffer src = clij.convert(test2D, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);

        float angleRad = (float) (-angle / 180.0 * Math.PI);

        AffineTransform2D at = new AffineTransform2D();

        at.translate(-src.getWidth() / 2, -src.getHeight() / 2);
        at.rotate(angleRad);
        at.translate(src.getWidth() / 2, src.getHeight() / 2);
        Kernels.affineTransform2D(clij, src, dst, AffineTransform.matrixToFloatArray2D(at));

        ImagePlus rotCL = clij.convert(dst, ImagePlus.class);

        ClearCLBuffer imageJResult = clij.convert(rotIJ, ClearCLBuffer.class);
        long countNonZeroPixels = TestUtilities.countPixelsWithDifferenceAboveTolerance(clij, imageJResult, dst, tolerance);

        // differences are an edge-artefact. Check that only a number of pixels is affected that's
        // smaller than all edge pixels
        System.out.println("pixels with huge differences: " + countNonZeroPixels);

        assertTrue( countNonZeroPixels < 2 * imageJResult.getWidth() + 2 * imageJResult.getHeight());

        imageJResult.close();

        src.close();
        dst.close();

        IJ.exit();
        clij.close();
    }


}