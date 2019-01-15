package net.haesleinhuepf.clij.converters.implementations;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.converters.ConverterUtilities;
import net.haesleinhuepf.clij.kernels.Kernels;
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
        System.out.println("conversion target " + target);
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
