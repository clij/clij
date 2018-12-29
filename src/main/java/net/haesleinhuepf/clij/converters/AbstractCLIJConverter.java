package net.haesleinhuepf.clij.converters;

import net.haesleinhuepf.clij.CLIJ;

/**
 * AbstractCLIJConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
public abstract class AbstractCLIJConverter<S, T> implements CLIJConverterPlugin<S, T>{
    protected CLIJ clij;

    @Override
    public void setCLIJ(CLIJ clij) {
        this.clij = clij;
    }
}
