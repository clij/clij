package clearcl.imagej.test;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import clearcl.imagej.ClearCLIJ;
import clearcl.imagej.kernels.Kernels;
import ij.IJ;
import ij.ImagePlus;
import ij.Prefs;
import ij.gui.NewImage;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import ij.plugin.GaussianBlur3D;
import ij.plugin.ImageCalculator;
import ij.plugin.filter.MaximumFinder;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
 * Test class for all OpenCL kernels accessible via the Kernels class
 *
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * March 2018
 */
public class KernelsTest
{

  ImagePlus testImp1;
  ImagePlus testImp2;
  ImagePlus testImp2D1;
  ImagePlus testImp2D2;
  ImagePlus mask3d;
  ImagePlus mask2d;
  static ClearCLIJ clij;

  @Before public void initTest()
  {
    testImp1 =
        NewImage.createImage("",
                             100,
                             100,
                             12,
                             16,
                             NewImage.FILL_BLACK);
    testImp2 =
        NewImage.createImage("",
                             100,
                             100,
                             12,
                             16,
                             NewImage.FILL_BLACK);
    mask3d =
        NewImage.createImage("",
                             100,
                             100,
                             12,
                             16,
                             NewImage.FILL_BLACK);

    for (int z = 0; z < 5; z++)
    {
      testImp1.setZ(z + 1);
      ImageProcessor ip1 = testImp1.getProcessor();
      ip1.set(5, 5, 1);
      ip1.set(6, 6, 1);
      ip1.set(7, 7, 1);

      testImp2.setZ(z + 1);
      ImageProcessor ip2 = testImp2.getProcessor();
      ip2.set(7, 5, 2);
      ip2.set(6, 6, 2);
      ip2.set(5, 7, 2);

      if (z < 3)
      {
        mask3d.setZ(z + 3);
        ImageProcessor ip3 = mask3d.getProcessor();
        ip3.set(2, 2, 1);
        ip3.set(2, 3, 1);
        ip3.set(2, 4, 1);
        ip3.set(3, 2, 1);
        ip3.set(3, 3, 1);
        ip3.set(3, 4, 1);
        ip3.set(4, 2, 1);
        ip3.set(4, 3, 1);
        ip3.set(4, 4, 1);
      }
    }

    testImp2D1 = new Duplicator().run(testImp1, 1, 1);
    testImp2D2 = new Duplicator().run(testImp1, 1, 1);
    mask2d = new Duplicator().run(mask3d, 3, 3);

    if (clij == null)
    {
      clij = //ClearCLIJ.getInstance();
          //new ClearCLIJ("Geforce");
      new ClearCLIJ("HD");
    }
  }

  @Test public void addPixelwise3d()
  {
    // do operation with ImageJ
    ImageCalculator ic = new ImageCalculator();
    ImagePlus sumImp = ic.run("Add create stack", testImp1, testImp2);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage src1 = clij.converter(testImp2).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp1).getClearCLImage();

    Kernels.addPixelwise(clij, src, src1, dst);
    ImagePlus sumImpFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

