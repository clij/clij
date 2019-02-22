package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_spotDisplacement")
public class SpotDisplacement extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.spotDisplacement(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), (ClearCLImage)(args[2]), asInteger(args[3]), asInteger(args[3]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.spotDisplacement(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]), asInteger(args[3]), asInteger(args[4]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2, Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "For all non-zero pixels in source image 1: Determine the closest non-zero pixel in\n" +
                "source image 2 and write the distance in the result image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
