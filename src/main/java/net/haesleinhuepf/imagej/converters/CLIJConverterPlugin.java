package net.haesleinhuepf.imagej.converters;

import net.haesleinhuepf.imagej.ClearCLIJ;
import org.scijava.plugin.SciJavaPlugin;

/**
 * CLIJConverterPlugin
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
public interface CLIJConverterPlugin<S, T> extends SciJavaPlugin {
    void setCLIJ(ClearCLIJ clij);
    T convert(S source);
    Class<S> getSourceType();
    Class<T> getTargetType();
}
