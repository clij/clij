package net.haesleinhuepf.clij.test;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.converters.implementations.RandomAccessibleIntervalToClearCLImageConverter;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This test goes through all available OpenCL devices and test if
 * type conversion works. Therefore, OffHeapPlanarStacks,
 * ClearCLImages and Imglib2 images are converted into each other.
 * Afterwards, it is checked if the images still contain the pixels
 * and are the same size.
 * <p>
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class ImageConverterTest
{

  CLIJ clij = null;

  @Test
  public void testSimpleCopyBackAndForth() {
    for (int i = 0; i < 2; i++) {
      ImagePlus imp = NewImage.createShortImage("title", 512, 512, 20, NewImage.FILL_RAMP);
      CLIJ clij = CLIJ.getInstance();

      long time = System.currentTimeMillis();
      ClearCLBuffer buffer1 = clij.convert(imp, ClearCLBuffer.class);
              //converter(imp).getClearCLBuffer();
      System.out.println("Forth took " + (System.currentTimeMillis() - time) + " msec");
      ClearCLBuffer buffer2 = clij.createCLBuffer(buffer1.getDimensions(), buffer1.getNativeType());

      Kernels.copy(clij, buffer1, buffer2);


      time = System.currentTimeMillis();
      ImagePlus result = clij.convert(buffer2, ImagePlus.class);
              //converter(buffer2).getImagePlus();
      System.out.println("Back took " + (System.currentTimeMillis() - time) + " msec");
      result.show();
    }
  }

  @Test
  public void testImgClearCLImageConverter()
  {
    for (String deviceName : CLIJ.getAvailableDeviceNames())
    {
      clij = CLIJ.getInstance(deviceName);
      System.out.println("Testing device " + deviceName);

      RandomAccessibleInterval<FloatType>
          lFloatImg =
          ArrayImgs.floats(new long[] { 5, 6, 7 });
      fillTestImage(lFloatImg);
      testBackAndForthConversionViaCLImage(lFloatImg);

      RandomAccessibleInterval<UnsignedByteType>
          lUnsignedByteImg =
          ArrayImgs.unsignedBytes(new long[] { 5, 6, 7 });
      fillTestImage(lUnsignedByteImg);
      testBackAndForthConversionViaCLImage(lUnsignedByteImg);

      RandomAccessibleInterval<ByteType>
          lByteImg =
          ArrayImgs.bytes(new long[] { 5, 6, 7 });
      fillTestImage(lByteImg);
      testBackAndForthConversionViaCLImage(lByteImg);

      RandomAccessibleInterval<UnsignedShortType>
          lUnsignedShortImg =
          ArrayImgs.unsignedShorts(new long[] { 5, 6, 7 });
      fillTestImage(lUnsignedShortImg);
      testBackAndForthConversionViaCLImage(lUnsignedShortImg);

      RandomAccessibleInterval<ShortType>
          lShortImg =
          ArrayImgs.shorts(new long[] { 5, 6, 7 });
      fillTestImage(lShortImg);
      testBackAndForthConversionViaCLImage(lShortImg);

        clij.close();

    }

      IJ.exit();
    }

  @Test public void testBufferConversion()
  {
    clij = CLIJ.getInstance();

    RandomAccessibleInterval<FloatType>
        lFloatImg =
        ArrayImgs.floats(new long[] { 5, 6, 7 });
    fillTestImage(lFloatImg);

    CLIJ lCLIJ = clij;

    ClearCLBuffer
        lClearCLBuffer =
        lCLIJ.convert(lFloatImg, ClearCLBuffer.class);
    RandomAccessibleInterval
        lRAIconvertedTwice =
        lCLIJ.convert(lFloatImg, RandomAccessibleInterval.class);

    assertTrue(TestUtilities.compareIterableIntervals(Views.iterable(
        lFloatImg), Views.iterable(lRAIconvertedTwice)));

    lClearCLBuffer.close();


      IJ.exit();
      clij.close();
  }

  private <T extends RealType<T>> void fillTestImage(
      RandomAccessibleInterval<T> lFloatImg)
  {
    RandomAccess<T> lRandomAccess = lFloatImg.randomAccess();
    lRandomAccess.setPosition(new long[] { 1, 2, 3 });
    lRandomAccess.get().setReal(4);

    if (!(lRandomAccess.get() instanceof ByteType
          || lRandomAccess.get() instanceof UnsignedByteType))
    {
      lRandomAccess.setPosition(new long[] { 1, 3, 3 });
      lRandomAccess.get().setReal(40000);
    }
  }

  private <T extends RealType<T>> void testBackAndForthConversionViaCLImage(
      RandomAccessibleInterval<T> lRAI)
  {
    ClearCLImage
        lClearCLImage =
        clij.convert(lRAI, ClearCLImage.class);
                //converter(lRAI).getClearCLImage();

    RandomAccessibleInterval<T>
        lRAIconvertedTwice =
        (RandomAccessibleInterval<T>) clij.convert(lClearCLImage, RandomAccessibleInterval.class);

    assertTrue(TestUtilities.compareIterableIntervals(Views.iterable(
        lRAI), Views.iterable(lRAIconvertedTwice)));

    lClearCLImage.close();
  }


  @Test public void convertHugeUnsignedShortImageTest()
  {
    clij = CLIJ.getInstance();
    RandomAccessibleInterval<UnsignedShortType>
        lRAI =
        ArrayImgs.unsignedShorts(new long[] { 10, 10 });
    RandomAccess<UnsignedShortType> lRA = lRAI.randomAccess();
    lRA.setPosition(new long[] { 1, 1 });
    lRA.get().set(40000);


    testBackAndForthConversionViaCLImage(lRAI);

      IJ.exit();
      clij.close();

  }

  @Test public void convertHugeSignedShortImageTest()
  {
    clij = CLIJ.getInstance();
    RandomAccessibleInterval<ShortType>
        lRAI =
        ArrayImgs.shorts(new long[] { 1, 1 });
    RandomAccess<ShortType> lRA = lRAI.randomAccess();
    lRA.setPosition(new long[] { 0, 0 });
    lRA.get().setReal(-25400);


    testBackAndForthConversionViaCLImage(lRAI);

      IJ.exit();
      clij.close();

  }

  @Test public void convertHugeFloatImageTest()
  {
    clij = CLIJ.getInstance();
    RandomAccessibleInterval<FloatType>
        lRAI =
        ArrayImgs.floats(new long[] { 1, 1 });
    RandomAccess<FloatType> lRA = lRAI.randomAccess();
    lRA.setPosition(new long[] { 0, 0 });
    lRA.get().setReal(-25400);

    testBackAndForthConversionViaCLImage(lRAI);

      IJ.exit();
      clij.close();
  }


    @Test
    public void testConversionUnsignedShortStackToFloatCLImage() {
        CLIJ.debug = true;
        clij = CLIJ.getInstance();

        RandomAccessibleInterval<UnsignedShortType> rai = ArrayImgs.unsignedShorts(new long[] { 3, 3 });

        RandomAccess<UnsignedShortType> ra = rai.randomAccess();
        ra.setPosition(new int[] {1,1});
        ra.get().set(4);

        ClearCLImage stack = clij.convert(rai, ClearCLImage.class);

        // test starts here
        RandomAccessibleInterval rai2 = clij.convert(stack, RandomAccessibleInterval.class);

        ClearCLImage clImage = clij.createCLImage(stack.getDimensions(), ImageChannelDataType.Float);
        RandomAccessibleIntervalToClearCLImageConverter.copyRandomAccessibleIntervalToClearCLImage(rai2, clImage);

        ClearCLImage clImage2 = clij.createCLImage(stack.getDimensions(), ImageChannelDataType.UnsignedInt16);

        Kernels.copy(clij, clImage, clImage2);

        RandomAccessibleInterval<UnsignedShortType> rai3 = (RandomAccessibleInterval<UnsignedShortType>) clij.convert(clImage2, RandomAccessibleInterval.class);

        assertTrue(TestUtilities.compareIterableIntervals(Views.iterable(rai2), Views.iterable(rai3)));

        IJ.exit();
        clij.close();

    }

}
