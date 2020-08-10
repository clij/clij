package net.haesleinhuepf.clij.macro;

import ij.ImagePlus;

/**
 * CLIJOpenCLProcessor
 * <p>
 * Author: @haesleinhuepf
 * December 2018
 */
public interface CLIJImageJProcessor {
    boolean executeIJ();

    ImagePlus create(ImagePlus imp);
}
