package net.haesleinhuepf.imagej.macro.modules;

import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJHandler;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.imagej.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_reportMemory")
public class ReportMemory extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        CLIJHandler.getInstance().reportGPUMemory();
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "";
    }


    @Override
    public String getDescription() {
        return "Prints a list of all images cached in the GPU to ImageJs log window together with a sum of memory \n" +
                "consumption.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "-";
    }
}
