package clearcl.imagej.demo;

import clearcl.ClearCLImage;
import clearcl.imagej.ClearCLIJ;
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
public class FlipCLDemo
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
      lParameterMap.put("src", lSrcImage);
      lParameterMap.put("dst", lDstImage);
      lParameterMap.put("flipx", 1);
      lParameterMap.put("flipy", 0);
      lParameterMap.put("flipz", 0);

      lCLIJ.execute("src/main/java/clearcl/imagej/demo/kernels/flip.cl",
                    "flip_ui",
                    lParameterMap);

      RandomAccessibleInterval
          lResultImg =
          lCLIJ.converter(lDstImage).getRandomAccessibleInterval();

      ImageJFunctions.show(lResultImg);
    }

  }
}
