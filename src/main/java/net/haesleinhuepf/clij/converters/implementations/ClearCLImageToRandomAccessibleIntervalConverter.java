package net.haesleinhuepf.clij.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLContext;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.coremem.ContiguousMemoryInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.coremem.offheap.OffHeapMemory;
import net.haesleinhuepf.clij.coremem.offheap.OffHeapMemoryAccess;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

import java.util.UnknownFormatConversionException;

/**
 * ClearCLImageToRandomAccessibleIntervalConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLImageToRandomAccessibleIntervalConverter extends AbstractCLIJConverter<ClearCLImage, RandomAccessibleInterval> {
    @Override
    public RandomAccessibleInterval convert(ClearCLImage source) {

        long[] dimensions = new long[]{
                source.getWidth(),
                source.getHeight(),
                1,
                source.getDepth(),
                1
        };

        Img rai = null;

        if (source.getChannelDataType()
                == ImageChannelDataType.SignedInt8) {
            rai = ArrayImgs.bytes(dimensions);
        } else if (source.getChannelDataType() == ImageChannelDataType.UnsignedInt8 ||
                   source.getChannelDataType() == ImageChannelDataType.UnsignedNormalizedInt8) {
            rai = ArrayImgs.unsignedBytes(dimensions);
        } else if (source.getChannelDataType() == ImageChannelDataType.SignedInt16) {
            rai = ArrayImgs.shorts(dimensions);
        } else if (source.getChannelDataType() == ImageChannelDataType.UnsignedInt16 ||
                   source.getChannelDataType() == ImageChannelDataType.UnsignedNormalizedInt16) {
            rai = ArrayImgs.unsignedShorts(dimensions);
        } else if (source.getChannelDataType() == ImageChannelDataType.Float) {
            rai = ArrayImgs.floats(dimensions);
        } else {
            throw new UnknownFormatConversionException(
                    "Cannot convert image of type "
                            + source.getChannelDataType().name());
        }

        convertClearClImageToRandomAccessibleInterval(clij.getClearCLContext(), source, rai);

        return rai;
    }
    public static <T extends RealType<T>> void convertClearClImageToRandomAccessibleInterval(ClearCLContext pContext, ClearCLImage source, RandomAccessibleInterval rai) {

        NativeTypeEnum inputType = source.getNativeType();

        long bytesPerPixel = inputType.getSizeInBytes();
        long numberOfPixels = source.getWidth() * source.getHeight() * source.getDepth();

        ClearCLBuffer buffer = pContext.createBuffer(inputType, numberOfPixels);

        source.copyTo(buffer, true);

        copyClBufferToImg(buffer, rai, bytesPerPixel, numberOfPixels);
        buffer.close();
    }

    private static <T extends RealType<T>> void copyClBufferToImg(ClearCLBuffer buffer, RandomAccessibleInterval rai, long bytesPerPixel, long numberOfPixels) {
        NativeTypeEnum inputType = buffer.getNativeType();

        long numberOfBytesToAllocate = bytesPerPixel * numberOfPixels;

        ContiguousMemoryInterface contOut = new OffHeapMemory("memmm", null, OffHeapMemoryAccess.allocateMemory(numberOfBytesToAllocate), numberOfBytesToAllocate);

        buffer.writeTo(contOut, true);

        int memoryOffset = 0;
        Cursor<T> cursor = Views.iterable(rai).cursor();

        if (inputType == NativeTypeEnum.Byte
                || inputType == NativeTypeEnum.UnsignedByte) {
            while (cursor.hasNext()) {
                cursor.next().setReal(contOut.getByte(memoryOffset));
                memoryOffset += bytesPerPixel;
            }
        } else if (inputType == NativeTypeEnum.Short
                || inputType == NativeTypeEnum.UnsignedShort) {
            while (cursor.hasNext()) {
                cursor.next().setReal(contOut.getShort(memoryOffset));
                memoryOffset += bytesPerPixel;
            }
        } else if (inputType == NativeTypeEnum.Float) {
            while (cursor.hasNext()) {
                cursor.next().setReal(contOut.getFloat(memoryOffset));
                memoryOffset += bytesPerPixel;
            }
        } else {
            throw new UnknownFormatConversionException(
                    "Cannot convert object of type " + inputType.getClass()
                            .getCanonicalName());
        }
        contOut.free();
    }

    @Override
    public Class<ClearCLImage> getSourceType() {
        return ClearCLImage.class;
    }

    @Override
    public Class<RandomAccessibleInterval> getTargetType() {
        return RandomAccessibleInterval.class;
    }
}
