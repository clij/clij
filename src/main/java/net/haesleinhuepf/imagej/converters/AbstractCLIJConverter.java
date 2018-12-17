package net.haesleinhuepf.imagej.converters;

import net.haesleinhuepf.imagej.ClearCLIJ;
import net.imagej.ops.Ops;

/**
 * AbstractCLIJConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
public abstract class AbstractCLIJConverter<S, T> implements CLIJConverterPlugin<S, T>{
    protected ClearCLIJ clij;

    @Override
    public void setCLIJ(ClearCLIJ clij) {
        this.clij = clij;
    }
}
