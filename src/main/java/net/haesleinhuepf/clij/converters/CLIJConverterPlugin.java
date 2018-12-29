package net.haesleinhuepf.clij.converters;

import net.haesleinhuepf.clij.CLIJ;
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
    void setCLIJ(CLIJ clij);
    T convert(S source);
    Class<S> getSourceType();
    Class<T> getTargetType();
}
