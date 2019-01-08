package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbsoluteTest {
    @Test
    public void absolute3d() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus negativeImp =
                NewImage.createImage("",
                        2,
                        2,
                        2,
                        32,
                        NewImage.FILL_BLACK);

        ImageProcessor ip1 = negativeImp.getProcessor();
        ip1.setf(0, 1, -1.0f);

        ClearCLImage input = clij.convert(negativeImp, ClearCLImage.class);


        assertEquals(-1, Kernels.sumPixels(clij, input), 0.0001);

        ClearCLImage abs = clij.createCLImage(input);
        Kernels.absolute(clij, input, abs);
        assertEquals(1, Kernels.sumPixels(clij, abs), 0.0001);

        input.close();
        abs.close();
        negativeImp.close();
        clij.close();
    }

    @Test
    public void absolute3d_Buffer() {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus negativeImp =
                NewImage.createImage("",
                        2,
                        2,
                        2,
                        32,
                        NewImage.FILL_BLACK);

        ImageProcessor ip1 = negativeImp.getProcessor();
        ip1.setf(0, 1, -1.0f);

        ClearCLBuffer input = clij.convert(negativeImp, ClearCLBuffer.class);
        //converter(negativeImp).getClearCLBuffer();

        assertEquals(-1, Kernels.sumPixels(clij, input), 0.0001);

        ClearCLBuffer abs = clij.createCLBuffer(input);
        Kernels.absolute(clij, input, abs);
        assertEquals(1, Kernels.sumPixels(clij, abs), 0.0001);


        input.close();
        abs.close();
        negativeImp.close();
        clij.close();
    }

}