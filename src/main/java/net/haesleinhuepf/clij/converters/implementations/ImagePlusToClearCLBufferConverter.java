package net.haesleinhuepf.clij.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.ContiguousMemoryInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import ij.IJ;
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

    final int THIRD_DIMENSION_Z = 0;
    final int THIRD_DIMENSION_T = 1;
    final int THIRD_DIMENSION_C = 2;
    final int THIRD_DIMENSION_NONE = -1;

    private void setThirdDimension(ImagePlus imp, int thirdDimension, int value) {
        if (thirdDimension == THIRD_DIMENSION_Z) {
            imp.setZ(value);
        } else if (thirdDimension == THIRD_DIMENSION_C) {
            imp.setC(value);
        } else if (thirdDimension == THIRD_DIMENSION_T) {
            imp.setT(value);
        }
    }

    @Override
    public ClearCLBuffer convert(ImagePlus source) {
        //long time = System.currentTimeMillis();
        //convertLegacy(source).close();
        //IJ.log("legacy conv took " + (System.currentTimeMillis() - time));
        //long time2 = System.currentTimeMillis();

        long[] dimensions = null; // = new long[source.getNSlices() == 1?2:3];
        int thirdDimension;
        // check what's the third dimension
        if (source.getNSlices() > 1) {
            dimensions = new long[]{source.getWidth(), source.getHeight(), source.getNSlices()};
            thirdDimension = THIRD_DIMENSION_Z;
        } else if (source.getNChannels() > 1) {
            dimensions = new long[]{source.getWidth(), source.getHeight(), source.getNChannels()};
            thirdDimension = THIRD_DIMENSION_C;
        } else if (source.getNFrames() > 1) {
            dimensions = new long[]{source.getWidth(), source.getHeight(), source.getNFrames()};
            thirdDimension = THIRD_DIMENSION_T;
        } else {
            dimensions = new long[]{source.getWidth(), source.getHeight()};
            thirdDimension = THIRD_DIMENSION_NONE;
        }

        int numberOfPixelsPerSlice = (int)(dimensions[0] * dimensions[1]);
        long numberOfPixels = numberOfPixelsPerSlice;
        if (thirdDimension != THIRD_DIMENSION_NONE) {
            numberOfPixels = numberOfPixels * dimensions[2];
        }

            //NativeTypeEnum type;
        if (source.getBitDepth() == 8) {
            ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedByte);

            byte[] inputArray = new byte[(int) numberOfPixels];
            for (int z = 0; z < target.getDepth(); z++) {
                setThirdDimension(source, thirdDimension, z + 1);

                byte[] sourceArray = (byte[])(source.getProcessor().getPixels());
                System.arraycopy(sourceArray, 0, inputArray, z * numberOfPixelsPerSlice, sourceArray.length);
            }
            ByteBuffer byteBuffer = ByteBuffer.wrap(inputArray);
            target.readFrom(byteBuffer, true);
            return target;

        } else if (source.getBitDepth() == 16) {
            ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedShort);

            //time = System.currentTimeMillis();
            short[] inputArray = new short[(int) numberOfPixels];
            //IJ.log("Alloc took " + (System.currentTimeMillis() - time));

            //time = System.currentTimeMillis();
            for (int z = 0; z < target.getDepth(); z++) {
                setThirdDimension(source, thirdDimension, z + 1);

                short[] sourceArray = (short[])(source.getProcessor().getPixels());
                System.arraycopy(sourceArray, 0, inputArray, z * numberOfPixelsPerSlice, sourceArray.length);
            }
            //IJ.log("Copy1 took " + (System.currentTimeMillis() - time));

            ShortBuffer byteBuffer = ShortBuffer.wrap(inputArray);
            //time = System.currentTimeMillis();
            target.readFrom(byteBuffer, true);
            //IJ.log("Copy2 took " + (System.currentTimeMillis() - time));
            //IJ.log("conv took " + (System.currentTimeMillis() - time2));
            return target;
        } else  if (source.getBitDepth() == 32) {
            ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.Float);

            float[] inputArray = new float[(int) numberOfPixels];
            for (int z = 0; z < target.getDepth(); z++) {
                setThirdDimension(source, thirdDimension, z + 1);

                float[] sourceArray = (float[])(source.getProcessor().getPixels());
                System.arraycopy(sourceArray, 0, inputArray, z * numberOfPixelsPerSlice, sourceArray.length);
            }
            FloatBuffer byteBuffer = FloatBuffer.wrap(inputArray);
            target.readFrom(byteBuffer, true);
            return target;
        } else {
            return convertLegacy(source);
        }
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
