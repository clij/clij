package net.haesleinhuepf.clij.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.converters.ConverterUtilities;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

/**
 * RandomAccessibleIntervalToClearCLImageConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class RandomAccessibleIntervalToClearCLImageConverter extends AbstractCLIJConverter<RandomAccessibleInterval, ClearCLImage> {

    @Override
    public ClearCLImage convert(RandomAccessibleInterval source) {
        long[]
                dimensions =
                new long[source.numDimensions()];
        source.dimensions(dimensions);

        RealType pixel = (RealType) Views.iterable(source).firstElement();

        ImageChannelDataType lImageChannelType = ConverterUtilities.imglib2TypeToImageChannelDataType(pixel);

        ClearCLImage lClearClImage = clij.createCLImage(dimensions, lImageChannelType);

        copyRandomAccessibleIntervalToClearCLImage(source, lClearClImage);

        return lClearClImage;
    }

    public static <T extends RealType<T>> void copyRandomAccessibleIntervalToClearCLImage(RandomAccessibleInterval<T> source, ClearCLImage target) {
        long[] dimensions = new long[source.numDimensions()];
        source.dimensions(dimensions);

        long sumDimensions = 1;
        for (int i = 0; i < dimensions.length; i++) {
            sumDimensions *= dimensions[i];
        }

        int count = 0;
        Cursor<T> cursor = Views.iterable(source).cursor();

        if (target.getChannelDataType() == ImageChannelDataType.SignedInt8 ||
                target.getChannelDataType() == ImageChannelDataType.UnsignedInt8) {

            byte[] inputArray = new byte[(int) sumDimensions];
            while (cursor.hasNext()) {
                inputArray[count] = (byte) cursor.next().getRealFloat();
                count++;
            }
            target.readFrom(inputArray, true);
        } else if (target.getChannelDataType() == ImageChannelDataType.SignedInt16 ||
                target.getChannelDataType() == ImageChannelDataType.UnsignedInt16) {

            short[] inputArray = new short[(int) sumDimensions];
            while (cursor.hasNext()) {
                inputArray[count] = (short) cursor.next().getRealFloat();
                count++;
            }
            target.readFrom(inputArray, true);
        } else if (target.getChannelDataType() == ImageChannelDataType.Float) {
            float[] inputArray = new float[(int) sumDimensions];
            while (cursor.hasNext()) {
                inputArray[count] = cursor.next().getRealFloat();
                count++;
            }
            target.readFrom(inputArray, true);
        } else {
            throw new IllegalArgumentException("Cannot copy content of image because of unknown type.");
        }
    }


    @Override
    public Class<RandomAccessibleInterval> getSourceType() {
        return RandomAccessibleInterval.class;
    }

    @Override
    public Class<ClearCLImage> getTargetType() {
        return ClearCLImage.class;
    }
}
