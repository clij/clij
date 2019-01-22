package net.haesleinhuepf.clij.demo;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imagej.ImageJ;
import net.imglib2.img.Img;

import java.io.IOException;

/**
 * LocalThresholdDemo
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2018
 */
public class LocalThresholdDemo {
    public static void main(String... args) throws IOException {
        ImageJ ij = new ImageJ();
        Img img = (Img) ij.io().open("src/main/resources/droso_crop.tif");

        CLIJ clij = CLIJ.getInstance();

        // conversion
        ClearCLBuffer input = clij.push(img);
        ClearCLBuffer output = clij.create(input);
        ClearCLBuffer temp = clij.create(input);

        // blur
        Kernels.blurFast(clij, input, temp, 2, 2, 2);

        // local threshold
        Kernels.localThreshold(clij, input, output, temp);

        Kernels.erodeSphere(clij, output, temp);
        Kernels.erodeSphere(clij, temp, output);

        // show results
        clij.show(input, "original");
        clij.show(output, "mask");

        input.close();
        output.close();
        temp.close();
    }
}
