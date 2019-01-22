package net.haesleinhuepf.clij.demo;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.imagej.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
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
        ImageJ ij = new ImageJ();

        RandomAccessibleInterval<UnsignedShortType> input = (Img) ij.io().open("src/main/resources/flybrain.tif");

        RandomAccessibleInterval<UnsignedShortType> output = ij.op().create().img(input);

        ij.ui().show(input);

        CLIJ clij = CLIJ.getInstance("hd");

        // ---------------------------------------------------------------
        // Example 1: Flip image in X
        {
            ClearCLBuffer srcImage = clij.push(input);
            ClearCLBuffer dstImage = clij.push(output);

            Map<String, Object> lParameterMap = new HashMap<>();
            lParameterMap.put("src", srcImage);
            lParameterMap.put("dst", dstImage);
            lParameterMap.put("flipx", 1);
            lParameterMap.put("flipy", 0);
            lParameterMap.put("flipz", 0);

            clij.execute("src/main/java/net/haesleinhuepf/clij/kernels/flip.cl", "flip_3d", lParameterMap);

            Img result = clij.pull(dstImage);
            ij.ui().show(result);
        }

    }
}
