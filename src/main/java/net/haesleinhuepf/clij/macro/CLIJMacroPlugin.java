package net.haesleinhuepf.clij.macro;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.CLIJ;
import org.scijava.plugin.SciJavaPlugin;

public interface CLIJMacroPlugin extends SciJavaPlugin {

    void setClij(CLIJ clij);

    void setArgs(Object[] args);

    String getParameterHelpText();

    String getName();
}
