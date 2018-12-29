package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.macro.*;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_clear")
public class Clear extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        CLIJHandler.getInstance().clearGPU();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Resets the GPUs memory by deleting all cached images.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "";
    }
}
