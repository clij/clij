package net.haesleinhuepf.clij.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.ImagePlus;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.plugin.Plugin;

/**
 * ClearCLImageToImagePlusConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLImageToImagePlusConverter extends AbstractCLIJConverter<ClearCLImage, ImagePlus> {

    @Override
    public ImagePlus convert(ClearCLImage source) {
        ClearCLImageToRandomAccessibleIntervalConverter cclitraic = new ClearCLImageToRandomAccessibleIntervalConverter();
        cclitraic.setCLIJ(clij);
        RandomAccessibleInterval rai = cclitraic.convert(source);
        return ImageJFunctions.wrap(rai, "" + rai);
    }

    @Override
    public Class<ClearCLImage> getSourceType() {
        return ClearCLImage.class;
    }

    @Override
    public Class<ImagePlus> getTargetType() {
        return ImagePlus.class;
    }
}

