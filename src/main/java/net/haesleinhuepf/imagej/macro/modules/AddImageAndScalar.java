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
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_addImageAndScalar")
public class AddImageAndScalar extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.addScalar(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asFloat(args[2]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.addScalar(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
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
        return "Adds a scalar value s to all pixels x of a given image X.\n\nf(x, s) = x + s";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
