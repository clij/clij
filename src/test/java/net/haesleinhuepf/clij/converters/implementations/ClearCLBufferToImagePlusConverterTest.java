package net.haesleinhuepf.clij.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClearCLBufferToImagePlusConverterTest {

    ImagePlus result1;
    ImagePlus result2;

    @Test
    public void testByteImageConversion() {
        CLIJ clij = CLIJ.getInstance();
        ClearCLBufferToImagePlusConverter biConverter = new ClearCLBufferToImagePlusConverter();
        biConverter.setCLIJ(clij);

        ImagePlus imp1 = NewImage.createByteImage("text", 1024, 1024, 25, NewImage.FILL_RAMP);
        ImagePlus imp2 = NewImage.createByteImage("text", 1024, 1024, 25, NewImage.FILL_RAMP);

        ClearCLBuffer buffer1 = clij.convert(imp1, ClearCLBuffer.class);
        ClearCLBuffer buffer2 = clij.convert(imp2, ClearCLBuffer.class);

        imp1.close();
        imp2.close();

        for (int i = 0; i < 10; i++ ){

            ElapsedTime.measureForceOutput("conversion", () -> {
                result1 = biConverter.convert(buffer1);
            });

            ElapsedTime.measureForceOutput("legacy conversion", () -> {
                result2 = biConverter.convertLegacy(buffer2);
            });

            assertTrue(TestUtilities.clBuffersEqual(clij, buffer1, buffer2, 0.001));
            assertTrue(TestUtilities.compareImages(result1, result2, 0.001));

            result1.close();
            result2.close();
        }

        buffer1.close();
        buffer2.close();
    }

    @Test
    public void testShortImageConversion() {
        CLIJ clij = CLIJ.getInstance();
        ClearCLBufferToImagePlusConverter biConverter = new ClearCLBufferToImagePlusConverter();
        biConverter.setCLIJ(clij);

        ImagePlus imp1 = NewImage.createShortImage("text", 1024, 1024, 25, NewImage.FILL_RAMP);
        ImagePlus imp2 = NewImage.createShortImage("text", 1024, 1024, 25, NewImage.FILL_RAMP);

        ClearCLBuffer buffer1 = clij.convert(imp1, ClearCLBuffer.class);
        ClearCLBuffer buffer2 = clij.convert(imp2, ClearCLBuffer.class);

        imp1.close();
        imp2.close();

        for (int i = 0; i < 10; i++ ){

            ElapsedTime.measureForceOutput("conversion", () -> {
                result1 = biConverter.convert(buffer1);
            });

            ElapsedTime.measureForceOutput("legacy conversion", () -> {
                result2 = biConverter.convertLegacy(buffer2);
            });

            assertTrue(TestUtilities.clBuffersEqual(clij, buffer1, buffer2, 0.001));
            assertTrue(TestUtilities.compareImages(result1, result2, 0.001));

            result1.close();
            result2.close();
        }

        buffer1.close();
        buffer2.close();
    }

    @Test
    public void testFloatImageConversion() {
        CLIJ clij = CLIJ.getInstance();
        ClearCLBufferToImagePlusConverter biConverter = new ClearCLBufferToImagePlusConverter();
        biConverter.setCLIJ(clij);

        ImagePlus imp1 = NewImage.createFloatImage("text", 1024, 1024, 15, NewImage.FILL_RAMP);
        ImagePlus imp2 = NewImage.createFloatImage("text", 1024, 1024, 15, NewImage.FILL_RAMP);

        ClearCLBuffer buffer1 = clij.convert(imp1, ClearCLBuffer.class);
        ClearCLBuffer buffer2 = clij.convert(imp2, ClearCLBuffer.class);

        imp1.close();
        imp2.close();

        for (int i = 0; i < 10; i++ ){

            ElapsedTime.measureForceOutput("conversion", () -> {
                result1 = biConverter.convert(buffer1);
            });

            ElapsedTime.measureForceOutput("legacy conversion", () -> {
                result2 = biConverter.convertLegacy(buffer2);
            });

            assertTrue(TestUtilities.clBuffersEqual(clij, buffer1, buffer2, 0.001));
            assertTrue(TestUtilities.compareImages(result1, result2, 0.001));

            result1.close();
            result2.close();
        }

        buffer1.close();
        buffer2.close();
    }
}