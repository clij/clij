package net.haesleinhuepf.clij.macro;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

/**
 * CLIJOpenCLProcessor
 * <p>
 * Author: @haesleinhuepf
 * December 2018
 */
public interface CLIJOpenCLProcessor {
    boolean executeCL();

    ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input);
}
