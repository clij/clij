package net.haesleinhuepf.clij.converters.implementations;

import clearcl.ClearCLBuffer;
import coremem.ContiguousMemoryInterface;
import coremem.enums.NativeTypeEnum;
import ij.ImagePlus;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * ImagePlusToRandomAccessibleIntervalConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ImagePlusToClearCLBufferConverter extends AbstractCLIJConverter<ImagePlus, ClearCLBuffer> {

    @Override
    public ClearCLBuffer convert(ImagePlus source) {


        long[] dimensions = new long[source.getNSlices() == 1?2:3];
        dimensions[0] = source.getWidth();
        dimensions[1] = source.getHeight();
        if (source.getNSlices() > 1) {
            dimensions[2] = source.getNSlices();
        }

        int numberOfPixelsPerSlice = (int)(dimensions[0] * dimensions[1]);
        long numberOfPixels = dimensions[0] * dimensions[1] * dimensions[2];

        NativeTypeEnum type;
        if (source.getBitDepth() == 8) {
            ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedByte);

            byte[] inputArray = new byte[(int) numberOfPixels];
            for (int z = 0; z < target.getDepth(); z++) {
                source.setSlice(z + 1);
                byte[] sourceArray = (byte[])(source.getProcessor().getPixels());
                System.arraycopy(sourceArray, 0, inputArray, z * numberOfPixelsPerSlice, sourceArray.length);
            }
            ByteBuffer byteBuffer = ByteBuffer.wrap(inputArray);
            target.readFrom(byteBuffer, true);


        } else if (source.getBitDepth() == 16) {
            ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedShort);

            short[] inputArray = new short[(int) numberOfPixels];
            for (int z = 0; z < target.getDepth(); z++) {
                source.setSlice(z + 1);
                short[] sourceArray = (short[])(source.getProcessor().getPixels());
                System.arraycopy(sourceArray, 0, inputArray, z * numberOfPixelsPerSlice, sourceArray.length);
            }
            ShortBuffer byteBuffer = ShortBuffer.wrap(inputArray);
            target.readFrom(byteBuffer, true);
        } else{
            ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.Float);

            float[] inputArray = new float[(int) numberOfPixels];
            for (int z = 0; z < target.getDepth(); z++) {
                source.setSlice(z + 1);
                float[] sourceArray = (float[])(source.getProcessor().getPixels());
                System.arraycopy(sourceArray, 0, inputArray, z * numberOfPixelsPerSlice, sourceArray.length);
            }
            FloatBuffer byteBuffer = FloatBuffer.wrap(inputArray);
            target.readFrom(byteBuffer, true);
        }
        return null;
    }

    public ClearCLBuffer convertLegacy(ImagePlus source) {
        RandomAccessibleInterval rai = ImageJFunctions.wrapReal(source);
        RandomAccessibleIntervalToClearCLBufferConverter raitcclbc = new RandomAccessibleIntervalToClearCLBufferConverter();
        raitcclbc.setCLIJ(clij);
        return raitcclbc.convert(rai);
    }

    @Override
    public Class<ImagePlus> getSourceType() {
        return ImagePlus.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }
}
