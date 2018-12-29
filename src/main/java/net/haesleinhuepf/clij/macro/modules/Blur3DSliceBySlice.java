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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_blur3DSliceBySlice")
public class Blur3DSliceBySlice extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float sigmaX = asFloat(args[2]);
        float sigmaY = asFloat(args[3]);
        int nX = sigmaToKernelSize(sigmaX);
        int nY = sigmaToKernelSize(sigmaY);

        if (containsCLBufferArguments()) {
            // convert all arguments to CLImages
            Object[] args = openCLImageArgs();
            boolean result = Kernels.blurSliceBySlice(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), nX, nY, sigmaX, sigmaY);
            // copy result back to the bufffer
            Kernels.copy(clij, (ClearCLImage)args[1], (ClearCLBuffer)this.args[1]);
            // cleanup
            releaseImages(args);
            return result;
        } else {
            return Kernels.blurSliceBySlice(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), nX, nY, sigmaX, sigmaY);
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number sigmaX, Number sigmaY";
    }


    @Override
    public String getDescription() {
        return "Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filter" +
                "kernel can have non-isotropic shape.\n\n" +
                "" +
                "The Gaussian blur is applied slice by slice in 2D.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
