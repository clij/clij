package net.haesleinhuepf.imagej.demo;

import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import ij.IJ;
import ij.ImagePlus;

/**
 * LocalThresholdDemo
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2018
 */
public class LocalThresholdDemo {
    public static void main(String... args) {
        ImagePlus imp = IJ.openImage("src/main/resources/droso_crop.tif");

        ClearCLIJ clij = ClearCLIJ.getInstance();

        // conversion
        ClearCLImage input = clij.converter(imp).getClearCLImage();
        ClearCLImage output = clij.createCLImage(input);
        ClearCLImage temp = clij.createCLImage(input);

        // blur
        Kernels.blurSeparable(clij, input, temp, new float[]{2, 2, 2});

        // local threshold
        Kernels.localThreshold(clij, input, output, temp);

        Kernels.erode(clij, output, temp);
        Kernels.erode(clij, temp, output);

        // show results
        clij.show(input, "original");
        clij.show(output, "mask");

        input.close();
        output.close();
        temp.close();
    }
}
