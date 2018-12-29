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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_detectMinimaSliceBySliceBox")
public class DetectMinimaSliceBySliceBox extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.detectMinimaSliceBySlice(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asInteger(args[2]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.detectMinimaSliceBySlice(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]));
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
