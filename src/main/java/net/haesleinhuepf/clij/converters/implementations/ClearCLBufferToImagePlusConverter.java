package net.haesleinhuepf.clij.converters.implementations;

import clearcl.ClearCLBuffer;
import ij.ImagePlus;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.plugin.Plugin;

/**
 * ClearCLBufferToImagePlusConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToImagePlusConverter extends AbstractCLIJConverter<ClearCLBuffer, ImagePlus> {

    @Override
    public ImagePlus convert(ClearCLBuffer source) {
        ClearCLBufferToRandomAccessibleIntervalConverter cclbtraic = new ClearCLBufferToRandomAccessibleIntervalConverter();
        cclbtraic.setCLIJ(clij);
        RandomAccessibleInterval rai = cclbtraic.convert(source);
        return ImageJFunctions.wrap(rai, "" + rai);
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }

    @Override
    public Class<ImagePlus> getTargetType() {
        return ImagePlus.class;
    }
}
