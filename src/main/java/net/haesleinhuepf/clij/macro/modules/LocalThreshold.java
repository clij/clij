package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_localThreshold")
public class LocalThreshold extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.localThreshold(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), (ClearCLImage)(args[2]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.localThreshold(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image localThreshold, Image destination";
    }

    @Override
    public String getDescription() {
        return "Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X \n" +
                "was above of equal to the pixel value m in mask image M.\n\n" +
                "f(x) = (1 if (x >=  m)); (0 otherwise)";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
