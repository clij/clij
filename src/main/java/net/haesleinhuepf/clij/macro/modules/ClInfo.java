package net.haesleinhuepf.clij.macro.modules;

import ij.IJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_clInfo")
public class ClInfo extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        IJ.log(CLIJ.clinfo());
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Outputs information about available OpenCL devices." +
                "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "";
    }
}
