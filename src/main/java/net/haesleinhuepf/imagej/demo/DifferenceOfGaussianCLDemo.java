package net.haesleinhuepf.imagej.demo;

import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.ClearCLIJ;
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
 * This
 *
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class DifferenceOfGaussianCLDemo
{
  public static void main(String... args) throws IOException
  {
    new ImageJ();
    ImagePlus
        lInputImagePlus =
        IJ.openImage("src/main/resources/flybrain.tif");

    lInputImagePlus = new Duplicator().run(lInputImagePlus, 25,25);

    RandomAccessibleInterval<UnsignedShortType>
        lInputImg =
        ImageJFunctions.wrap(lInputImagePlus);

    RandomAccessibleInterval<UnsignedShortType>
        lOutputImg =
        ImageJFunctions.wrap(new Duplicator().run(lInputImagePlus));

    ImageJFunctions.show(lInputImg);

    ClearCLIJ lCLIJ = new ClearCLIJ("hd"); //ClearCLIJ.getInstance();

    // ---------------------------------------------------------------
    // Example 1: Flip image in X
    {
      ClearCLImage
          lSrcImage =
          lCLIJ.converter(lInputImg).getClearCLImage();
      ClearCLImage
          lDstImage =
          lCLIJ.converter(lOutputImg).getClearCLImage();

      Map<String, Object> lParameterMap = new HashMap<>();
      lParameterMap.put("input",lSrcImage);
      lParameterMap.put("output",lDstImage);
      lParameterMap.put("radius",6);
      lParameterMap.put("sigma_minuend",1.5f);
      lParameterMap.put("sigma_subtrahend",3f);

      lCLIJ.execute("src/main/jython/differenceOfGaussian/differenceOfGaussian.cl", "subtract_convolved_images_2d_fast", lParameterMap);

      RandomAccessibleInterval
          lResultImg =
          lCLIJ.converter(lDstImage).getRandomAccessibleInterval();

      ImageJFunctions.show(lResultImg);
    }

  }
}
