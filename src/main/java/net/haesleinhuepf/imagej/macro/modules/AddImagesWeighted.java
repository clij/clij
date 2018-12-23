package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_addImagesWeighted")
public class AddImagesWeighted extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.addWeightedPixelwise(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), (ClearCLImage)(args[2]), asFloat(args[3]), asFloat(args[3]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.addWeightedPixelwise(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]), asFloat(args[3]), asFloat(args[4]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image summand1, Image summand2, Image destination, Number factor1, Number factor2";
    }
}
