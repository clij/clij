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

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_blur2DIJ")
public class Blur2DIJ extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, CLIJImageJProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float sigma = asFloat(args[2]);

        if (containsCLImageArguments()) {
            return Kernels.blurIJ(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), sigma);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.blurIJ(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), sigma);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number sigma";
    }

    @Override
    public boolean executeIJ() {
        Object[] args = imageJArgs();
        ImagePlus input = (ImagePlus) args[0];
        int sigma = asInteger(args[2]);

        IJ.run(input, "Gaussian Blur...", "sigma=" + sigma);
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
        return "Computes the Gaussian blurred image of an image given a sigma.\n\n" +
                "The implementation is close to ImageJs Gaussian blur filter. Differences in pixel values compared to \n" +
                "ImageJ of up to 0.5% need to be tolerated.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
