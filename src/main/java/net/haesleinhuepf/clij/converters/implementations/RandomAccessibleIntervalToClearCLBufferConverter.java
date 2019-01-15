package net.haesleinhuepf.clij.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.converters.ConverterUtilities;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * RandomAccessibleIntervalToClearCLBufferConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class RandomAccessibleIntervalToClearCLBufferConverter extends AbstractCLIJConverter<RandomAccessibleInterval, ClearCLBuffer> {

    @Override
    public ClearCLBuffer convert(RandomAccessibleInterval source) {
        long[]
                dimensions =
                new long[source.numDimensions()];
        source.dimensions(dimensions);

        RealType pixel = (RealType) (Views.iterable(source).firstElement());

        NativeTypeEnum lImageChannelType = ConverterUtilities.imglib2TypeToNativeType(pixel);

        ClearCLBuffer lClearClBuffer = clij.createCLBuffer(dimensions, lImageChannelType);

        copyRandomAccessibleIntervalToClearCLBuffer(source, lClearClBuffer);

        return lClearClBuffer;
    }

    public static <T extends RealType<T>> void copyRandomAccessibleIntervalToClearCLBuffer(RandomAccessibleInterval<T> source, ClearCLBuffer target) {
        long[] dimensions = new long[source.numDimensions()];
        source.dimensions(dimensions);

        long numberOfPixels = 1;
        for (int i = 0; i < dimensions.length; i++) {
            numberOfPixels *= dimensions[i];
        }

        int count = 0;
        Cursor<T> cursor = Views.iterable(source).cursor();

        if (target.getNativeType() == NativeTypeEnum.Byte ||
            target.getNativeType() == NativeTypeEnum.UnsignedByte) {

            byte[] inputArray = new byte[(int) numberOfPixels];
            while (cursor.hasNext()) {
                inputArray[count] = (byte) cursor.next().getRealFloat();
                count++;
            }
            ByteBuffer byteBuffer = ByteBuffer.wrap(inputArray);
            target.readFrom(byteBuffer, true);
        } else if (target.getNativeType() == NativeTypeEnum.Short ||
                   target.getNativeType() == NativeTypeEnum.UnsignedShort) {

            short[] inputArray = new short[(int) numberOfPixels];
            while (cursor.hasNext()) {
                inputArray[count] = (short) cursor.next().getRealFloat();
                count++;
            }
            ShortBuffer shortBuffer = ShortBuffer.wrap(inputArray);
            target.readFrom(shortBuffer, true);
        } else if (target.getNativeType() == NativeTypeEnum.Float) {
            float[] inputArray = new float[(int) numberOfPixels];
            while (cursor.hasNext()) {
                inputArray[count] = cursor.next().getRealFloat();
                count++;
            }
            FloatBuffer floatBuffer = FloatBuffer.wrap(inputArray);
            target.readFrom(floatBuffer, true);
        } else {
            throw new IllegalArgumentException("Cannot copy content of buffer because of unknown type.");
        }
    }

    @Override
    public Class<RandomAccessibleInterval> getSourceType() {
        return RandomAccessibleInterval.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }
}
