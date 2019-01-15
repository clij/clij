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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_detectMinimaSliceBySliceBox")
public class DetectMinimaSliceBySliceBox extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.detectMinimaSliceBySliceBox(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asInteger(args[2]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.detectMinimaSliceBySliceBox(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radius";
    }

    @Override
    public String getDescription() {
        return "Detects local minima in a given square neighborhood of an input image stack. The input image stack is \n" +
                "processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a \n" +
                "given radius which has a lower intensity, and to 0 otherwise.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
