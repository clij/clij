package net.haesleinhuepf.imagej.converters.implementations;

import clearcl.ClearCLImage;
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
public class ImagePlusToClearCLImageConverter extends AbstractCLIJConverter<ImagePlus, ClearCLImage> {

    @Override
    public ClearCLImage convert(ImagePlus source) {
        RandomAccessibleInterval rai = ImageJFunctions.wrapReal(source);
        RandomAccessibleIntervalToClearCLImageConverter raitcclic = new RandomAccessibleIntervalToClearCLImageConverter();
        raitcclic.setCLIJ(clij);
        return raitcclic.convert(rai);
    }

    @Override
    public Class<ImagePlus> getSourceType() {
        return ImagePlus.class;
    }

    @Override
    public Class<ClearCLImage> getTargetType() {
        return ClearCLImage.class;
    }
}
