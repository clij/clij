package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJImageJProcessor;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_threshold")
public class Threshold extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, CLIJImageJProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.threshold(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asFloat(args[2]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.threshold(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number threshold";
    }

    @Override
    public boolean executeIJ() {
        //
        //
        //imp2 = imp.duplicate();

        Object[] args = imageJArgs();
        ImagePlus input = (ImagePlus) args[0];
        int threshold = asInteger(args[2]);

        IJ.setRawThreshold(input, threshold, Double.MAX_VALUE, null);
        IJ.run(input,"Convert to Mask", "");
        input = new Duplicator().run(input);

        ClearCLBuffer result = clij.convert(input, ClearCLBuffer.class);

        if (this.args[1] instanceof ClearCLImage) {
            Kernels.copy(clij, result, (ClearCLImage) this.args[1]);
        } else if (this.args[1] instanceof ClearCLBuffer) {
            Kernels.copy(clij, result, (ClearCLBuffer) this.args[1]);
        } else {
            result.close();
            throw new IllegalArgumentException("argument[1] must be cl_buffer or cl_image!");
        }

        result.close();
        return true;
    }

    @Override
    public String getDescription() {
        return "Computes a binary image with pixel values 0 and 1. All pixel values x of a given input image with \n" +
                "value larger or equal to a given threshold t will be set to 1.\n\n" +
                "f(x,t) = (1 if (x >= t); (0 otherwise))\n\n" +
                "This plugin is comparable to setting a raw threshold in ImageJ and using the 'Convert to Mask' menu.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
