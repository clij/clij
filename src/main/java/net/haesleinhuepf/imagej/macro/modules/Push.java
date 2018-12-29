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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_push")
public class Push extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        CLIJHandler.getInstance().pushToGPU((String)args[0]);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "String image";
    }

    @Override
    public String getDescription() {
        return "Copies an image specified by its name to GPU memory in order to process it there later.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
