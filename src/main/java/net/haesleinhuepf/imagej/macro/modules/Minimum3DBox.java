package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_minimum3DBox")
public class Minimum3DBox extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor {

    @Override
    public boolean executeCL() {
        int radiusX = asInteger(args[2]);
        int radiusY = asInteger(args[3]);
        int radiusZ = asInteger(args[4]);

        if (containsCLBufferArguments()) {
            boolean result = Kernels.minimumSeparable(clij, (ClearCLBuffer) (args[0]), (ClearCLBuffer) (args[1]), radiusX, radiusY, radiusZ);
            return result;
        } else {
            return Kernels.minimumSeparable(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), radiusX, radiusY, radiusZ);
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }
}
