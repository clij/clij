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
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_dilateBox")
public class DilateBox extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, CLIJImageJProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.dilateBox(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.dilateBox(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public boolean executeIJ() {
        Object[] args = imageJArgs();
        ImagePlus input = (ImagePlus) args[0];

        IJ.run(input, "Dilate", "");
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
        return "Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.\n" +
                "The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.\n" +
                "The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.\n\n" +
                "This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only\n" +
                "difference is that the output image contains values 0 and 1 instead of 0 and 255.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
