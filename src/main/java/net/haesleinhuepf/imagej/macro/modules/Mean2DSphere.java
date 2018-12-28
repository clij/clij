package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImagePlus;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.imagej.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.imagej.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_mean2DSphere")
public class Mean2DSphere extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        int kernelSizeX = radiusToKernelSize(asInteger(args[2]));
        int kernelSizeY = radiusToKernelSize(asInteger(args[3]));

        if (containsCLImageArguments()) {
            return Kernels.mean(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), kernelSizeX, kernelSizeY);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.mean(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), kernelSizeX, kernelSizeY);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "Computes the local mean average of a pixels ellipsoidal neighborhood. The ellipses size is specified by \n" +
                "its half-width and half-height (radius).";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

}
