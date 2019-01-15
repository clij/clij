package net.haesleinhuepf.clij.test;

import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.view.Views;
import org.junit.Ignore;
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
  public static void main(String... args) throws IOException
  {
    new ImageJ();
    new DoubleFlipTest().testFlipImageTwiceOnAllDevices();
  }

  @Test
  public void testFlipImageTwiceOnAllDevices() throws
                                                     IOException
  {
    for (String deviceName : CLIJ.getAvailableDeviceNames())
    {
      CLIJ clij = CLIJ.getInstance(deviceName);
      System.out.println("Testing " + deviceName);

      ImagePlus inputImagePlus = IJ.openImage("src/main/resources/flybrain.tif");

      ClearCLImage clImage = clij.convert(inputImagePlus, ClearCLImage.class);

      RandomAccessibleInterval inputImagePlus2 = clij.convert(clImage, RandomAccessibleInterval.class);

      assertTrue(TestUtilities.compareIterableIntervals(Views.iterable(inputImagePlus2), ImageJFunctions.<UnsignedShortType>wrap(inputImagePlus)));

      RandomAccessibleInterval<UnsignedShortType> inputImg = ImageJFunctions.wrap(inputImagePlus);

      RandomAccessibleInterval<UnsignedShortType> outputImg = ImageJFunctions.wrap(new Duplicator().run(inputImagePlus));

      ClearCLImage srcCLImage = clij.convert(inputImg, ClearCLImage.class);
      ClearCLImage dstCLImage = clij.convert(outputImg, ClearCLImage.class);

      // flip once
      Kernels.flip(clij, srcCLImage, dstCLImage, true, false, false);

      // flip second time
      Map<String, Object> parameterMap = new HashMap<>();
      parameterMap.put("src", dstCLImage);
      parameterMap.put("dst", srcCLImage);
      parameterMap.put("flipx", 1);
      parameterMap.put("flipy", 0);
      parameterMap.put("flipz", 0);

      clij.execute("src/main/java/net/haesleinhuepf/clij/kernels/flip.cl", "flip_3d", parameterMap);

      RandomAccessibleInterval<UnsignedShortType> lIntermediateResultImg = (RandomAccessibleInterval<UnsignedShortType>) clij.convert(dstCLImage, RandomAccessibleInterval.class);

      System.out.println("Should be different:");
      assertFalse(TestUtilities.compareIterableIntervals(
              Views.iterable(lIntermediateResultImg),
              Views.iterable(inputImg)));

      RandomAccessibleInterval<UnsignedShortType> resultImg = (RandomAccessibleInterval<UnsignedShortType>) clij.convert(srcCLImage, RandomAccessibleInterval.class);

      System.out.println("Should be same:");
      assertTrue(TestUtilities.compareIterableIntervals(Views.iterable(resultImg), Views.iterable(inputImg)));
      ImageJFunctions.show(resultImg, "res " + deviceName);
      ImageJFunctions.show(inputImg, "inp " + deviceName);

      clImage.close();
      srcCLImage.close();
      dstCLImage.close();
      clij.close();
    }

    IJ.exit();
  }
}