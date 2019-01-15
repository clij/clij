package net.haesleinhuepf.clij.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static net.haesleinhuepf.clij.test.TestUtilities.clBuffersEqual;
import static org.junit.Assert.assertTrue;

public class ImagePlusToClearCLBufferConverterTest {

    ClearCLBuffer temp1;
    ClearCLBuffer temp2;

    @Test
    public void testByteImageConversion() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlusToClearCLBufferConverter ibConverter = new ImagePlusToClearCLBufferConverter();
        ibConverter.setCLIJ(clij);

        ClearCLBufferToImagePlusConverter biConverter = new ClearCLBufferToImagePlusConverter();
        biConverter.setCLIJ(clij);

        ImagePlus imp1 = NewImage.createByteImage("text", 1024, 1024, 25, NewImage.FILL_RAMP);
        ImagePlus imp2 = NewImage.createByteImage("text", 1024, 1024, 25, NewImage.FILL_RAMP);

        for (int i = 0; i < 10; i++ ){
            ElapsedTime.measureForceOutput("conversion", () -> {
                temp1 = ibConverter.convert(imp1);
            });

            ElapsedTime.measureForceOutput("legacy conversion", () -> {
                temp2 = ibConverter.convertLegacy(imp2);
            });

            assertTrue(clBuffersEqual(clij, temp1, temp2, 0.001));
            temp1.close();
            temp2.close();
        }

        imp1.close();
        imp2.close();
    }

    @Test
    public void testShortImageConversion() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlusToClearCLBufferConverter ibConverter = new ImagePlusToClearCLBufferConverter();
        ibConverter.setCLIJ(clij);

        ClearCLBufferToImagePlusConverter biConverter = new ClearCLBufferToImagePlusConverter();
        biConverter.setCLIJ(clij);

        ImagePlus imp1 = NewImage.createShortImage("text", 1024, 1024, 25, NewImage.FILL_RAMP);
        ImagePlus imp2 = NewImage.createShortImage("text", 1024, 1024, 25, NewImage.FILL_RAMP);

        for (int i = 0; i < 10; i++ ){
            ElapsedTime.measureForceOutput("conversion", () -> {
                temp1 = ibConverter.convert(imp1);
            });

            ElapsedTime.measureForceOutput("legacy conversion", () -> {
                temp2 = ibConverter.convertLegacy(imp2);
            });

            assertTrue(clBuffersEqual(clij, temp1, temp2, 0.001));
            temp1.close();
            temp2.close();
        }

        imp1.close();
        imp2.close();
    }

    @Test
    public void testFloatImageConversion() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlusToClearCLBufferConverter ibConverter = new ImagePlusToClearCLBufferConverter();
        ibConverter.setCLIJ(clij);

        ClearCLBufferToImagePlusConverter biConverter = new ClearCLBufferToImagePlusConverter();
        biConverter.setCLIJ(clij);

        ImagePlus imp1 = NewImage.createFloatImage("text", 1024, 1024, 25, NewImage.FILL_RAMP);
        ImagePlus imp2 = NewImage.createFloatImage("text", 1024, 1024, 25, NewImage.FILL_RAMP);

        for (int i = 0; i < 10; i++ ){
            ElapsedTime.measureForceOutput("conversion", () -> {
                temp1 = ibConverter.convert(imp1);
            });

            ElapsedTime.measureForceOutput("legacy conversion", () -> {
                temp2 = ibConverter.convertLegacy(imp2);
            });

            assertTrue(clBuffersEqual(clij, temp1, temp2, 0.001));
            temp1.close();
            temp2.close();
        }

        imp1.close();
        imp2.close();
    }

    @Test
    public void testSpecialConversion() {
        CLIJ clij = CLIJ.getInstance();

        ImagePlus imp1 = TestUtilities.getRandomImage(100, 100, 3, 16, 0, 15);

        ImagePlusToClearCLBufferConverter ibConverter = new ImagePlusToClearCLBufferConverter();
        ibConverter.setCLIJ(clij);

        ElapsedTime.measureForceOutput("conversion", () -> {
            temp1 = ibConverter.convert(imp1);
        });

        ElapsedTime.measureForceOutput("legacy conversion", () -> {
            temp2 = ibConverter.convertLegacy(imp1);
        });

        assertTrue(clBuffersEqual(clij, temp1, temp2, 0.001));
        temp1.close();
        temp2.close();
    }
}