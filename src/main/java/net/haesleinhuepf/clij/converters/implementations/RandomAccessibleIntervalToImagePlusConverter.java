package net.haesleinhuepf.clij.converters.implementations;

import ij.ImagePlus;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.plugin.Plugin;

/**
 * RandomAccessibleIntervalToImagePlusConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class RandomAccessibleIntervalToImagePlusConverter extends AbstractCLIJConverter<RandomAccessibleInterval, ImagePlus> {

    @Override
    public ImagePlus convert(RandomAccessibleInterval source) {
        return ImageJFunctions.wrap(source, "" + source);
    }

    @Override
    public Class<RandomAccessibleInterval> getSourceType() {
        return RandomAccessibleInterval.class;
    }

    @Override
    public Class<ImagePlus> getTargetType() {
        return ImagePlus.class;
    }
}
