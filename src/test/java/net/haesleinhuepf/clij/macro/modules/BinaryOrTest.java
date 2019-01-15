package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class BinaryOrTest {

    @Test
    public void binaryOr2d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 1, 8, 0, 1);

        ClearCLImage clearCLImage = clij.convert(testImp2, ClearCLImage.class);;
        ClearCLImage clearCLImageNot = clij.createCLImage(clearCLImage.getDimensions(), clearCLImage.getChannelDataType());
        ClearCLImage clearCLImageOr = clij.createCLImage(clearCLImage.getDimensions(), clearCLImage.getChannelDataType());

        Kernels.binaryNot(clij, clearCLImage, clearCLImageNot);
        Kernels.binaryOr(clij, clearCLImage, clearCLImageNot, clearCLImageOr);

        long numberOfPixels = (long) Kernels.sumPixels(clij, clearCLImageOr);

        long numberOfPositivePixels = (long) Kernels.sumPixels(clij, clearCLImage);
        long numberOfNegativePixels = (long) Kernels.sumPixels(clij, clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
        clearCLImageOr.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void binaryOr2d_Buffer() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 1, 8, 0, 1);

        ClearCLBuffer clearCLImage = clij.convert(testImp2, ClearCLBuffer.class);;
        ClearCLBuffer clearCLImageNot = clij.createCLBuffer(clearCLImage.getDimensions(), clearCLImage.getNativeType());
        ClearCLBuffer clearCLImageOr = clij.createCLBuffer(clearCLImage.getDimensions(), clearCLImage.getNativeType());

        Kernels.binaryNot(clij, clearCLImage, clearCLImageNot);
        Kernels.binaryOr(clij, clearCLImage, clearCLImageNot, clearCLImageOr);

        long numberOfPixels = (long) Kernels.sumPixels(clij, clearCLImageOr);

        long numberOfPositivePixels = (long) Kernels.sumPixels(clij, clearCLImage);
        long numberOfNegativePixels = (long) Kernels.sumPixels(clij, clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
        clearCLImageOr.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void binaryOr3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 8, 0, 1);

        ClearCLImage clearCLImage = clij.convert(mask3d, ClearCLImage.class);
        ClearCLImage clearCLImageNot = clij.createCLImage(clearCLImage.getDimensions(), clearCLImage.getChannelDataType());
        ClearCLImage clearCLImageOr = clij.createCLImage(clearCLImage.getDimensions(), clearCLImage.getChannelDataType());

        Kernels.binaryNot(clij, clearCLImage, clearCLImageNot);
        Kernels.binaryOr(clij, clearCLImage, clearCLImageNot, clearCLImageOr);

        long numberOfPixels = (long) Kernels.sumPixels(clij, clearCLImageOr);

        long numberOfPositivePixels = (long) Kernels.sumPixels(clij, clearCLImage);
        long numberOfNegativePixels = (long) Kernels.sumPixels(clij, clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
        clearCLImageOr.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void binaryOr3d_Buffers() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 8, 0, 1);

        ClearCLBuffer clearCLImage = clij.convert(mask3d, ClearCLBuffer.class);
        ClearCLBuffer clearCLImageNot = clij.createCLBuffer(clearCLImage.getDimensions(), clearCLImage.getNativeType());
        ClearCLBuffer clearCLImageOr = clij.createCLBuffer(clearCLImage.getDimensions(), clearCLImage.getNativeType());

        Kernels.binaryNot(clij, clearCLImage, clearCLImageNot);
        Kernels.binaryOr(clij, clearCLImage, clearCLImageNot, clearCLImageOr);

        long numberOfPixels = (long) Kernels.sumPixels(clij, clearCLImageOr);

        long numberOfPositivePixels = (long) Kernels.sumPixels(clij, clearCLImage);
        long numberOfNegativePixels = (long) Kernels.sumPixels(clij, clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
        clearCLImageOr.close();
        IJ.exit();
        clij.close();
    }

}