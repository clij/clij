package clearcl.imagej.test;

import clearcl.ClearCLImage;
import clearcl.imagej.ClearCLIJ;
import clearcontrol.stack.OffHeapPlanarStack;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
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
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class ImageConverterTest
{
  @Test
  public void testImgClearCLImageConverter()
  {
    RandomAccessibleInterval<FloatType>
        lFloatImg = ArrayImgs.floats(new long[] { 5, 6, 7 });
    fillTestImage(lFloatImg);
    testBackAndForthConversionViaCLImage(lFloatImg);
    testBackAndForthConversionViaOffHeapPlanarStack(lFloatImg);
    testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(lFloatImg);

    RandomAccessibleInterval<UnsignedByteType>
        lUnsignedByteImg = ArrayImgs.unsignedBytes(new long[] { 5, 6, 7 });
    fillTestImage(lUnsignedByteImg);
    testBackAndForthConversionViaCLImage(lUnsignedByteImg);
    testBackAndForthConversionViaOffHeapPlanarStack(lUnsignedByteImg);
    testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(lUnsignedByteImg);

    RandomAccessibleInterval<ByteType>
        lByteImg = ArrayImgs.bytes(new long[] { 5, 6, 7 });
    fillTestImage(lByteImg);
    testBackAndForthConversionViaCLImage(lByteImg);
    testBackAndForthConversionViaOffHeapPlanarStack(lByteImg);
    testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(lByteImg);

    RandomAccessibleInterval<UnsignedShortType>
        lUnsignedShortImg = ArrayImgs.unsignedShorts(new long[] { 5, 6, 7 });
    fillTestImage(lUnsignedShortImg);
    testBackAndForthConversionViaCLImage(lUnsignedShortImg);
    testBackAndForthConversionViaOffHeapPlanarStack(lUnsignedShortImg);
    testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(lUnsignedShortImg);


    RandomAccessibleInterval<ShortType>
        lShortImg = ArrayImgs.shorts(new long[] { 5, 6, 7 });
    fillTestImage(lShortImg);
    testBackAndForthConversionViaCLImage(lShortImg);
    testBackAndForthConversionViaOffHeapPlanarStack(lShortImg);
    testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(lShortImg);

  }






  private <T extends RealType<T>> void fillTestImage(RandomAccessibleInterval<T> lFloatImg) {
    RandomAccess<T> lRandomAccess = lFloatImg.randomAccess();
    lRandomAccess.setPosition(new long[]{1,2,3});
    lRandomAccess.get().setReal(4);
  }

  private <T extends RealType<T>> void testBackAndForthConversionViaCLImage(RandomAccessibleInterval<T> lRAI) {
    ClearCLIJ lCLIJ = ClearCLIJ.getInstance();

    ClearCLImage lClearCLImage = lCLIJ.converter(lRAI).getClearCLImage();

    RandomAccessibleInterval<T>
        lRAIconvertedTwice =
        (RandomAccessibleInterval<T>) lCLIJ.converter(lClearCLImage).getRandomAccessibleInterval();

    assertTrue(compareIterableIntervals(Views.iterable(lRAI), Views.iterable(lRAIconvertedTwice)));
  }

  private <T extends RealType<T>> void testBackAndForthConversionViaOffHeapPlanarStack(RandomAccessibleInterval<T> lRAI) {
    ClearCLIJ lCLIJ = ClearCLIJ.getInstance();

    OffHeapPlanarStack lOffHeapPlanarStack = lCLIJ.converter(lRAI).getOffHeapPlanarStack();

    RandomAccessibleInterval<T>
        lRAIconvertedTwice =
        (RandomAccessibleInterval<T>) lCLIJ.converter(lOffHeapPlanarStack).getRandomAccessibleInterval();

    assertTrue(compareIterableIntervals(Views.iterable(lRAI), Views.iterable(lRAIconvertedTwice)));
  }

  private <T extends RealType<T>> void testBackAndForthConversionViaOffHeapPlanarStackAndCLImage(RandomAccessibleInterval<T> lRAI) {
    ClearCLIJ lCLIJ = ClearCLIJ.getInstance();

    OffHeapPlanarStack lOffHeapPlanarStack = lCLIJ.converter(lRAI).getOffHeapPlanarStack();

    ClearCLImage lCLImage = lCLIJ.converter(lOffHeapPlanarStack).getClearCLImage();

    RandomAccessibleInterval<T>
        lRAIconvertedTwice =
        (RandomAccessibleInterval<T>) lCLIJ.converter(lCLImage).getRandomAccessibleInterval();

    ClearCLImage lCLImage2 = lCLIJ.converter(lRAIconvertedTwice).getClearCLImage();

    OffHeapPlanarStack lOffHeapPlanarStack2 = lCLIJ.converter(lCLImage2).getOffHeapPlanarStack();

    RandomAccessibleInterval<T> lRAIconverted = lCLIJ.converter(lOffHeapPlanarStack2).getRandomAccessibleInterval();

    assertTrue(compareIterableIntervals(Views.iterable(lRAI), Views.iterable(lRAIconverted)));
  }


  private <T extends RealType<T>> boolean compareIterableIntervals(IterableInterval<T> lIterableInterval1,
                                                                  IterableInterval<T> lIterableInterval2)
  {
    double lSum = 0;
    Cursor<T> lCursor1 = lIterableInterval1.cursor();
    Cursor<T> lCursor2 = lIterableInterval2.cursor();

    while (lCursor1.hasNext() && lCursor2.hasNext()) {
      if (lCursor1.next().getRealFloat() != lCursor2.next().getRealFloat()) {
        System.out.println("lCursor1 " + lCursor1.get().getRealFloat());
        System.out.println("lCursor2 " + lCursor2.get().getRealFloat());
        System.out.println("Value different ");
        return false;
      }
      lSum += lCursor1.get().getRealDouble();
    }
    System.out.println("sum " + lSum);

    // if one image is longer than the other
    if (lCursor1.hasNext() || lCursor2.hasNext()) {
      return false;
    }

    return true;
  }
}
