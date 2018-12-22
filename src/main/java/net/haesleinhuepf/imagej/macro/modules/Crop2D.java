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
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_crop2D")
public class Crop2D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.crop(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asInteger(args[2]), asInteger(args[3]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.crop(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number startX, Number startY";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int startX = asInteger(args[2]);
        int startY = asInteger(args[3]);

        return clij.createCLBuffer(new long[]{(long)(input.getWidth() - startX), (long)(input.getHeight() - startY)}, input.getNativeType());
    }

}
