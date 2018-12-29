package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJImageJProcessor;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.imagej.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.imagej.utilities.CLIJUtilities.sigmaToKernelSize;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_blur2D")
public class Blur2D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float sigmaX = asFloat(args[2]);
        float sigmaY = asFloat(args[3]);
        int nX = sigmaToKernelSize(sigmaX);
        int nY = sigmaToKernelSize(sigmaY);

        if (containsCLImageArguments()) {
            return Kernels.blur(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), nX, nY, sigmaX, sigmaY);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.blur(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), nX, nY, sigmaX, sigmaY);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number sigmaX, Number sigmaY";
    }

    @Override
    public String getDescription() {
        return "Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filter" +
                "kernel can have non-isotropic shape.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
