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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_applyVectorField2D")
public class ApplyVectorField2D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return Kernels.applyVectorfield(clij,
                    (ClearCLImage)( args[0]),
                    (ClearCLImage)( args[1]),
                    (ClearCLImage)( args[2]),
                    (ClearCLImage)(args[3])

            );
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.applyVectorfield(clij,
                    (ClearCLBuffer)( args[0]),
                    (ClearCLBuffer)( args[1]),
                    (ClearCLBuffer)( args[2]),
                    (ClearCLBuffer)(args[3])

            );
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image vectorX, Image vectorY, Image destination";
    }

    @Override
    public String getDescription() {
        return "Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. ";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
