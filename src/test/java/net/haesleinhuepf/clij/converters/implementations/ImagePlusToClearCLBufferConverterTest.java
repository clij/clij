package net.haesleinhuepf.clij.converters.implementations;

import clearcl.ClearCLBuffer;
import clearcl.util.ElapsedTime;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImagePlusToClearCLBufferConverterTest {

    ClearCLBuffer temp;

    @Test
    public void benchmarkConversion() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlusToClearCLBufferConverter ibConverter = new ImagePlusToClearCLBufferConverter();
        ibConverter.setCLIJ(clij);

        ClearCLBufferToImagePlusConverter biConverter = new ClearCLBufferToImagePlusConverter();
        biConverter.setCLIJ(clij);

        ImagePlus imp1 = NewImage.createByteImage("text", 1024, 1024, 100, NewImage.FILL_RAMP);
        ImagePlus imp2 = NewImage.createByteImage("text", 1024, 1024, 100, NewImage.FILL_RAMP);

        for (int i = 0; i < 10; i++ ){
            ElapsedTime.measureForceOutput("conversion", () -> {
                ibConverter.convert(imp1);
            });

            ElapsedTime.measureForceOutput("legacy conversion", () -> {
                temp = ibConverter.convertLegacy(imp2);
            });

            ElapsedTime.measureForceOutput("back conversion", () -> {
                biConverter.convert(temp);
            });

        }




    }
}