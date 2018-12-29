package net.haesleinhuepf.clij.macro;

import clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.CLIJ;
import org.scijava.plugin.SciJavaPlugin;

public interface CLIJMacroPlugin extends SciJavaPlugin {

    void setClij(CLIJ clij);

    void setArgs(Object[] args);

    String getParameterHelpText();

    ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input);

    String getName();
}
