package clearcl.imagej.test;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

/**
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class TestUtilities
{

  public static <T extends RealType<T>> boolean compareIterableIntervals(IterableInterval<T> lIterableInterval1,
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

    // check if one image is longer than the other
    if (lCursor1.hasNext() || lCursor2.hasNext()) {
      return false;
    }

    return true;
  }
}
