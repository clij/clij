package net.haesleinhuepf.clij.converters.implementations;

import clearcl.util.ElapsedTime;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImagePlusToClearCLBufferConverterTest {
    @Test
    public void benchmarkConversion() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlusToClearCLBufferConverter converter = new ImagePlusToClearCLBufferConverter();
        converter.setCLIJ(clij);

        ImagePlus imp1 = NewImage.createByteImage("text", 1024, 1024, 100, NewImage.FILL_RAMP);
        ImagePlus imp2 = NewImage.createByteImage("text", 1024, 1024, 100, NewImage.FILL_RAMP);

        for (int i = 0; i < 10; i++ ){
            ElapsedTime.measureForceOutput("conversion", () -> {
                converter.convert(imp1);
            });
            ElapsedTime.measureForceOutput("legacy conversion", () -> {
                converter.convertLegacy(imp1);
            });
        }
    }
}