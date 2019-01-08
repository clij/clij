package net.haesleinhuepf.clij.converters.implementations;

import clearcl.ClearCLBuffer;
import coremem.enums.NativeTypeEnum;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.NewImage;
import ij.plugin.Duplicator;
import ij.plugin.RGBStackMerge;
import ij.process.ByteProcessor;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * ClearCLBufferToImagePlusConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToImagePlusConverter extends AbstractCLIJConverter<ClearCLBuffer, ImagePlus> {

    @Override
    public ImagePlus convert(ClearCLBuffer source) {
        int width = (int) source.getWidth();
        int height = (int) source.getHeight();
        int depth = (int) source.getDepth();

        int numberOfPixelsPerPlane = width * height;

        ImagePlus result = null;
        //ImagePlus[] slices = new ImagePlus[depth];
        //ImageStack stack = new ImageStack(width, height, depth);
        ClearCLBuffer plane = clij.createCLBuffer(new long[]{width, height}, source.getNativeType());

        if (source.getNativeType() == NativeTypeEnum.UnsignedByte) {
            result = NewImage.createByteImage("slice", width, height, depth, NewImage.FILL_BLACK);
            for (int z = 0; z < depth; z++) {
                Kernels.copySlice(clij, source, plane, z);
                result.setSlice(z + 1);
                byte[] array = (byte[]) result.getProcessor().getPixels();
                ByteBuffer buffer = ByteBuffer.wrap(array);
                plane.writeTo(buffer, 0, numberOfPixelsPerPlane, true);
            }
        } else if (source.getNativeType() == NativeTypeEnum.UnsignedShort) {
            result = NewImage.createShortImage("slice", width, height, depth, NewImage.FILL_BLACK);
            for (int z = 0; z < depth; z++) {
                Kernels.copySlice(clij, source, plane, z);
                result.setSlice(z + 1);
                short[] array = (short[]) result.getProcessor().getPixels();
                ShortBuffer buffer = ShortBuffer.wrap(array);
                plane.writeTo(buffer, 0, numberOfPixelsPerPlane, true);
            }
        } else if (source.getNativeType() == NativeTypeEnum.Float) {
            result = NewImage.createFloatImage("slice", width, height, depth, NewImage.FILL_BLACK);
            for (int z = 0; z < depth; z++) {
                Kernels.copySlice(clij, source, plane, z);
                result.setSlice(z + 1);
                float[] array = (float[]) result.getProcessor().getPixels();
                FloatBuffer buffer = FloatBuffer.wrap(array);
                plane.writeTo(buffer, 0, numberOfPixelsPerPlane, true);
            }
        }
        plane.close();

        if (result == null) {
            result = convertLegacy(source);
        }

        //ImagePlus imp = new ImagePlus("image", stack);
        //stack
        //RGBStackMerge.mergeStacks()
        return result;

    }

    public ImagePlus convertLegacy(ClearCLBuffer source) {
        ClearCLBufferToRandomAccessibleIntervalConverter cclbtraic = new ClearCLBufferToRandomAccessibleIntervalConverter();
        cclbtraic.setCLIJ(clij);
        RandomAccessibleInterval rai = cclbtraic.convert(source);
        return new Duplicator().run(ImageJFunctions.wrap(rai, "" + rai));
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }

    @Override
    public Class<ImagePlus> getTargetType() {
        return ImagePlus.class;
    }
}
