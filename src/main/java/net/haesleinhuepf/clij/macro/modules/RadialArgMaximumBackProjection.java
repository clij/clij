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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_radialArgMaximumBackProjection")
public class RadialArgMaximumBackProjection extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        float deltaAngle = asFloat(args[3]);
        float deltaRadius = asFloat(args[4]);

        if (containsCLImageArguments()) {
            return Kernels.radialArgMaximumBackProjection(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), (ClearCLImage)(args[2]), deltaAngle, deltaRadius);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.radialArgMaximumBackProjection(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]), deltaAngle, deltaRadius);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image intensityImage, Image radiusImage, Image destination, Number deltaAngle, Number deltaRadius";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getAvailableForDimensions() {
        return "[2D,2D] -> 3D";
    }
}
