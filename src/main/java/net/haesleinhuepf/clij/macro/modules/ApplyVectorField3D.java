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

@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_applyVectorField3D")
public class ApplyVectorField3D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments() && clij.hasImageSupport()) {
            return Kernels.applyVectorfield(clij,
                    (ClearCLImage)( args[0]),
                    (ClearCLImage)( args[1]),
                    (ClearCLImage)( args[2]),
                    (ClearCLImage)( args[3]),
                    (ClearCLImage)(args[4])

            );
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.applyVectorfield(clij,
                    (ClearCLBuffer)( args[0]),
                    (ClearCLBuffer)( args[1]),
                    (ClearCLBuffer)( args[2]),
                    (ClearCLBuffer)( args[3]),
                    (ClearCLBuffer)(args[4])

            );
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image vectorX, Image vectorY, Image vectorZ, Image destination";
    }

    @Override
    public String getDescription() {
        return "Deforms an image stack according to distances provided in the given vector image stacks. " +
                "It is recommended to use 32-bit image stacks for input, output and vector image stacks. " +
                "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
