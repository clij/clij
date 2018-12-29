package net.haesleinhuepf.clij.converters.implementations;

import ij.ImagePlus;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
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
public class ImagePlusToRandomAccessibleIntervalConverter extends AbstractCLIJConverter<ImagePlus, RandomAccessibleInterval> {

    @Override
    public RandomAccessibleInterval convert(ImagePlus source) {
        return ImageJFunctions.wrapReal(source);
    }

    @Override
    public Class<ImagePlus> getSourceType() {
        return ImagePlus.class;
    }

    @Override
    public Class<RandomAccessibleInterval> getTargetType() {
        return RandomAccessibleInterval.class;
    }
}
