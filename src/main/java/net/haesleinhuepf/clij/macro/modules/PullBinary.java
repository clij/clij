package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * PullBinary
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * August 2019
 */

@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_pullBinary")
public class PullBinary extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        CLIJHandler.getInstance().pullBinaryFromGPU((String)args[0]);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "String image";
    }

    @Override
    public String getDescription() {
        return "Copies a binary image specified by its name from GPU memory back to ImageJ and shows it. " +
                "This binary image will have 0 and 255 pixel intensities as needed for ImageJ to interpret it as binary." +
                "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
