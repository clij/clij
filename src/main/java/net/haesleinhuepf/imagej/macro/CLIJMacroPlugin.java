package net.haesleinhuepf.imagej.macro;

import net.haesleinhuepf.imagej.ClearCLIJ;
import org.scijava.plugin.SciJavaPlugin;

public interface CLIJMacroPlugin extends SciJavaPlugin {

    void setClij(ClearCLIJ clij);

    void setArgs(Object[] args);

    String getParameterHelpText();

}
