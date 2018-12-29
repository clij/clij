package net.haesleinhuepf.imagej.macro.modules;

import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJHandler;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.imagej.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Release
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_release")
public class Release extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        CLIJHandler.getInstance().releaseBufferInGPU((String)args[0]);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image image";
    }

    @Override
    public String getDescription() {
        return "Frees memory of a specified image in GPU memory.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
