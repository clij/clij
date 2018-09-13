package clearcl.imagej.test;

import clearcl.ClearCLImage;
import clearcl.enums.ImageChannelDataType;
import clearcl.imagej.ClearCLIJ;
import clearcl.imagej.kernels.Kernels;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.image.Kernel;
import java.util.HashMap;

/**
 * OutOfMemoryTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 09 2018
 */
public class OutOfMemoryTest
{
  @Ignore @Test public void runOutOfMemory_crashes1()
  {
    ClearCLIJ clij = ClearCLIJ.getInstance();

    ClearCLImage image1 = clij.createCLImage(new long[] { 1024, 1024, 50 }, ImageChannelDataType.Float);
    ClearCLImage image2 = clij.createCLImage(new long[] { 1024, 1024, 50 }, ImageChannelDataType.Float);

    for (int i = 0; i < 100; i++)
    {
      System.out.println(i);
      ClearCLImage image3 = clij.createCLImage(new long[] { 1024, 1024, 50 }, ImageChannelDataType.Float);
      Kernels.tenengradFusion(clij, image1, new float[] { 15, 15, 5 }, image2, image3);
      clij.release(image3);

    }
    clij.release(image1);
    clij.release(image2);

  }


  @Ignore @Test public void runOutOfMemory_crashes2()
  {
    ClearCLIJ clij = ClearCLIJ.getInstance();

    ClearCLImage image1 = clij.createCLImage(new long[] { 1024, 1024, 50 }, ImageChannelDataType.Float);
    ClearCLImage image2 = clij.createCLImage(new long[] { 1024, 1024, 50 }, ImageChannelDataType.Float);
    ClearCLImage image3 = clij.createCLImage(new long[] { 1024, 1024, 50 }, ImageChannelDataType.Float);

    for (int i = 0; i < 100; i++)
    {
      System.out.println(i);
      Kernels.tenengradFusion(clij, image1, new float[] { 15, 15, 5 }, image2, image3);
    }

    clij.release(image1);
    clij.release(image2);
    clij.release(image3);
  }


  @Ignore @Test public void runOutOfMemory_does_not_crash()
  {
    ClearCLIJ clij = ClearCLIJ.getInstance();



    for (int i = 0; i < 100; i++)
    {
      System.out.println(i);

      ClearCLImage image1 = clij.createCLImage(new long[] { 1024, 1024, 50 }, ImageChannelDataType.Float);
      ClearCLImage image2 = clij.createCLImage(new long[] { 1024, 1024, 50 }, ImageChannelDataType.Float);
      ClearCLImage image3 = clij.createCLImage(new long[] { 1024, 1024, 50 }, ImageChannelDataType.Float);

      Kernels.tenengradFusion(clij, image1, new float[] { 15, 15, 5 }, image2, image3);

      clij.release(image1);
      clij.release(image2);
      clij.release(image3);
    }

  }


}
