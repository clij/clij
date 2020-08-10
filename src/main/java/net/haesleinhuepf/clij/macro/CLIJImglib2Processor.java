package net.haesleinhuepf.clij.macro;

import net.imglib2.RandomAccessibleInterval;

/**
 * CLIJImglib2Processor
 * <p>
 * Author: @haesleinhuepf
 *         August 2020
 */
public interface CLIJImglib2Processor {
    boolean executeImglib2();
    RandomAccessibleInterval create();
}
