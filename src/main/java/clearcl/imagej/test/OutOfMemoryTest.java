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

    ClearCLImage inputA = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);
    ClearCLImage intpuB = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);
    ClearCLImage output = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);

    for (int i = 0; i < 100; i++)
    {
      System.out.println(i);
      ClearCLImage intermediateResultA = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);

      ClearCLImage tempA = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);
      Kernels.nothing(clij, inputA, tempA);
      Kernels.nothing(clij, tempA, inputA);
      Kernels.nothing(clij, inputA, intermediateResultA);
      clij.release(tempA);

      ClearCLImage intermediateResultB = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);

      ClearCLImage tempB = clij.createCLImage(new long[] { 1024, 1024, size }, ImageChannelDataType.Float);
      Kernels.nothing(clij, intpuB, tempB);
      Kernels.nothing(clij, tempB, intpuB);
      Kernels.nothing(clij, intpuB, intermediateResultB);
      clij.release(tempB);

      Kernels.nothing(clij, inputA, intermediateResultA);
      Kernels.nothing(clij, intpuB, intermediateResultB);

      Kernels.nothing(clij, intermediateResultA, output);
      Kernels.nothing(clij, intermediateResultB, output);
      clij.release(tempA);
      clij.release(intermediateResultB);

      //Kernels.tenengradFusion(clij, image1, new float[] { 15, 15, 5 }, image2, image3);
    }

    clij.release(inputA);
    clij.release(intpuB);
    clij.release(output);
  }



}
