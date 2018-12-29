package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.imagej.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_maximumXYZProjection")
public class MaximumXYZProjection extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.maxProjection(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.maxProjection(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]), asInteger(args[3]), asInteger(args[4]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination_max, Number dimensionX, Number dimensionY, Number projectedDimension";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
    }


    @Override
    public String getDescription() {
        return "Determines the maximum projection of an image along a given dimension. Furthermore, the X and Y\n" +
                " dimesions of the resulting image must be specified by the user according to its definition:\n" +
                "X = 0\n" +
                "Y = 1\n" +
                "Z = 2\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
