package net.haesleinhuepf.clij.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgs;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.UnknownFormatConversionException;

/**
 * ClearCLBufferToRandomAccessibleIntervalConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToRandomAccessibleIntervalConverter extends AbstractCLIJConverter<ClearCLBuffer, RandomAccessibleInterval> {

    @Override
    public RandomAccessibleInterval convert(ClearCLBuffer source) {
        int numberOfPixels = (int) (source.getWidth() * source.getHeight() * source.getDepth());
        if (numberOfPixels <= 0) {
            throw new IllegalArgumentException("Wrong image size!");
        }

        long[] dimensions = new long[]{
                source.getWidth(),
                source.getHeight(),
                1,
                source.getDepth(),
                1
        };

        // Todo: in case of large images, we might use PlanarImgs!

        if (source.getNativeType() == NativeTypeEnum.Byte ||
            source.getNativeType() == NativeTypeEnum.UnsignedByte) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(numberOfPixels);
            source.writeTo(byteBuffer, true);
            if (source.getNativeType() == NativeTypeEnum.Byte) {
                return ArrayImgs.bytes(byteBuffer.array(), dimensions);
            } else {
                byte[] array = byteBuffer.array();
                for (int i = 0; i < byteBuffer.limit(); i++) {
                    array[i] = (byte) (255 & array[i]);
                }
                return ArrayImgs.unsignedBytes(array, dimensions);
            }
        } else if (source.getNativeType() == NativeTypeEnum.Short ||
                   source.getNativeType() == NativeTypeEnum.UnsignedShort) {

            ShortBuffer shortBuffer = ShortBuffer.allocate(numberOfPixels);
            source.writeTo(shortBuffer, true);
            if (source.getNativeType() == NativeTypeEnum.Short) {
                return ArrayImgs.shorts(shortBuffer.array(), dimensions);
            } else {
                return ArrayImgs.unsignedShorts(shortBuffer.array(), dimensions);
            }
        } else if (source.getNativeType() == NativeTypeEnum.Float) {

            FloatBuffer floatBuff = FloatBuffer.allocate(numberOfPixels);
            source.writeTo(floatBuff, true);
            return ArrayImgs.floats(floatBuff.array(), dimensions);
        } else {
            throw new UnknownFormatConversionException(
                    "Cannot convert image of type "
                            + source.getNativeType().name());
        }
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }

    @Override
    public Class<RandomAccessibleInterval> getTargetType() {
        return RandomAccessibleInterval.class;
    }
}
