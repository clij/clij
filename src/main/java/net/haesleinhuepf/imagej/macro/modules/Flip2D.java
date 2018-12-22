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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_flip2D")
public class Flip2D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor {

    @Override
    public boolean executeCL() {
        Boolean flipX = asBoolean(args[2]);
        Boolean flipY = asBoolean(args[3]);

        if (containsCLImageArguments()) {
            return Kernels.flip(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), flipX, flipY);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.flip(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), flipX, flipY);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Boolean flipX, Boolean flipY";
    }
}
