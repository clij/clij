package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.sigmaToKernelSize;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_blur3D")
public class Blur3D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float sigmaX = asFloat(args[2]);
        float sigmaY = asFloat(args[3]);
        float sigmaZ = asFloat(args[4]);
        int nX = sigmaToKernelSize(sigmaX);
        int nY = sigmaToKernelSize(sigmaY);
        int nZ = sigmaToKernelSize(sigmaZ);

        if (containsCLImageArguments()) {
            return Kernels.blur(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), nX, nY, nZ, sigmaX, sigmaY, sigmaZ);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.blur(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), nX, nY, nZ, sigmaX, sigmaY, sigmaZ);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number sigmaX, Number sigmaY, Number sigmaZ";
    }

    @Override
    public String getDescription() {
        return "Computes the Gaussian blurred image of an image given two sigma values in X, Y and Z. Thus, the filter" +
                "kernel can have non-isotropic shape.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
