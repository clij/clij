package net.haesleinhuepf.clij.demo;

import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;

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
        new ImageJ();
        ImagePlus inputImp = IJ.openImage("src/main/resources/flybrain.tif");

        inputImp = new Duplicator().run(inputImp, 25, 25);

        RandomAccessibleInterval<UnsignedShortType> input = ImageJFunctions.wrap(inputImp);

        RandomAccessibleInterval<UnsignedShortType> output = ImageJFunctions.wrap(new Duplicator().run(inputImp));

        ImageJFunctions.show(input);

        CLIJ clij = CLIJ.getInstance("hd"); //CLIJ.getInstance();

        // ---------------------------------------------------------------
        // Example 1: Flip image in X
        {
            ClearCLImage srcImage = clij.convert(input, ClearCLImage.class);
            ClearCLImage dstImage = clij.convert(output, ClearCLImage.class);

            Map<String, Object> lParameterMap = new HashMap<>();
            lParameterMap.put("input", srcImage);
            lParameterMap.put("output", dstImage);
            lParameterMap.put("radius", 6);
            lParameterMap.put("sigma_minuend", 1.5f);
            lParameterMap.put("sigma_subtrahend", 3f);

            clij.execute("src/main/jython/differenceOfGaussian/differenceOfGaussian.cl", "subtract_convolved_images_2d_fast", lParameterMap);

            RandomAccessibleInterval result = clij.convert(dstImage, RandomAccessibleInterval.class);

            ImageJFunctions.show(result);
        }

    }
}
