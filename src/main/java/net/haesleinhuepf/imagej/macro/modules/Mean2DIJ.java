package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.Duplicator;
import ij.plugin.PlugIn;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJImageJProcessor;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_mean2dIJ")
public class Mean2DIJ extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, CLIJImageJProcessor {

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);

        if (containsCLImageArguments()) {
            return Kernels.meanIJ(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), radiusX);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.meanIJ(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), radiusX);
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

        IJ.run(input, "Mean...", "radius=" + radius);
        input = new Duplicator().run(input);
        input.show();
        System.out.println("input: " + input.getTitle());

        ClearCLBuffer result = clij.convert(input, ClearCLBuffer.class);
        clij.show(result, "result");

        if (this.args[1] instanceof ClearCLImage) {
            Kernels.copy(clij, result, (ClearCLImage) this.args[1]);
        } else if (this.args[1] instanceof ClearCLBuffer) {
            Kernels.copy(clij, result, (ClearCLBuffer) this.args[1]);
        } else {
            result.close();
            throw new IllegalArgumentException("argument[1] must be cl_buffer or cl_image!");
        }

        clij.show((ClearCLImage)(this.args[1]), "args[1] " + this.args[1]);

        result.close();
        return true;
    }

}
