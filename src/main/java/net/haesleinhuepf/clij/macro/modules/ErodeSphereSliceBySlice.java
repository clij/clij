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
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_erodeSphereSliceBySlice")
public class ErodeSphereSliceBySlice extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments() && clij.hasImageSupport()) {
            return Kernels.erodeSphereSliceBySlice(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.erodeSphereSliceBySlice(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }


    @Override
    public String getDescription() {
        return "Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.\n" +
                "The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.\n" +
                "The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.\n\n" +
                "This filter is applied slice by slice in 2D.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
