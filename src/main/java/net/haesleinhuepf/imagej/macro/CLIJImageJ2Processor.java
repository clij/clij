package net.haesleinhuepf.imagej.macro;

import net.imagej.ImageJ;

/**
 * CLIJImageJ2Processor
 * <p>
 * Author: @haesleinhuepf
 * December 2018
 */
public interface CLIJImageJ2Processor {
    void setImageJ2(ImageJ ij);
    boolean executeImageJ2();
}
