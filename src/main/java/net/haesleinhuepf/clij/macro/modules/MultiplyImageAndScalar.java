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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_multiplyImageAndScalar")
public class MultiplyImageAndScalar extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.multiplyImageAndScalar(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asFloat(args[2]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.multiplyImageAndScalar(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number scalar";
    }

    @Override
    public String getDescription() {
        return "Multiplies all pixels value x in a given image X with a constant scalar s.\n\nf(x, s) = x * s";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
