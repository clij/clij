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
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_erodeBox")
public class ErodeBoxIJ extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, CLIJImageJProcessor {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.erodeBox(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.erodeBox(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
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

        IJ.run(input, "Erode", "");
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
}
