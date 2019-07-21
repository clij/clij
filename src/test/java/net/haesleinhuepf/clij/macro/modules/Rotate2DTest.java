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
    private final static double tolerance = 0.1;
    private final static double angle = 45.0;

    
    @Ignore  
    // Only as principle test
    // Differences between rotIJ and are to big for direct pixel comparison
    @Test
    public void rotateLeft2d() throws InterruptedException {

        CLIJ clij = CLIJ.getInstance();
        
        ImagePlus test2D = IJ.openImage("src/main/resources/blobs.tif");
        IJ.run(test2D, "Invert LUT", "");
        IJ.run(test2D, "32-bit", "");

        test2D.show();

        // do operation with ImageJ
        ImagePlus rotIJ = test2D.duplicate();
        IJ.run(rotIJ, "Rotate... ", "angle=" + angle + " interpolation=Bicubic");
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

        clij.show(rotCL, "rotCL");
        new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(rotIJ, rotCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clij.close();
    }

    
    @Ignore  
    // Problem with Buffer version.
    // Error: passing 'float2' (vector of 2 'float' values) to parameter 
    // of incompatible type 'int2' (vector of 2 'int' values)
    // (Maybe due to used OpenCLVersion 1.2 and 2.0)
    // (Buffer version are called in modules for getOpenCLVersion() < 1.2 only)
    @Test
    public void rotateLeft2d_Buffers() throws InterruptedException {

        CLIJ clij = CLIJ.getInstance();
        
        ImagePlus test2D = IJ.openImage("src/main/resources/blobs.tif");
        IJ.run(test2D, "Invert LUT", "");
        IJ.run(test2D, "32-bit", "");

        test2D.show();

        // do operation with ImageJ
        ImagePlus rotIJ = test2D.duplicate();
        IJ.run(rotIJ, "Rotate... ", "angle=" + angle + " interpolation=Bicubic");
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

        clij.show(dst, "rotCL");
        new WaitForUserDialog("wait").show();

        assertTrue(TestUtilities.compareImages(rotIJ, rotCL, tolerance));

        src.close();
        dst.close();

        IJ.exit();
        clij.close();
    }


}