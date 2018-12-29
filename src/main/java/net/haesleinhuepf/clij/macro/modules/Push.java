package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
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
