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
            boolean result = Kernels.invertBinary(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
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
