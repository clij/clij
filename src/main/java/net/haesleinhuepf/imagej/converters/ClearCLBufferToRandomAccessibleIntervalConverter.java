package net.haesleinhuepf.imagej.converters;

import clearcl.ClearCLBuffer;
import coremem.enums.NativeTypeEnum;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
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

        // Todo: in case of large images, we might use PlanarImgs!

        if (source.getNativeType() == NativeTypeEnum.Byte ||
            source.getNativeType() == NativeTypeEnum.UnsignedByte) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(numberOfPixels);
            source.writeTo(byteBuffer, true);
            if (source.getNativeType() == NativeTypeEnum.Byte) {
                return ArrayImgs.bytes(byteBuffer.array(), source.getDimensions());
            } else {
                byte[] array = byteBuffer.array();
                for (int i = 0; i < byteBuffer.limit(); i++) {
                    array[i] = (byte) (255 & array[i]);
                }
                return ArrayImgs.unsignedBytes(array, source.getDimensions());
            }
        } else if (source.getNativeType() == NativeTypeEnum.Short ||
                   source.getNativeType() == NativeTypeEnum.UnsignedShort) {

            ShortBuffer shortBuffer = ShortBuffer.allocate(numberOfPixels);
            source.writeTo(shortBuffer, true);
            if (source.getNativeType() == NativeTypeEnum.Short) {
                return ArrayImgs.shorts(shortBuffer.array(), source.getDimensions());
            } else {
                return ArrayImgs.unsignedShorts(shortBuffer.array(), source.getDimensions());
            }
        } else if (source.getNativeType() == NativeTypeEnum.Float) {

            FloatBuffer floatBuff = FloatBuffer.allocate(numberOfPixels);
            source.writeTo(floatBuff, true);
            return ArrayImgs.floats(floatBuff.array(), source.getDimensions());
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
