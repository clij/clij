package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import clearcl.enums.ImageChannelDataType;
import coremem.enums.NativeTypeEnum;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.ByteType;
import org.junit.Test;

import static org.junit.Assert.*;

public class DownsampleSliceBySliceHalfMedianTest {
    @Test
    public void downsampleSliceBySliceMedian() {
        CLIJ clij = CLIJ.getInstance();

        Img<ByteType> testImgBig = ArrayImgs.bytes(new byte[]{
                1, 2, 4, 4,
                2, 3, 4, 4,
                0, 1, 0, 0,
                1, 2, 0, 0
        }, new long[]{4, 4, 1});

        Img<ByteType> testImgSmall = ArrayImgs.bytes(new byte[]{
                2, 4,
                1, 0
        }, new long[]{2, 2, 1});

        ClearCLImage inputCL = clij.convert(testImgBig, ClearCLImage.class);
        ClearCLImage outputCL = clij.createCLImage(new long[]{2, 2, 1}, ImageChannelDataType.SignedInt8);

        Kernels.downsampleSliceBySliceHalfMedian(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);
        ImagePlus reference = clij.convert(testImgSmall, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(result, reference));
    }

    @Test
    public void downsampleSliceBySliceMedian_Buffer() {
        CLIJ clij = CLIJ.getInstance();

        Img<ByteType> testImgBig = ArrayImgs.bytes(new byte[]{
                1, 2, 4, 4,
                2, 3, 4, 4,
                0, 1, 0, 0,
                1, 2, 0, 0
        }, new long[]{4, 4, 1});

        Img<ByteType> testImgSmall = ArrayImgs.bytes(new byte[]{
                2, 4,
                1, 0
        }, new long[]{2, 2, 1});

        ClearCLBuffer inputCL = clij.convert(testImgBig, ClearCLBuffer.class);
        ClearCLBuffer outputCL = clij.createCLBuffer(new long[]{2, 2, 1}, NativeTypeEnum.Byte);

        Kernels.downsampleSliceBySliceHalfMedian(clij, inputCL, outputCL);

        ImagePlus result = clij.convert(outputCL, ImagePlus.class);
        ImagePlus reference = clij.convert(testImgSmall, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(result, reference));
    }


}