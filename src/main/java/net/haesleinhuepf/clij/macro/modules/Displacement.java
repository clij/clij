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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_displacement")
public class Displacement extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.displacement(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), (ClearCLImage)(args[2]), (ClearCLImage)(args[3]), asInteger(args[4]), asInteger(args[5]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.displacement(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]), (ClearCLBuffer)(args[3]), asInteger(args[4]), asInteger(args[5]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2, Image destinationDeltaX, Image destinationDeltaY, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "For every pixel in source image 1, determine the pixel with the most similar intensity in \n" +
                " the local neighborhood with a given radius in source image 2. Write the distance in \n" +
                "X and Y in the two corresponding destination images.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
