package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class BinaryNotTest {

    @Test
    public void binaryNot2d() throws InterruptedException {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(10, 10, 1, 8, 0, 1);

        ClearCLImage clearCLImage = clij.convert(testImp2, ClearCLImage.class);;
        ClearCLImage clearCLImageNot = clij.createCLImage(clearCLImage.getDimensions(), clearCLImage.getChannelDataType());

        Kernels.binaryNot(clij, clearCLImage, clearCLImageNot);

        long numberOfPixels = clearCLImage.getWidth() * clearCLImage.getHeight() * clearCLImage.getDepth();

        long numberOfPositivePixels = (long) Kernels.sumPixels(clij, clearCLImage);
        long numberOfNegativePixels = (long) Kernels.sumPixels(clij, clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
    }

    @Test
    public void binaryNot2d_Buffers() throws InterruptedException {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 8, 0, 1);

        ClearCLBuffer clearCLImage = clij.convert(testImp2, ClearCLBuffer.class);;
        ClearCLBuffer clearCLImageNot = clij.createCLBuffer(clearCLImage.getDimensions(), clearCLImage.getNativeType());

        Kernels.binaryNot(clij, clearCLImage, clearCLImageNot);

        long numberOfPixels = clearCLImage.getWidth() * clearCLImage.getHeight() * clearCLImage.getDepth();

        long numberOfPositivePixels = (long) Kernels.sumPixels(clij, clearCLImage);
        long numberOfNegativePixels = (long) Kernels.sumPixels(clij, clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
    }

    @Test
    public void binaryNot3d() throws InterruptedException {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 8, 0, 1);

        ClearCLImage clearCLImage = clij.convert(mask3d, ClearCLImage.class);
        ClearCLImage clearCLImageNot = clij.createCLImage(clearCLImage.getDimensions(), clearCLImage.getChannelDataType());

        Kernels.binaryNot(clij, clearCLImage, clearCLImageNot);

        long numberOfPixels = clearCLImage.getWidth() * clearCLImage.getHeight() * clearCLImage.getDepth();

        long numberOfPositivePixels = (long) Kernels.sumPixels(clij, clearCLImage);
        long numberOfNegativePixels = (long) Kernels.sumPixels(clij, clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
    }


    @Test
    public void binaryNot3d_Buffers() throws InterruptedException {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus mask3d = TestUtilities.getRandomImage(100, 100, 3, 8, 0, 1);

        ClearCLBuffer clearCLImage = clij.convert(mask3d, ClearCLBuffer.class);
        ClearCLBuffer clearCLImageNot = clij.createCLBuffer(clearCLImage.getDimensions(), clearCLImage.getNativeType());

        Kernels.binaryNot(clij, clearCLImage, clearCLImageNot);

        long numberOfPixels = clearCLImage.getWidth() * clearCLImage.getHeight() * clearCLImage.getDepth();

        long numberOfPositivePixels = (long) Kernels.sumPixels(clij, clearCLImage);
        long numberOfNegativePixels = (long) Kernels.sumPixels(clij, clearCLImageNot);

        assertEquals(numberOfPixels, numberOfNegativePixels + numberOfPositivePixels);
        clearCLImage.close();
        clearCLImageNot.close();
    }

}