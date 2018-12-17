package net.haesleinhuepf.imagej.converters.implementations;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.converters.AbstractCLIJConverter;
import net.haesleinhuepf.imagej.converters.CLIJConverterPlugin;
import net.haesleinhuepf.imagej.converters.ConverterUtilities;
import net.haesleinhuepf.imagej.kernels.Kernels;
import org.scijava.plugin.Plugin;

/**
 * ClearCLImageToClearCLBufferConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLImageToClearCLBufferConverter extends AbstractCLIJConverter<ClearCLImage, ClearCLBuffer> {
    @Override
    public ClearCLBuffer convert(ClearCLImage source) {
        ClearCLBuffer target = clij.createCLBuffer(source.getDimensions(), ConverterUtilities.imageChannelDataTypeToNativeType(source.getChannelDataType()));
        Kernels.copy(clij, source, target);
        return target;
    }

    @Override
    public Class<ClearCLImage> getSourceType() {
        return ClearCLImage.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }

}