    src.close();
    src1.close();
    dst.close();
  }

  @Test public void addPixelwise2d()
  {
    // do operation with ImageJ
    ImageCalculator ic = new ImageCalculator();
    ImagePlus sumImp = ic.run("Add create", testImp2D1, testImp2D2);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage src1 = clij.converter(testImp2D2).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp2D1).getClearCLImage();

    Kernels.addPixelwise(clij, src, src1, dst);
    ImagePlus sumImpFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

    src.close();
    src1.close();
    dst.close();
  }

  @Test public void addScalar3d()
  {
    // do operation with ImageJ
    ImagePlus added = new Duplicator().run(testImp1);
    IJ.run(added, "Add...", "value=1 stack");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp1).getClearCLImage();

    Kernels.addScalar(clij, src, dst, 1);
    ImagePlus addedFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(added, addedFromCL));

    src.close();
    dst.close();
  }

  @Test public void addScalar2d()
  {
    // do operation with ImageJ
    ImagePlus added = new Duplicator().run(testImp2D1);
    IJ.run(added, "Add...", "value=1");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp2D1).getClearCLImage();

    Kernels.addScalar(clij, src, dst, 1);
    ImagePlus addedFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(added, addedFromCL));

    src.close();
    dst.close();
  }

  @Test public void addWeightedPixelwise3d()
  {
    float factor1 = 3f;
    float factor2 = 2;

    // do operation with ImageJ
    ImagePlus testImp1copy = new Duplicator().run(testImp1);
    ImagePlus testImp2copy = new Duplicator().run(testImp2);
    IJ.run(testImp1copy,
           "Multiply...",
           "value=" + factor1 + " stack");
    IJ.run(testImp2copy,
           "Multiply...",
           "value=" + factor2 + " stack");

    ImageCalculator ic = new ImageCalculator();
    ImagePlus
        sumImp =
        ic.run("Add create stack", testImp1copy, testImp2copy);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage src1 = clij.converter(testImp2).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp1).getClearCLImage();

    Kernels.addWeightedPixelwise(clij,
                                 src,
                                 src1,
                                 dst,
                                 factor1,
                                 factor2);
    ImagePlus sumImpFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

    src.close();
    src1.close();
    dst.close();
  }

  @Test public void addWeightedPixelwise2d()
  {
    float factor1 = 3f;
    float factor2 = 2;

    // do operation with ImageJ
    ImagePlus testImp1copy = new Duplicator().run(testImp2D1);
    ImagePlus testImp2copy = new Duplicator().run(testImp2D2);
    IJ.run(testImp1copy, "Multiply...", "value=" + factor1 + " ");
    IJ.run(testImp2copy, "Multiply...", "value=" + factor2 + " ");

    ImageCalculator ic = new ImageCalculator();
    ImagePlus
        sumImp =
        ic.run("Add create ", testImp1copy, testImp2copy);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage src1 = clij.converter(testImp2D2).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp2D1).getClearCLImage();

    Kernels.addWeightedPixelwise(clij,
                                 src,
                                 src1,
                                 dst,
                                 factor1,
                                 factor2);
    ImagePlus sumImpFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

    src.close();
    src1.close();
    dst.close();
  }

  @Test public void argMaxProjection()
  {
    // do operation with ImageJ
    ImagePlus
        maxProjection =
        NewImage.createShortImage("",
                                  testImp1.getWidth(),
                                  testImp2.getHeight(),
                                  1,
                                  NewImage.FILL_BLACK);
    ImageProcessor ipMax = maxProjection.getProcessor();
    ImagePlus
        argMaxProjection =
        NewImage.createShortImage("",
                                  testImp1.getWidth(),
                                  testImp2.getHeight(),
                                  1,
                                  NewImage.FILL_BLACK);
    ImageProcessor ipArgMax = maxProjection.getProcessor();

    ImagePlus testImp1copy = new Duplicator().run(testImp1);
    for (int z = 0; z < testImp1copy.getNSlices(); z++)
    {
      testImp1copy.setZ(z + 1);
      ImageProcessor ip = testImp1copy.getProcessor();
      for (int x = 0; x < testImp1copy.getWidth(); x++)
      {
        for (int y = 0; y < testImp1copy.getHeight(); y++)
        {
          float value = ip.getf(x, y);
          if (value > ipMax.getf(x, y))
          {
            ipMax.setf(x, y, value);
            ipArgMax.setf(x, y, z);
          }
        }
      }
    }

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage
        dst =
        clij.createCLImage(new long[] { src.getWidth(),
                                        src.getHeight() },
                           src.getChannelDataType());
    ClearCLImage
        dst_arg =
        clij.createCLImage(new long[] { src.getWidth(),
                                        src.getHeight() },
                           src.getChannelDataType());

    Kernels.argMaxProjection(clij, src, dst, dst_arg);

    ImagePlus maxProjectionCL = clij.converter(dst).getImagePlus();
    ImagePlus
        argMaxProjectionCL =
        clij.converter(dst_arg).getImagePlus();

    assertTrue(TestUtilities.compareImages(maxProjection,
                                           maxProjectionCL));
    assertTrue(TestUtilities.compareImages(argMaxProjection,
                                           argMaxProjectionCL));

    src.close();
    dst.close();
    dst_arg.close();
  }

  @Test public void blur3d()
  {
    // do operation with ImageJ
    ImagePlus gauss = new Duplicator().run(testImp1);
    GaussianBlur3D.blur(gauss, 2, 2, 2);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp1).getClearCLImage();

    Kernels.blur(clij, src, dst, 6, 6, 6, 2, 2, 2);
    ImagePlus gaussFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(gauss, gaussFromCL));

    src.close();
    dst.close();
  }

  @Test public void blur2d()
  {
    // do operation with ImageJ
    ImagePlus gauss = new Duplicator().run(testImp2D1);
    ImagePlus gaussCopy = new Duplicator().run(testImp2D1);
    IJ.run(gauss, "Gaussian Blur...", "sigma=2");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(gaussCopy).getClearCLImage();
    ClearCLImage dst = clij.converter(gaussCopy).getClearCLImage();

    Kernels.blur(clij, src, dst, 6, 6, 2, 2);
    ImagePlus gaussFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(gauss, gaussFromCL));

    src.close();
    dst.close();
  }

  @Test public void blurSlicewise()
  {
    // do operation with ImageJ
    ImagePlus gauss = new Duplicator().run(testImp1);
    IJ.run(gauss, "Gaussian Blur...", "sigma=2 stack");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp1).getClearCLImage();

    Kernels.blurSlicewise(clij, src, dst, 6, 6, 2, 2);
    ImagePlus gaussFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(gauss, gaussFromCL));

    src.close();
    dst.close();
  }

  @Test public void copyImageToBuffer3d()
  {
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLBuffer
        dst =
        clij.createCLBuffer(src.getDimensions(), src.getNativeType());

    Kernels.copy(clij, src, dst);
    ImagePlus copyFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(testImp1, copyFromCL));

    src.close();
    dst.close();
  }

  @Test public void copyImageToBuffer2d()
  {
    ClearCLImage src = clij.converter(testImp2D1).getClearCLImage();
    ClearCLBuffer
        dst =
        clij.createCLBuffer(src.getDimensions(), src.getNativeType());

    Kernels.copy(clij, src, dst);
    ImagePlus copyFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(testImp2D1, copyFromCL));

    src.close();
    dst.close();
  }

  @Test public void copyBufferToImage3d()
  {
    ClearCLBuffer src = clij.converter(testImp1).getClearCLBuffer();
    ClearCLImage dst = clij.converter(testImp1).getClearCLImage();

    Kernels.set(clij, dst, 0);

    Kernels.copy(clij, src, dst);
    ImagePlus copyFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(testImp1, copyFromCL));

    src.close();
    dst.close();
  }

  @Test public void copyBufferToImage2d()
  {
    ClearCLBuffer src = clij.converter(testImp2D1).getClearCLBuffer();
    ClearCLImage dst = clij.converter(testImp2D1).getClearCLImage();

    Kernels.set(clij, dst, 0);

    Kernels.copy(clij, src, dst);
    ImagePlus copyFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(testImp2D1, copyFromCL));

    src.close();
    dst.close();
  }

  @Test public void copy3d()
  {
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage
        dst =
        clij.createCLImage(src.getDimensions(),
                           src.getChannelDataType());
    ;

    Kernels.copy(clij, src, dst);
    ImagePlus copyFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(testImp1, copyFromCL));

    src.close();
    dst.close();
  }

  @Test public void copy2d()
  {
    ClearCLImage src = clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage
        dst =
        clij.createCLImage(src.getDimensions(),
                           src.getChannelDataType());

    Kernels.copy(clij, src, dst);
    ImagePlus copyFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(testImp2D1, copyFromCL));

    src.close();
    dst.close();
  }

  @Test public void copyBuffer3d()
  {
    ImagePlus imp = new Duplicator().run(testImp1);
    Img<FloatType> img = ImageJFunctions.convertFloat(testImp1);

    ClearCLBuffer src = clij.converter(imp).getClearCLBuffer();
    ClearCLBuffer
        dst =
        clij.createCLBuffer(src.getDimensions(), src.getNativeType());

    Kernels.copy(clij, src, dst);
    ImagePlus copyFromCL = clij.converter(dst).getImagePlus();
    assertTrue(TestUtilities.compareImages(testImp1, copyFromCL));

    RandomAccessibleInterval
        rai =
        clij.converter(dst).getRandomAccessibleInterval();
    assertTrue(TestUtilities.compareIterableIntervals(img,
                                                      Views.iterable(
                                                          rai)));

    src.close();
    dst.close();
  }

  @Test public void copyBuffer2d()
  {
    ImagePlus imp = new Duplicator().run(testImp2D1);
    Img<FloatType> img = ImageJFunctions.convertFloat(testImp2D1);

    ClearCLBuffer src = clij.converter(imp).getClearCLBuffer();
    ClearCLBuffer
        dst =
        clij.createCLBuffer(src.getDimensions(), src.getNativeType());

    Kernels.copy(clij, src, dst);
    ImagePlus copyFromCL = clij.converter(dst).getImagePlus();
    assertTrue(TestUtilities.compareImages(testImp2D1, copyFromCL));

    RandomAccessibleInterval
        rai =
        clij.converter(dst).getRandomAccessibleInterval();
    assertTrue(TestUtilities.compareIterableIntervals(img,
                                                      Views.iterable(
                                                          rai)));

    src.close();
    dst.close();
  }

  @Test public void copySlice()
  {
    // do operation with ImageJ
    ImagePlus copy = new Duplicator().run(testImp1, 3, 3);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage
        dst =
        clij.createCLImage(new long[] { src.getWidth(),
                                        src.getHeight() },
                           src.getChannelDataType());

    Kernels.copySlice(clij, src, dst, 2);
    ImagePlus copyFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(copy, copyFromCL));

    src.close();
    dst.close();
  }

  @Test public void copySliceBuffer()
  {
    // do operation with ImageJ
    ImagePlus copy = new Duplicator().run(testImp1, 3, 3);

    // do operation with ClearCL
    ClearCLBuffer src = clij.converter(testImp1).getClearCLBuffer();
    ClearCLBuffer
        dst =
        clij.createCLBuffer(new long[] { src.getWidth(),
                                         src.getHeight() },
                            src.getNativeType());

    Kernels.copySlice(clij, src, dst, 2);
    ImagePlus copyFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(copy, copyFromCL));

    src.close();
    dst.close();
  }

  @Test public void crop3d()
  {
    // do operation with ImageJ
    Roi roi = new Roi(2, 2, 10, 10);
    testImp1.setRoi(roi);
    ImagePlus crop = new Duplicator().run(testImp1, 3, 12);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage
        dst =
        clij.createCLImage(new long[] { 10, 10, 10 },
                           src.getChannelDataType());

    Kernels.crop(clij, src, dst, 2, 2, 2);
    ImagePlus cropFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(crop, cropFromCL));

    src.close();
    dst.close();
  }

  @Test public void crop2d()
  {
    // do operation with ImageJ
    Roi roi = new Roi(2, 2, 10, 10);
    testImp2D1.setRoi(roi);
    ImagePlus crop = new Duplicator().run(testImp2D1);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage
        dst =
        clij.createCLImage(new long[] { 10, 10 },
                           src.getChannelDataType());

    Kernels.crop(clij, src, dst, 2, 2);
    ImagePlus cropFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(crop, cropFromCL));

    src.close();
    dst.close();
  }

  @Test public void cropBuffer3d()
  {
    // do operation with ImageJ
    Roi roi = new Roi(2, 2, 10, 10);
    testImp1.setRoi(roi);
    ImagePlus crop = new Duplicator().run(testImp1, 3, 12);

    // do operation with ClearCL
    ClearCLBuffer src = clij.converter(testImp1).getClearCLBuffer();
    ClearCLBuffer
        dst =
        clij.createCLBuffer(new long[] { 10, 10, 10 },
                            src.getNativeType());

    Kernels.crop(clij, src, dst, 2, 2, 2);
    ImagePlus cropFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(crop, cropFromCL));

    src.close();
    dst.close();
  }

  @Test public void cropBuffer2d()
  {
    // do operation with ImageJ
    Roi roi = new Roi(2, 2, 10, 10);
    testImp2D1.setRoi(roi);
    ImagePlus crop = new Duplicator().run(testImp2D1);

    // do operation with ClearCL
    ClearCLBuffer src = clij.converter(testImp2D1).getClearCLBuffer();
    ClearCLBuffer
        dst =
        clij.createCLBuffer(new long[] { 10, 10 },
                            src.getNativeType());

    Kernels.crop(clij, src, dst, 2, 2);
    ImagePlus cropFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(crop, cropFromCL));

    src.close();
    dst.close();
  }

  @Test public void detectMaxima3d()
  {
    // do operation with ImageJ
    ImagePlus
        spotsImage =
        NewImage.createImage("",
                             100,
                             100,
                             3,
                             16,
                             NewImage.FILL_BLACK);

    spotsImage.setZ(2);
    ImageProcessor ip1 = spotsImage.getProcessor();
    ip1.set(50, 50, 10);
    ip1.set(60, 60, 10);
    ip1.set(70, 70, 10);

    spotsImage.show();
    //IJ.run(spotsImage, "Find Maxima...", "noise=2 output=[Single Points]");

    ByteProcessor
        byteProcessor =
        new MaximumFinder().findMaxima(spotsImage.getProcessor(),
                                       2,
                                       MaximumFinder.SINGLE_POINTS,
                                       true);
    ImagePlus maximaImp = new ImagePlus("A", byteProcessor);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(spotsImage).getClearCLImage();
    ClearCLImage dst = clij.converter(spotsImage).getClearCLImage();

    Kernels.detectOptima(clij, src, dst, 1, true);
    ImagePlus maximaFromCL = clij.converter(dst).getImagePlus();
    maximaFromCL = new Duplicator().run(maximaFromCL, 2, 2);

    IJ.run(maximaImp, "Divide...", "value=255");

    assertTrue(TestUtilities.compareImages(maximaImp, maximaFromCL));

    src.close();
    dst.close();
  }

  @Test public void detectMaxima2d()
  {
    // do operation with ImageJ
    ImagePlus
        spotsImage =
        NewImage.createImage("",
                             100,
                             100,
                             1,
                             16,
                             NewImage.FILL_BLACK);

    spotsImage.setZ(1);
    ImageProcessor ip1 = spotsImage.getProcessor();
    ip1.set(50, 50, 10);
    ip1.set(60, 60, 10);
    ip1.set(70, 70, 10);

    spotsImage.show();
    //IJ.run(spotsImage, "Find Maxima...", "noise=2 output=[Single Points]");

    ByteProcessor
        byteProcessor =
        new MaximumFinder().findMaxima(spotsImage.getProcessor(),
                                       2,
                                       MaximumFinder.SINGLE_POINTS,
                                       true);
    ImagePlus maximaImp = new ImagePlus("A", byteProcessor);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(spotsImage).getClearCLImage();
    ClearCLImage dst = clij.converter(spotsImage).getClearCLImage();

    Kernels.detectOptima(clij, src, dst, 1, true);
    ImagePlus maximaFromCL = clij.converter(dst).getImagePlus();
    //    maximaFromCL = new Duplicator().run(maximaFromCL, 2, 2);

    IJ.run(maximaImp, "Divide...", "value=255");

    assertTrue(TestUtilities.compareImages(maximaImp, maximaFromCL));

    src.close();
    dst.close();
  }

  @Test public void differenceOfGaussian3d()
  {
    // do operation with ImageJ
    System.out.println("Todo: implement test for DoG");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp1).getClearCLImage();

    Kernels.differenceOfGaussian(clij, src, dst, 6, 1.1f, 3.3f);

    src.close();
    dst.close();
  }

  @Test public void differenceOfGaussian2d()
  {
    // do operation with ImageJ
    System.out.println("Todo: implement test for DoG");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp2D1).getClearCLImage();

    Kernels.differenceOfGaussian(clij, src, dst, 6, 1.1f, 3.3f);

    src.close();
    dst.close();
  }

  @Test public void dilate3d()
  {
    ClearCLImage maskCL = clij.converter(mask3d).getClearCLImage();
    ClearCLImage
            maskCLafter =
            clij.converter(mask3d).getClearCLImage();

    Kernels.dilate(clij, maskCL, maskCLafter);

    double sum = Kernels.sumPixels(clij, maskCLafter);

    assertTrue(sum == 81);

    maskCL.close();
    maskCLafter.close();
  }

  @Test public void dilate2d()
  {
    ClearCLImage maskCL = clij.converter(mask2d).getClearCLImage();
    ClearCLImage
            maskCLafter =
            clij.converter(mask2d).getClearCLImage();

    Kernels.dilate(clij, maskCL, maskCLafter);

    double sum = Kernels.sumPixels(clij, maskCLafter);

    assertTrue(sum == 21);

    maskCL.close();
    maskCLafter.close();
  }

  @Test public void downsample3d()
  {
    // do operation with ImageJ
    System.out.println("Todo: implement test for downsample3d");
/*
        testImp1.show();
        IJ.run(testImp1, "Scale...", "x=0.5 y=0.5 z=0.5 width=512 height=1024 depth=5 interpolation=None process create");
        ImagePlus downsampled = IJ.getImage();
*/

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp1).getClearCLImage();

    Kernels.downsample(clij, src, dst, 0.5f, 0.5f, 0.5f);
