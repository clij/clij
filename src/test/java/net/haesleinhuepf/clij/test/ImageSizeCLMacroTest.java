package net.haesleinhuepf.clij.test;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import clojure.lang.Obj;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * ImageSizeCLMacroTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 01 2019
 */
public class ImageSizeCLMacroTest {
    @Test
    public void testReadImageSrc1Width() {
        CLIJ clij = CLIJ.getInstance();

        int referenceWidth = 7;

        ClearCLBuffer src1 = clij.createCLBuffer(new long[]{referenceWidth, 2, 3}, NativeTypeEnum.Byte);
        ClearCLBuffer src2 = clij.createCLBuffer(new long[]{4, 5, 6}, NativeTypeEnum.Byte);
        ClearCLBuffer dst = clij.createCLBuffer(new long[]{1, 1, 1}, NativeTypeEnum.Byte);

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        clij.execute(Kernels.class, "test.cl", "set_pixels_to_width_src1_3d", parameters);

        ImagePlus imp = clij.convert(dst, ImagePlus.class);

        int width = imp.getProcessor().get(0,0);

        assertEquals(referenceWidth, width);

        src1.close();
        src2.close();
        dst.close();
        IJ.exit();
        clij.close();
    }

    @Test
    public void testReadImageSrc12Width() {
        CLIJ clij = CLIJ.getInstance();

        int referenceWidth = 7;

        ClearCLBuffer src1 = clij.createCLBuffer(new long[]{1, 2, 3}, NativeTypeEnum.Byte);
        ClearCLBuffer src2 = clij.createCLBuffer(new long[]{referenceWidth, 5, 6}, NativeTypeEnum.Byte);
        ClearCLBuffer dst = clij.createCLBuffer(new long[]{1, 1, 1}, NativeTypeEnum.Byte);

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        clij.execute(Kernels.class, "test.cl", "set_pixels_to_width_src2_3d", parameters);

        ImagePlus imp = clij.convert(dst, ImagePlus.class);

        int width = imp.getProcessor().get(0,0);

        assertEquals(referenceWidth, width);

        src1.close();
        src2.close();
        dst.close();
        IJ.exit();
        clij.close();
    }
}
