package clearcl.imagej.test;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import clearcl.imagej.ClearCLIJ;
import clearcontrol.stack.OffHeapPlanarStack;
import clearcontrol.stack.StackInterface;
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
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This test goes through all available OpenCL devices and test if
 * type conversion works. Therefore, OffHeapPlanarStacks,
 * ClearCLImages and Imglib2 images are converted into each other.
 * Afterwards, it is checked if the images still contain the pixels
 * and are the same size.
 *
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class ImageConverterTest
{
  ClearCLIJ mCLIJ = null;

  @Test
  public void testImgClearCLImageConverter()
  {
    for (String lDeviceName : ClearCLIJ.getAvailableDeviceNames())
    {
      mCLIJ = new ClearCLIJ(lDeviceName);
      System.out.println("Testing device " + lDeviceName);

      RandomAccessibleInterval<FloatType>
          lFloatImg =
          ArrayImgs.floats(new long[] { 5, 6, 7 });
      fillTestImage(lFloatImg);
      testBackAndForthConversionViaCLImage(lFloatImg);
      testBackAndForthConversionViaOffHeapPlanarStack(lFloatImg);
      testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(
          lFloatImg);

      RandomAccessibleInterval<UnsignedByteType>
          lUnsignedByteImg =
          ArrayImgs.unsignedBytes(new long[] { 5, 6, 7 });
      fillTestImage(lUnsignedByteImg);
      testBackAndForthConversionViaCLImage(lUnsignedByteImg);
      testBackAndForthConversionViaOffHeapPlanarStack(
          lUnsignedByteImg);
      testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(
          lUnsignedByteImg);

      RandomAccessibleInterval<ByteType>
          lByteImg =
          ArrayImgs.bytes(new long[] { 5, 6, 7 });
      fillTestImage(lByteImg);
      testBackAndForthConversionViaCLImage(lByteImg);
      testBackAndForthConversionViaOffHeapPlanarStack(lByteImg);
      testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(
          lByteImg);

      RandomAccessibleInterval<UnsignedShortType>
          lUnsignedShortImg =
          ArrayImgs.unsignedShorts(new long[] { 5, 6, 7 });
      fillTestImage(lUnsignedShortImg);
      testBackAndForthConversionViaCLImage(lUnsignedShortImg);
      testBackAndForthConversionViaOffHeapPlanarStack(
          lUnsignedShortImg);
      testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(
          lUnsignedShortImg);

      RandomAccessibleInterval<ShortType>
          lShortImg =
          ArrayImgs.shorts(new long[] { 5, 6, 7 });
      fillTestImage(lShortImg);
      testBackAndForthConversionViaCLImage(lShortImg);
      testBackAndForthConversionViaOffHeapPlanarStack(lShortImg);
      testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(
          lShortImg);
    }
  }

  @Test
  public void testBufferConversion() {


    mCLIJ = new ClearCLIJ("HD");

    RandomAccessibleInterval<FloatType>
            lFloatImg =
            ArrayImgs.floats(new long[] { 5, 6, 7 });
    fillTestImage(lFloatImg);

    ClearCLIJ lCLIJ = mCLIJ;

    ClearCLBuffer lClearCLBuffer = lCLIJ.converter(lFloatImg).getClearCLBuffer();
    RandomAccessibleInterval lRAIconvertedTwice = lCLIJ.converter(lClearCLBuffer).getRandomAccessibleInterval();


    assertTrue(TestUtilities.compareIterableIntervals(Views.iterable(lFloatImg), Views.iterable(lRAIconvertedTwice)));
  }


  private <T extends RealType<T>> void fillTestImage(RandomAccessibleInterval<T> lFloatImg) {
    RandomAccess<T> lRandomAccess = lFloatImg.randomAccess();
    lRandomAccess.setPosition(new long[]{1,2,3});
    lRandomAccess.get().setReal(4);

    if (!(lRandomAccess.get() instanceof ByteType || lRandomAccess.get() instanceof UnsignedByteType)) {
      lRandomAccess.setPosition(new long[]{1, 3, 3});
      lRandomAccess.get().setReal(40000);
    }
  }

  private <T extends RealType<T>> void testBackAndForthConversionViaCLImage(RandomAccessibleInterval<T> lRAI) {
    ClearCLIJ lCLIJ = mCLIJ;

    ClearCLImage lClearCLImage = lCLIJ.converter(lRAI).getClearCLImage();

    RandomAccessibleInterval<T>
        lRAIconvertedTwice =
        (RandomAccessibleInterval<T>) lCLIJ.converter(lClearCLImage).getRandomAccessibleInterval();

    assertTrue(TestUtilities.compareIterableIntervals(Views.iterable(lRAI), Views.iterable(lRAIconvertedTwice)));
  }

  private <T extends RealType<T>> void testBackAndForthConversionViaOffHeapPlanarStack(RandomAccessibleInterval<T> lRAI) {
    ClearCLIJ lCLIJ = mCLIJ;

    StackInterface lOffHeapPlanarStack = lCLIJ.converter(lRAI).getOffHeapPlanarStack();

    RandomAccessibleInterval<T>
        lRAIconvertedTwice =
        (RandomAccessibleInterval<T>) lCLIJ.converter(lOffHeapPlanarStack).getRandomAccessibleInterval();

    assertTrue(TestUtilities.compareIterableIntervals(Views.iterable(lRAI), Views.iterable(lRAIconvertedTwice)));
  }

  private <T extends RealType<T>> void testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(RandomAccessibleInterval<T> lRAI) {
    ClearCLIJ lCLIJ = mCLIJ;

    StackInterface lOffHeapPlanarStack = lCLIJ.converter(lRAI).getOffHeapPlanarStack();

    ClearCLImage lCLImage = lCLIJ.converter(lOffHeapPlanarStack).getClearCLImage();

    RandomAccessibleInterval<T>
        lRAIconvertedTwice =
        (RandomAccessibleInterval<T>) lCLIJ.converter(lCLImage).getRandomAccessibleInterval();

    ClearCLImage lCLImage2 = lCLIJ.converter(lRAIconvertedTwice).getClearCLImage();

    StackInterface lOffHeapPlanarStack2 = lCLIJ.converter(lCLImage2).getOffHeapPlanarStack();

    RandomAccessibleInterval<T> lRAIconverted = lCLIJ.converter(lOffHeapPlanarStack2).getRandomAccessibleInterval();

    assertTrue(TestUtilities.compareIterableIntervals(Views.iterable(lRAI), Views.iterable(lRAIconverted)));
  }

  @Test
  public void convertHugeUnsignedShortImageTest() {
    mCLIJ = new ClearCLIJ("CPU");
    RandomAccessibleInterval<UnsignedShortType> lRAI = ArrayImgs.unsignedShorts(new long[]{10,10});
    RandomAccess<UnsignedShortType> lRA = lRAI.randomAccess();
    lRA.setPosition(new long[]{1,1});
    lRA.get().set(40000);

    testBackAndForthConversionViaOffHeapPlanarStack(lRAI);

    testBackAndForthConversionViaCLImage(lRAI);

    testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(lRAI);
  }


  @Test
  public void convertHugeSignedShortImageTest() {
    mCLIJ = new ClearCLIJ("CPU");
    RandomAccessibleInterval<ShortType> lRAI = ArrayImgs.shorts(new long[]{1,1});
    RandomAccess<ShortType> lRA = lRAI.randomAccess();
    lRA.setPosition(new long[]{0,0});
    lRA.get().setReal(-25400);

    testBackAndForthConversionViaOffHeapPlanarStack(lRAI);

    testBackAndForthConversionViaCLImage(lRAI);

    testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(lRAI);
  }


  @Test
  public void convertHugeFloatImageTest() {
    mCLIJ = new ClearCLIJ("CPU");
    RandomAccessibleInterval<FloatType> lRAI = ArrayImgs.floats(new long[]{1,1});
    RandomAccess<FloatType> lRA = lRAI.randomAccess();
    lRA.setPosition(new long[]{0,0});
    lRA.get().setReal(-25400);

    testBackAndForthConversionViaOffHeapPlanarStack(lRAI);

    testBackAndForthConversionViaCLImage(lRAI);

    testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(lRAI);
  }

}
