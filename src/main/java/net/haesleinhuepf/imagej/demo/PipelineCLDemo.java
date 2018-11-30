package net.haesleinhuepf.imagej.demo;

import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This example shows how to take input images, process them through
 * by two OpenCL kernels and show the result
 *
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class PipelineCLDemo
{

  public static void main(String... args) throws IOException
  {
    // Initialize ImageJ and example images
    new ImageJ();
    ImagePlus
        lInputImagePlus =
        IJ.openImage("src/main/resources/flybrain.tif");

    RandomAccessibleInterval<UnsignedShortType>
        lInputImg =
        ImageJFunctions.wrap(lInputImagePlus);

    RandomAccessibleInterval<UnsignedShortType>
        lOutputImg =
        ImageJFunctions.wrap(new Duplicator().run(lInputImagePlus));

    ImageJFunctions.show(lInputImg);

    // Startup OpenCL device, convert images to ClearCL format
    ClearCLIJ lCLIJ = ClearCLIJ.getInstance();

    ClearCLImage
        lClearCLImage1 =
        lCLIJ.converter(lInputImg).getClearCLImage();
    ClearCLImage
        lClearCLImage2 =
        lCLIJ.converter(lOutputImg).getClearCLImage();

    // ---------------------------------------------------------------
    // Example step 1: Downsampling

    Map<String, Object> lParameterMap = new HashMap<>();
    lParameterMap.put("src", lClearCLImage1);
    lParameterMap.put("dst", lClearCLImage2);
    lParameterMap.put("factor_x", 0.5f);
    lParameterMap.put("factor_y", 0.5f);
    lParameterMap.put("factor_z", 1.f);

    lCLIJ.execute(Kernels.class,
                  "downsampling.cl",
                  "downsample_3d_nearest",
                  lParameterMap);


    // Convert/copy and show intermediate result
    RandomAccessibleInterval
        lIntermediateResultImg =
        lCLIJ.converter(lClearCLImage2).getRandomAccessibleInterval();

    ImageJFunctions.show(lIntermediateResultImg);

    // ---------------------------------------------------------------
    // Example Step 2: Bluring
    HashMap<String, Object> lBlurParameterMap = new HashMap<>();
    lBlurParameterMap.put("Nx", 3);
    lBlurParameterMap.put("Ny", 3);
    lBlurParameterMap.put("Nz", 3);
    lBlurParameterMap.put("sx", 2.0f);
    lBlurParameterMap.put("sy", 2.0f);
    lBlurParameterMap.put("sz", 2.0f);
    // we reuse memory in the GPU by taking the result from the former
    // step as input here and the input from the former step as
    // output:
    lBlurParameterMap.put("src", lClearCLImage2);
    lBlurParameterMap.put("dst", lClearCLImage1);

    lCLIJ.execute(Kernels.class,
            "blur.cl",
        "gaussian_blur_image3d",
                  lBlurParameterMap);

    // Convert and show final result
    RandomAccessibleInterval
        lResultImg =
        lCLIJ.converter(lClearCLImage1).getRandomAccessibleInterval();

    ImageJFunctions.show(lResultImg);


  }
}
