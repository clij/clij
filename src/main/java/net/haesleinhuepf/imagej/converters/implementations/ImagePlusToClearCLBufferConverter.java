package net.haesleinhuepf.imagej.converters.implementations;

import clearcl.ClearCLBuffer;
import ij.ImagePlus;
import net.haesleinhuepf.imagej.converters.AbstractCLIJConverter;
import net.haesleinhuepf.imagej.converters.CLIJConverterPlugin;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.plugin.Plugin;

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
