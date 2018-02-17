package clearcl.imagej.demo;

import clearcl.ClearCLImage;
import clearcl.imagej.ClearCLIJ;
import fastfuse.tasks.DownsampleXYbyHalfTask;
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
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class ClearCLIJDemo
{

  public static void main(String... args) throws IOException
  {
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

    ClearCLIJ lCLIJ = ClearCLIJ.getInstance();

    // ---------------------------------------------------------------
    // Example 1: Downsampling
    {
      ClearCLImage
          lSrcImage =
          lCLIJ.converter(lInputImg).getClearCLImage();
      ClearCLImage
          lDstImage =
          lCLIJ.converter(lOutputImg).getClearCLImage();

      Map<String, Object> lParameterMap = new HashMap<>();
      lParameterMap.put("src", lSrcImage);
      lParameterMap.put("dst", lDstImage);

      lCLIJ.execute(DownsampleXYbyHalfTask.class,
                    "kernels/downsampling.cl",
                    "downsample_xy_by_half_nearest",
                    lParameterMap);

      RandomAccessibleInterval
          lResultImg =
          lCLIJ.converter(lDstImage).getRandomAccessibleInterval();

      ImageJFunctions.show(lResultImg);
    }

    // ---------------------------------------------------------------
    // Example 1: Bluring
    {
      ClearCLImage
          lSrcImage =
          lCLIJ.converter(lInputImg).getClearCLImage();
      ClearCLImage
          lDstImage =
          lCLIJ.converter(lOutputImg).getClearCLImage();

      HashMap<String, Object> lParameterMap = new HashMap<>();
      lParameterMap.put("Nx", 3);
      lParameterMap.put("Ny", 3);
      lParameterMap.put("Nz", 3);
      lParameterMap.put("sx", 2.0f);
      lParameterMap.put("sy", 2.0f);
      lParameterMap.put("sz", 2.0f);
      lParameterMap.put("src", lSrcImage);
      lParameterMap.put("dst", lDstImage);

      lCLIJ.execute(DownsampleXYbyHalfTask.class,
                                      "kernels/blur.cl",
                                      "gaussian_blur_image3d",
                    lParameterMap);

      RandomAccessibleInterval
          lResultImg =
          lCLIJ.converter(lDstImage).getRandomAccessibleInterval();

      ImageJFunctions.show(lResultImg);
    }

  }
}
