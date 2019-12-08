package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_countNonZeroVoxels3DSphere")
public class CountNonZeroVoxels3DSphere extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments() && clij.hasImageSupport()) {
            return Kernels.countNonZeroVoxelsLocally(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.countNonZeroVoxelsLocally(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }

    @Override
    public String getDescription() {
        return "Counts non-zero voxels in a sphere around every voxel." +
                "Put the number in the result image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