/*
        ImagePlus downsampledCL = clij.converter(dst).getImagePlus();

      assertTrue(compareImages(downsampled, downsampledCL));
      */
  }

  @Test public void downsample2d()
  {
    // do operation with ImageJ
    System.out.println("Todo: implement test for downsample2d");
     /*
      testImp2D1.show();
      IJ.run(testImp2D1, "Scale...", "x=0.5 y=0.5 width=50 height=50 interpolation=None");

      Thread.sleep(1000);
      //new Scaler().run();
      int[] idlist = WindowManager.getIDList();
      ImagePlus downsampled = WindowManager.getImage(idlist[idlist.length - 1]);//IJ.getImage();
*/

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage
        dst =
        clij.createCLImage(new long[] { src.getWidth() / 2,
                                        src.getHeight() / 2 },
                           src.getChannelDataType());

    Kernels.downsample(clij, src, dst, 0.5f, 0.5f);
/*
      ImagePlus downsampledCL = clij.converter(dst).getImagePlus();

      assertTrue(TestUtilities.compareImages(downsampled, downsampledCL));
*/
  }

  @Test public void erode3d()
  {
    ClearCLImage maskCL = clij.converter(mask3d).getClearCLImage();
    ClearCLImage
        maskCLafter =
        clij.converter(mask3d).getClearCLImage();

    Kernels.erode(clij, maskCL, maskCLafter);

    double sum = Kernels.sumPixels(clij, maskCLafter);

    assertTrue(sum == 1);

    maskCL.close();
    maskCLafter.close();
  }

  @Test public void erode2d()
  {
    ClearCLImage maskCL = clij.converter(mask2d).getClearCLImage();
    ClearCLImage
        maskCLafter =
        clij.converter(mask2d).getClearCLImage();

    Kernels.erode(clij, maskCL, maskCLafter);

    double sum = Kernels.sumPixels(clij, maskCLafter);

    assertTrue(sum == 1);

    maskCL.close();
    maskCLafter.close();
  }

  @Test public void flip3d() throws InterruptedException
  {
    ClearCLImage testCL = clij.converter(testImp1).getClearCLImage();
    ClearCLImage flip = clij.converter(testImp1).getClearCLImage();
    ClearCLImage flop = clij.converter(testImp1).getClearCLImage();

    Kernels.flip(clij, testCL, flip, true, false, false);

    ImagePlus testFlipped = clij.converter(flip).getImagePlus();

    Kernels.flip(clij, flip, flop, true, false, false);
    ImagePlus testFlippedTwice = clij.converter(flop).getImagePlus();

    assertTrue(TestUtilities.compareImages(testImp1,
                                           testFlippedTwice));
    assertFalse(TestUtilities.compareImages(testImp1, testFlipped));

    testCL.close();
    flip.close();
    flop.close();
  }

  @Test public void flip2d()
  {
    ClearCLImage
        testCL =
        clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage flip = clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage flop = clij.converter(testImp2D1).getClearCLImage();

    Kernels.flip(clij, testCL, flip, true, false);

    ImagePlus testFlipped = clij.converter(flip).getImagePlus();

    Kernels.flip(clij, flip, flop, true, false);
    ImagePlus testFlippedTwice = clij.converter(flop).getImagePlus();

    assertTrue(TestUtilities.compareImages(testImp2D1,
                                           testFlippedTwice));
    assertFalse(TestUtilities.compareImages(testImp2D1, testFlipped));

    testCL.close();
    flip.close();
    flop.close();
  }

  @Test public void flipBuffer3d()
  {
    ClearCLBuffer
        testCL =
        clij.converter(testImp1).getClearCLBuffer();
    ClearCLBuffer flip = clij.converter(testImp1).getClearCLBuffer();
    ClearCLBuffer flop = clij.converter(testImp1).getClearCLBuffer();

    Kernels.flip(clij, testCL, flip, true, false, false);

    ImagePlus testFlipped = clij.converter(flip).getImagePlus();

    Kernels.flip(clij, flip, flop, true, false, false);
    ImagePlus testFlippedTwice = clij.converter(flop).getImagePlus();

    assertTrue(TestUtilities.compareImages(testImp1,
                                           testFlippedTwice));
    assertFalse(TestUtilities.compareImages(testImp1, testFlipped));

    testCL.close();
    flip.close();
    flop.close();
  }

  @Test public void flipBuffer2d()
  {
    ClearCLBuffer
        testCL =
        clij.converter(testImp2D1).getClearCLBuffer();
    ClearCLBuffer
        flip =
        clij.converter(testImp2D1).getClearCLBuffer();
    ClearCLBuffer
        flop =
        clij.converter(testImp2D1).getClearCLBuffer();

    Kernels.flip(clij, testCL, flip, true, false);

    ImagePlus testFlipped = clij.converter(flip).getImagePlus();

    Kernels.flip(clij, flip, flop, true, false);
    ImagePlus testFlippedTwice = clij.converter(flop).getImagePlus();

    assertTrue(TestUtilities.compareImages(testImp2D1,
                                           testFlippedTwice));
    assertFalse(TestUtilities.compareImages(testImp2D1, testFlipped));

    testCL.close();
    flip.close();
    flop.close();
  }

  @Test public void invertBinary3d()
  {
    ClearCLImage maskCL = clij.converter(mask3d).getClearCLImage();
    ClearCLImage
        maskCLafter =
        clij.converter(mask3d).getClearCLImage();

    Kernels.invertBinary(clij, maskCL, maskCLafter);

    double sumCL = Kernels.sumPixels(clij, maskCL);
    double sumCLafter = Kernels.sumPixels(clij, maskCLafter);

    assertTrue(sumCLafter
               == maskCL.getWidth()
                  * maskCL.getHeight()
                  * maskCL.getDepth() - sumCL);

    maskCL.close();
    maskCLafter.close();
  }

  @Test public void invertBinary2d()
  {
    ClearCLImage maskCL = clij.converter(mask2d).getClearCLImage();
    ClearCLImage
        maskCLafter =
        clij.converter(mask2d).getClearCLImage();

    Kernels.invertBinary(clij, maskCL, maskCLafter);

    double sumCL = Kernels.sumPixels(clij, maskCL);
    double sumCLafter = Kernels.sumPixels(clij, maskCLafter);

    assertTrue(sumCLafter
               == maskCL.getWidth()
                  * maskCL.getHeight()
                  * maskCL.getDepth() - sumCL);

    maskCL.close();
    maskCLafter.close();
  }

  @Test public void mask3d()
  {
    // do operation with ImageJ
    System.out.println("Todo: implement test for mask3d");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage mask = clij.converter(mask3d).getClearCLImage();
    ClearCLImage dst = clij.converter(mask3d).getClearCLImage();

    Kernels.mask(clij, src, mask, dst);

    mask.close();
    dst.close();
  }

  @Test public void mask2d()
  {
    // do operation with ImageJ
    System.out.println("Todo: implement test for mask2d");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage mask = clij.converter(mask2d).getClearCLImage();
    ClearCLImage dst = clij.converter(mask2d).getClearCLImage();

    Kernels.mask(clij, src, mask, dst);

    mask.close();
    dst.close();
  }

  @Test public void maskStackWithPlane()
  {
    // do operation with ImageJ
    System.out.println("Todo: implement test for maskStackWithPlane");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage mask = clij.converter(mask2d).getClearCLImage();
    ClearCLImage dst = clij.converter(mask3d).getClearCLImage();

    Kernels.maskStackWithPlane(clij, src, mask, dst);

    mask.close();
    dst.close();

  }

  @Test public void maxProjection() throws InterruptedException
  {
    // do operation with ImageJ
    ImagePlus
        maxProjection =
        NewImage.createShortImage("",
                                  testImp1.getWidth(),
                                  testImp2.getHeight(),
                                  1,
                                  NewImage.FILL_BLACK);
    ImageProcessor ipMax = maxProjection.getProcessor();

    ImagePlus testImp1copy = new Duplicator().run(testImp1);
    for (int z = 0; z < testImp1copy.getNSlices(); z++)
    {
      testImp1copy.setZ(z + 1);
      ImageProcessor ip = testImp1copy.getProcessor();
      for (int x = 0; x < testImp1copy.getWidth(); x++)
      {
        for (int y = 0; y < testImp1copy.getHeight(); y++)
        {
          float value = ip.getf(x, y);
          if (value > ipMax.getf(x, y))
          {
            ipMax.setf(x, y, value);
          }
        }
      }
    }

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage
        dst =
        clij.createCLImage(new long[] { src.getWidth(),
                                        src.getHeight() },
                           src.getChannelDataType());

    Kernels.maxProjection(clij, src, dst);

    ImagePlus maxProjectionCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(maxProjection,
                                           maxProjectionCL));

    src.close();
    dst.close();
  }

  @Test public void multiplyPixelwise3d()
  {
    // do operation with ImageJ
    ImagePlus
        multiplied =
        new ImageCalculator().run("Multiply create stack",
                                  testImp1,
                                  testImp2);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage src1 = clij.converter(testImp2).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp1).getClearCLImage();

    Kernels.multiplyPixelwise(clij, src, src1, dst);

    ImagePlus multipliedCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

    src.close();
    src1.close();
    dst.close();
  }

  @Test public void multiplyPixelwise2d()
  {
    // do operation with ImageJ
    ImagePlus
        multiplied =
        new ImageCalculator().run("Multiply create",
                                  testImp2D1,
                                  testImp2D2);

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage src1 = clij.converter(testImp2D2).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp2D1).getClearCLImage();

    Kernels.multiplyPixelwise(clij, src, src1, dst);

    ImagePlus multipliedCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(multiplied, multipliedCL));

    src.close();
    src1.close();
    dst.close();
  }

  @Test public void multiplyScalar3d()
  {
    // do operation with ImageJ
    ImagePlus added = new Duplicator().run(testImp1);
    IJ.run(added, "Multiply...", "value=2 stack");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp1).getClearCLImage();

    Kernels.multiplyScalar(clij, src, dst, 2);
    ImagePlus addedFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(added, addedFromCL));

    src.close();
    dst.close();
  }

  @Test public void multiplyScalar2d()
  {
    // do operation with ImageJ
    ImagePlus added = new Duplicator().run(testImp2D1);
    IJ.run(added, "Multiply...", "value=2");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp2D1).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp2D1).getClearCLImage();

    Kernels.multiplyScalar(clij, src, dst, 2);
    ImagePlus addedFromCL = clij.converter(dst).getImagePlus();

    assertTrue(TestUtilities.compareImages(added, addedFromCL));

    src.close();
    dst.close();
  }

  @Test public void multiplyStackWithPlane()
  {
    // do operation with ImageJ
    System.out.println(
        "Todo: implement test for multiplyStackWithPlane");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp1).getClearCLImage();
    ClearCLImage mask = clij.converter(mask2d).getClearCLImage();
    ClearCLImage dst = clij.converter(mask3d).getClearCLImage();

    Kernels.multiplyStackWithPlane(clij, src, mask, dst);

    mask.close();
    dst.close();

  }

  @Test public void set3d()
  {
    ClearCLImage imageCL = clij.converter(mask3d).getClearCLImage();

    Kernels.set(clij, imageCL, 2);

    double sum = Kernels.sumPixels(clij, imageCL);

    assertTrue(sum
               == imageCL.getWidth()
                  * imageCL.getHeight()
                  * imageCL.getDepth()
                  * 2);

    imageCL.close();
  }

  @Test public void set2d()
  {
    ClearCLImage imageCL = clij.converter(mask2d).getClearCLImage();

    Kernels.set(clij, imageCL, 2);

    double sum = Kernels.sumPixels(clij, imageCL);

    assertTrue(sum == imageCL.getWidth() * imageCL.getHeight() * 2);

    imageCL.close();
  }

  @Test public void sumPixels3d()
  {
    ClearCLImage maskCL = clij.converter(mask3d).getClearCLImage();

    double sum = Kernels.sumPixels(clij, maskCL);

    assertTrue(sum == 27);

    maskCL.close();
  }

  @Test public void sumPixels2d()
  {
    ClearCLImage maskCL = clij.converter(mask2d).getClearCLImage();

    double sum = Kernels.sumPixels(clij, maskCL);

    assertTrue(sum == 9);

    maskCL.close();
  }

  @Test public void threshold3d()
  {
    // do operation with ImageJ
    ImagePlus thresholded = new Duplicator().run(testImp2);
    Prefs.blackBackground = false;
    IJ.setRawThreshold(thresholded, 2, 65535, null);
    IJ.run(thresholded,
           "Convert to Mask",
           "method=Default background=Dark");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp2).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp2).getClearCLImage();

    Kernels.threshold(clij, src, dst, 2);
    Kernels.multiplyScalar(clij, dst, src, 255);

    ImagePlus thresholdedCL = clij.converter(src).getImagePlus();

    assertTrue(TestUtilities.compareImages(thresholded,
                                           thresholdedCL));

    src.close();
    dst.close();
  }

  @Test public void threshold2d()
  {
    // do operation with ImageJ
    ImagePlus thresholded = new Duplicator().run(testImp2D2);
    Prefs.blackBackground = false;
    IJ.setRawThreshold(thresholded, 2, 65535, null);
    IJ.run(thresholded,
           "Convert to Mask",
           "method=Default background=Dark");

    // do operation with ClearCL
    ClearCLImage src = clij.converter(testImp2D2).getClearCLImage();
    ClearCLImage dst = clij.converter(testImp2D2).getClearCLImage();

    Kernels.threshold(clij, src, dst, 2);
    Kernels.multiplyScalar(clij, dst, src, 255);

    ImagePlus thresholdedCL = clij.converter(src).getImagePlus();

    assertTrue(TestUtilities.compareImages(thresholded,
                                           thresholdedCL));

    src.close();
    dst.close();
  }
}