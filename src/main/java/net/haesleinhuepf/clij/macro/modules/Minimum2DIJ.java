package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_minimum2DIJ")
public class Minimum2DIJ extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, CLIJImageJProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);

        if (containsCLImageArguments()) {
            return Kernels.minimumIJ(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), radiusX);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.minimumIJ(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), radiusX);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radius";
    }

    @Override
    public boolean executeIJ() {
        Object[] args = imageJArgs();
        ImagePlus input = (ImagePlus) args[0];
        int radius = asInteger(args[2]);

        IJ.run(input, "Minimum...", "radius=" + radius);
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
        return "Computes the local minimumSphere of a pixels circle-like neighborhood. The circle size is specified by \n" +
                "its radius.\n\n" +
                "This operation is equal to ImageJs 'Minimum...' menu.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
