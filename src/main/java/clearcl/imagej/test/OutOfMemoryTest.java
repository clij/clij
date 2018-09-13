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


  @Ignore @Test public void runOutOfMemory_simplified_crashes()
  {
    int size = 25;
    ClearCLIJ clij = ClearCLIJ.getInstance();

    ClearCLImage image1 = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);
    ClearCLImage image5 = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);
    ClearCLImage image7 = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);

    for (int i = 0; i < 100; i++)
    {
      System.out.println(i);
      ClearCLImage image3 = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);

      ClearCLImage image2 = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);
      Kernels.nothing(clij, image1, image2);
      Kernels.nothing(clij, image2, image1);
      Kernels.nothing(clij, image1, image3);
      clij.release(image2);

      ClearCLImage image6 = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);

      ClearCLImage image4 = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);
      Kernels.nothing(clij, image5, image4);
      Kernels.nothing(clij, image4, image5);
      Kernels.nothing(clij, image5, image6);
      clij.release(image4);

      Kernels.nothing(clij, image1, image3);
      Kernels.nothing(clij, image5, image6);

      Kernels.nothing(clij, image3, image7);
      Kernels.nothing(clij, image6, image7);
      clij.release(image2);
      clij.release(image6);

      //Kernels.tenengradFusion(clij, image1, new float[] { 15, 15, 5 }, image2, image3);
    }

    clij.release(image1);
    clij.release(image5);
    clij.release(image7);
  }



}
