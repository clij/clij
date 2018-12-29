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
public class FlipCLDemo {
    public static void main(String... args) throws IOException {
        new ImageJ();
        ImagePlus lInputImagePlus = IJ.openImage("src/main/resources/flybrain.tif");

        RandomAccessibleInterval<UnsignedShortType> input = ImageJFunctions.wrap(lInputImagePlus);

        RandomAccessibleInterval<UnsignedShortType> output = ImageJFunctions.wrap(new Duplicator().run(lInputImagePlus));

        ImageJFunctions.show(input);

        CLIJ clij = CLIJ.getInstance("hd");

        // ---------------------------------------------------------------
        // Example 1: Flip image in X
        {
            ClearCLImage srcImage = clij.convert(input, ClearCLImage.class);
            ClearCLImage dstImage = clij.convert(output, ClearCLImage.class);

            Map<String, Object> lParameterMap = new HashMap<>();
            lParameterMap.put("src", srcImage);
            lParameterMap.put("dst", dstImage);
            lParameterMap.put("flipx", 1);
            lParameterMap.put("flipy", 0);
            lParameterMap.put("flipz", 0);

            clij.execute("src/main/java/net/haesleinhuepf/clij/kernels/flip.cl", "flip_3d", lParameterMap);

            RandomAccessibleInterval result = clij.convert(dstImage, RandomAccessibleInterval.class);

            ImageJFunctions.show(result);
        }

    }
}
