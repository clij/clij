package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.imagej.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_downsample2D")
public class Downsample2D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float downsampleX = asFloat(args[2]);
        float downsampleY = asFloat(args[3]);

        if (containsCLImageArguments()) {
            return Kernels.downsample(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), downsampleX, downsampleY);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.downsample(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), downsampleX, downsampleY);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number factorX, Number factorY";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        float downsampleX = asFloat(args[2]);
        float downsampleY = asFloat(args[3]);
        float downsampleZ = asFloat(args[4]);

        return clij.createCLBuffer(new long[]{(long)(input.getWidth() * downsampleX), (long)(input.getHeight() * downsampleY), (long)(input.getDepth() * downsampleZ)}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method\n" +
                "is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
