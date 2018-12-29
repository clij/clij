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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_invert")
public class Invert extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.invert(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]));
        } else {
            // convert everything to images
            Object[] args = openCLImageArgs();
            boolean result = Kernels.invert(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
            // copy result back to the buffer
            Kernels.copy(clij, (ClearCLImage)args[1], (ClearCLBuffer)this.args[1]);
            // cleanup
            releaseImages(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Computes the negative value of all pixels in a given image. It is recommended to convert images to \n" +
                "32-bit float before applying this operation.\n\n" +
                "f(x) = - x\n\n" +
                "For binary images, use binaryNot.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
