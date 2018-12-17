package net.haesleinhuepf.imagej.converters.implementations;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.converters.AbstractCLIJConverter;
import net.haesleinhuepf.imagej.converters.CLIJConverterPlugin;
import net.haesleinhuepf.imagej.converters.ConverterUtilities;
import net.haesleinhuepf.imagej.kernels.Kernels;
import org.scijava.plugin.Plugin;

/**
 * ClearCLBufferToClearCLImageConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToClearCLImageConverter extends AbstractCLIJConverter<ClearCLBuffer, ClearCLImage> {
    @Override
    public ClearCLImage convert(ClearCLBuffer source) {
        ClearCLImage target = clij.createCLImage(source.getDimensions(), ConverterUtilities.nativeTypeToImageChannelDataType(source.getNativeType()));
        Kernels.copy(clij, source, target);
        return target;
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }

    @Override
    public Class<ClearCLImage> getTargetType() {
        return ClearCLImage.class;
    }
}
