package net.haesleinhuepf.clij.demo;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.view.Views;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This
 * <p>
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class DifferenceOfGaussianCLDemo {
    public static void main(String... args) throws IOException {
        ImageJ ij = new ImageJ();
        Img img = (Img) ij.io().open("src/main/resources/flybrain.tif");

        RandomAccessibleInterval<UnsignedShortType> input = Views.hyperSlice(img, img.numDimensions()-1, 25);

        RandomAccessibleInterval<UnsignedShortType> output = ij.op().create().img(input);

        ij.ui().show(input);

        CLIJ clij = CLIJ.getInstance("hd"); //CLIJ.getInstance();

        // ---------------------------------------------------------------
        // Example 1: Flip image in X
        {
            ClearCLBuffer srcImage = clij.push(input);
            ClearCLBuffer dstImage = clij.push(output);

            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("src", srcImage);
            parameterMap.put("dst", dstImage);
            parameterMap.put("radius", 6);
            parameterMap.put("sigma_minuend", 1.5f);
            parameterMap.put("sigma_subtrahend", 3f);

            clij.execute("src/main/jython/differenceOfGaussian/differenceOfGaussian.cl", "subtract_convolved_images_2d_fast", parameterMap);

            Img result = clij.pull(dstImage);
            ij.ui().show(result);
        }

    }
}
