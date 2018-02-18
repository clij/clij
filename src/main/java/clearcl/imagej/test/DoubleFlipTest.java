package clearcl.imagej.test;

import clearcl.ClearCLImage;
import clearcl.imagej.ClearCLIJ;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.view.Views;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class DoubleFlipTest
{
  ClearCLIJ mCLIJ = null;

  @Test public void testFlipImageTwiceOnAllDevicess() throws
                                                      IOException
  {
    for (String lDeviceName : ClearCLIJ.getAvailableDeviceNames())
    {
      mCLIJ = new ClearCLIJ(lDeviceName);

    }

    ImagePlus lInputImagePlus = IJ.openImage("src/main/resources/flybrain.tif");

    RandomAccessibleInterval<UnsignedShortType>
        lInputImg =
        ImageJFunctions.wrap(lInputImagePlus);

    RandomAccessibleInterval<UnsignedShortType>
        lOutputImg =
        ImageJFunctions.wrap(new Duplicator().run(lInputImagePlus));

    // ---------------------------------------------------------------
    // Example 1: Flip image in X
    {
      ClearCLImage lSrcImage = mCLIJ.converter(lInputImg).getClearCLImage();
      ClearCLImage lDstImage = mCLIJ.converter(lOutputImg).getClearCLImage();

      // flip once
      Map<String, Object> lParameterMap = new HashMap<>();
      lParameterMap.put("src", lSrcImage);
      lParameterMap.put("dst", lDstImage);
      lParameterMap.put("flipx", 1);
      lParameterMap.put("flipy", 0);
      lParameterMap.put("flipz", 0);

      mCLIJ.execute(
          "src/main/java/clearcl/imagej/demo/kernels/flip.cl",
          "flip_ui",
          lParameterMap);

      // flip second time
      lParameterMap = new HashMap<>();
      lParameterMap.put("src", lDstImage);
      lParameterMap.put("dst", lSrcImage);
      lParameterMap.put("flipx", 1);
      lParameterMap.put("flipy", 0);
      lParameterMap.put("flipz", 0);

      mCLIJ.execute(
          "src/main/java/clearcl/imagej/demo/kernels/flip.cl",
          "flip_ui",
          lParameterMap);



      RandomAccessibleInterval<UnsignedShortType>
          lIntermediateResultImg =
          (RandomAccessibleInterval<UnsignedShortType>) mCLIJ.converter(lDstImage).getRandomAccessibleInterval();

      assertFalse(TestUtilities.compareIterableIntervals(Views.iterable(lIntermediateResultImg), Views.iterable(lInputImg)));

      RandomAccessibleInterval<UnsignedShortType>
          lResultImg =
          (RandomAccessibleInterval<UnsignedShortType>) mCLIJ.converter(lSrcImage).getRandomAccessibleInterval();

      assertTrue(TestUtilities.compareIterableIntervals(Views.iterable(lResultImg), Views.iterable(lInputImg)));
    }

  }
}