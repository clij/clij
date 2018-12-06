package net.haesleinhuepf.imagej.utilities;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLContext;
import clearcl.ClearCLImage;
import clearcl.enums.HostAccessType;
import clearcl.enums.ImageChannelDataType;
import clearcl.enums.ImageChannelOrder;
import clearcl.enums.KernelAccessType;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import coremem.ContiguousMemoryInterface;
import coremem.enums.NativeTypeEnum;
import coremem.offheap.OffHeapMemory;
import coremem.offheap.OffHeapMemoryAccess;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.imagej.ops.create.img.Imgs;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.*;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import javax.lang.model.type.UnknownTypeException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.UnknownFormatConversionException;

/**
 * This class converts many kinds of images to each other. Hand over
 * whatever type of image you have to the constructor and afterwards
 * ask explictly for the type of image you want. So far, ClearCL
 * images, CoreMems OffHeapPlanarStacks, Imglib2
 * RandomAccessibleInterval and ImageJ1 ImagePlus are supported.
 * <p>
 * Example code:
 * ImageTypeConverter converter = ClearCLIJ.getInstance().converter(imagePlus);
 * <p>
 * ClearCLImage clImage = converter.getClearCLImage();
 * RandomAccessibleInterval rai = converter.getRandomAccessibleInterval();
 * OffHeapPlanarStack stack = converter.getOffHeapPlanarStack();
 * <p>
 * This class is maybe not the most beautiful piece of code I ever
 * wrote, but all the ifs and elses are necessary to convert between
 * ClearCL, CoreMem, ImageJ1 and ImageJ2 worlds.
 * <p>
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * February 2018
 */
public class ImageTypeConverter<T extends RealType<T>>
{
  private ClearCLContext mContext;
  private ClearCLIJ mCLIJ;
  private RandomAccessibleInterval<T>
      mRandomAccessibleInterval =
      null;
  private ClearCLImage mClearCLImage = null;
  private ClearCLBuffer mBuffer = null;

  public ImageTypeConverter(ClearCLIJ pCLIJ,
                            RandomAccessibleInterval<T> pRandomAccessibleInterval) throws
                                                                                   UnknownTypeException
  {
    mContext = pCLIJ.getClearCLContext();
    mCLIJ = pCLIJ;
    mRandomAccessibleInterval = pRandomAccessibleInterval;
  }

  public ImageTypeConverter(ClearCLIJ pCLIJ,
                            ImagePlus pImagePlus)
  {
    mContext = pCLIJ.getClearCLContext();
    mCLIJ = pCLIJ;
    mRandomAccessibleInterval = ImageJFunctions.wrapReal(pImagePlus);
  }

  public ImageTypeConverter(ClearCLIJ pCLIJ,
                            ClearCLImage pImage)
  {
    mContext = pCLIJ.getClearCLContext();
    mCLIJ = pCLIJ;
    mClearCLImage = pImage;
  }

  public ImageTypeConverter(ClearCLIJ pCLIJ,
                            ClearCLBuffer pBuffer) {
    mContext = pCLIJ.getClearCLContext();
    mCLIJ = pCLIJ;
    mBuffer = pBuffer;
  }


  public ClearCLImage getClearCLImage()
  {
    if (mClearCLImage == null)
    {
      if (mRandomAccessibleInterval != null)
      {
        mClearCLImage =
            convertRandomAccessibleIntervalToClearCLImage(
                mRandomAccessibleInterval);
      }
      else if (mBuffer != null)
      {
        mClearCLImage = convertCLBufferToCLImage(mBuffer);
      }
    }
    return mClearCLImage;
  }

  public RandomAccessibleInterval<T> getRandomAccessibleInterval()
  {
    if (mRandomAccessibleInterval == null)
    {
      if (mClearCLImage != null)
      {
        mRandomAccessibleInterval =
            convertClearClImageToRandomAccessibleInterval(mContext,
                                                          mClearCLImage);
      } else if (mBuffer != null) {
        mRandomAccessibleInterval = convertBufferToRandomAccessibleInterval(mBuffer);
      }
    }
    return mRandomAccessibleInterval;
  }

  public ImagePlus getImagePlus()
  {
    ImagePlus result = ImageJFunctions.wrap(getRandomAccessibleInterval(), "img");
    result = new Duplicator().run(result);
    if (result.getNChannels() > 1 && result.getNSlices() == 1) {
      IJ.run(result, "Properties...", "channels=1 slices=" + result.getNChannels() + " frames=1 unit=pixel pixel_width=1.0000 pixel_height=1.0000 voxel_depth=1.0000");
    }
    return result;
  }

  public ClearCLBuffer getClearCLBuffer() {
    ClearCLImage lClImage = getClearCLImage();
    return convertCLImageToCLBuffer(lClImage);
  }

  private ImageChannelDataType nativeTypeToImageChannelType(NativeTypeEnum lType) {
    ImageChannelDataType lImageChannelType = null;

    if (lType == NativeTypeEnum.UnsignedByte)
    {
      lImageChannelType = ImageChannelDataType.UnsignedInt8;
    }
    else if (lType == NativeTypeEnum.Byte)
    {
      lImageChannelType = ImageChannelDataType.SignedInt8;
    }
    else if (lType == NativeTypeEnum.UnsignedShort)
    {
      lImageChannelType = ImageChannelDataType.UnsignedInt16;
    }
    else if (lType == NativeTypeEnum.Short)
    {
      lImageChannelType = ImageChannelDataType.SignedInt16;
    }
    else if (lType == NativeTypeEnum.Float)
    {
      lImageChannelType = ImageChannelDataType.Float;
    }
    return lImageChannelType;

  }

  public static <T extends RealType<T>> RandomAccessibleInterval<T> convertClearClImageToRandomAccessibleInterval(
      ClearCLContext pContext,
      ClearCLImage pClearCLImage)
  {
    T lPixel = null;
    ImgFactory<T> lFactory = null;

    if (pClearCLImage.getChannelDataType()
        == ImageChannelDataType.SignedInt8)
    {
      //System.out.println("byte TYPE2");

      lPixel = (T) new ByteType();
      lFactory = (ImgFactory<T>) new ArrayImgFactory<ByteType>();
    }
    else if (pClearCLImage.getChannelDataType()
             == ImageChannelDataType.UnsignedInt8
             || pClearCLImage.getChannelDataType()
                == ImageChannelDataType.UnsignedNormalizedInt8)
    {
      //System.out.println("ubyte TYPE2");

      lPixel = (T) new UnsignedByteType();
      lFactory =
          (ImgFactory<T>) new ArrayImgFactory<UnsignedByteType>();
    }
    else if (pClearCLImage.getChannelDataType()
             == ImageChannelDataType.SignedInt16)
    {
      //System.out.println("short TYPE2");

      lPixel = (T) new ShortType();
      lFactory = (ImgFactory<T>) new ArrayImgFactory<ShortType>();
    }
    else if (pClearCLImage.getChannelDataType()
             == ImageChannelDataType.UnsignedInt16
             || pClearCLImage.getChannelDataType()
                == ImageChannelDataType.UnsignedNormalizedInt16)
    {
      //System.out.println("ushort TYPE2");

      lPixel = (T) new UnsignedShortType();
      lFactory =
          (ImgFactory<T>) new ArrayImgFactory<UnsignedShortType>();
    }
    else if (pClearCLImage.getChannelDataType()
             == ImageChannelDataType.Float)
    {

      //System.out.println("float TYPE2");

      lPixel = (T) new FloatType();
      lFactory = (ImgFactory<T>) new ArrayImgFactory<FloatType>();
    }
    else
    {
      throw new UnknownFormatConversionException(
          "Cannot convert image of type "
          + pClearCLImage.getChannelDataType().name());
    }

    return convertClearClImageToRandomAccessibleInterval(pContext,
                                                         pClearCLImage,
                                                         lFactory,
                                                         lPixel);
  }

  public static <T extends RealType<T>> RandomAccessibleInterval<T> convertClearClImageToRandomAccessibleInterval(
      ClearCLContext pContext,
      ClearCLImage pClearCLImage,
      ImgFactory<T> pFactory,
      T pPixel) {

    Dimensions lDimensions = new Dimensions() {
      @Override
      public void dimensions(long[] longs) {
        System.arraycopy(pClearCLImage.getDimensions(),
                0,
                longs,
                0,
                pClearCLImage.getDimensions().length);
      }

      @Override
      public long dimension(int i) {
        return pClearCLImage.getDimensions()[i];
      }

      @Override
      public int numDimensions() {
        return pClearCLImage.getDimensions().length;
      }
    };

    Img<T> img = Imgs.create(pFactory, lDimensions, pPixel);

    NativeTypeEnum lInputType = pClearCLImage.getNativeType();

    long lBytesPerPixel = lInputType.getSizeInBytes();
    //System.out.println("lBytesPerPixel " + lBytesPerPixel);
    long
            lNumberOfPixels =
            pClearCLImage.getWidth()
                    * pClearCLImage.getHeight()
                    * pClearCLImage.getDepth();


    long time = System.currentTimeMillis();

    ClearCLBuffer
            buffer =
            pContext.createBuffer(lInputType, lNumberOfPixels);

    System.out.println("Create buffer took " + (System.currentTimeMillis() - time) + " msec");
    time = System.currentTimeMillis();

    pClearCLImage.copyTo(buffer, true);
    System.out.println("Copy to buffer took " + (System.currentTimeMillis() - time) + " msec");

    copyClBufferToImg(buffer, img, lBytesPerPixel, lNumberOfPixels);
    return img;
  }

  public static <T extends RealType<T>> RandomAccessibleInterval<T> convertClearClBufferToRandomAccessibleInterval(
          ClearCLContext pContext,
          ClearCLBuffer pClearCLBuffer)
  {
    T lPixel = null;
    ImgFactory<T> lFactory = null;

    int numberOfPixels = (int)(pClearCLBuffer.getWidth() * pClearCLBuffer.getHeight() * pClearCLBuffer.getDepth());
    if (numberOfPixels <= 0) {
      throw new IllegalArgumentException("Wrong image size!");
    }

    // Todo: in case of large images, we might use PlanarImgs!

    if (pClearCLBuffer.getNativeType()
            == NativeTypeEnum.Byte ||
            pClearCLBuffer.getNativeType()
            == NativeTypeEnum.UnsignedByte)
    {
      ByteBuffer byteBuffer = ByteBuffer.allocate(numberOfPixels);
      pClearCLBuffer.writeTo(byteBuffer, true);
      return (Img<T>) ArrayImgs.bytes(byteBuffer.array(), pClearCLBuffer.getDimensions());
    }
    else if (pClearCLBuffer.getNativeType()
            == NativeTypeEnum.Short ||
            pClearCLBuffer.getNativeType()
            == NativeTypeEnum.UnsignedShort)
    {
      ShortBuffer shortBuffer = ShortBuffer.allocate(numberOfPixels);
      pClearCLBuffer.writeTo(shortBuffer, true);
      return (Img<T>) ArrayImgs.shorts(shortBuffer.array(), pClearCLBuffer.getDimensions());
    }
    else if (pClearCLBuffer.getNativeType()
            == NativeTypeEnum.Float)
    {
      FloatBuffer floatBuff = FloatBuffer.allocate(numberOfPixels);
      pClearCLBuffer.writeTo(floatBuff, true);
      return (Img<T>) ArrayImgs.floats(floatBuff.array(), pClearCLBuffer.getDimensions());
    }
    else
    {
      throw new UnknownFormatConversionException(
              "Cannot convert image of type "
                      + pClearCLBuffer.getNativeType().name());
    }
  }

  private static <T extends RealType<T>> void copyClBufferToImg(ClearCLBuffer buffer, Img<T> img, long lBytesPerPixel, long lNumberOfPixels)
  {
    NativeTypeEnum lInputType = buffer.getNativeType();

    long numberOfBytesToAllocate = lBytesPerPixel * lNumberOfPixels;

    long time = System.currentTimeMillis();

    ContiguousMemoryInterface
            contOut =
            new OffHeapMemory("memmm",
                    null,
                    OffHeapMemoryAccess.allocateMemory(
                            numberOfBytesToAllocate),
                    numberOfBytesToAllocate);

    System.out.println("Create offheap memory took " + (System.currentTimeMillis() - time) + " msec");
    time = System.currentTimeMillis();


    buffer.writeTo(contOut, true);
    System.out.println("Copy to offheap memory took " + (System.currentTimeMillis() - time) + " msec");

    time = System.currentTimeMillis();


    int lMemoryOffset = 0;
    Cursor<T> lCursor = img.cursor();


    if (lInputType == NativeTypeEnum.Byte
        || lInputType == NativeTypeEnum.UnsignedByte)
    {
      while (lCursor.hasNext())
      {
        lCursor.next().setReal(contOut.getByte(lMemoryOffset));
        lMemoryOffset += lBytesPerPixel;
      }
    }
    else if (lInputType == NativeTypeEnum.Short
             || lInputType == NativeTypeEnum.UnsignedShort)
    {
      while (lCursor.hasNext())
      {
        lCursor.next().setReal(contOut.getShort(lMemoryOffset));
        lMemoryOffset += lBytesPerPixel;
      }
    }
    else if (lInputType == NativeTypeEnum.Float)
    {
      while (lCursor.hasNext())
      {
        lCursor.next().setReal(contOut.getFloat(lMemoryOffset));
        lMemoryOffset += lBytesPerPixel;
      }
    }
    else
    {
      throw new UnknownFormatConversionException(
          "Cannot convert object of type " + lInputType.getClass()
                                                       .getCanonicalName());
    }

    System.out.println("Copy to cursor took " + (System.currentTimeMillis() - time) + " msec");

  }

  public static <T extends RealType<T>> void copyRandomAccessibleIntervalToClearCLImage(RandomAccessibleInterval<T> pRandomAccessibleInterval, ClearCLImage lClearClImage) {


    T
            lPixel =
            Views.iterable(pRandomAccessibleInterval).firstElement();

    long[]
            dimensions =
            new long[pRandomAccessibleInterval.numDimensions()];
    pRandomAccessibleInterval.dimensions(dimensions);

    long sumDimensions = 1;
    for (int i = 0; i < dimensions.length; i++)
    {
      sumDimensions *= dimensions[i];
    }

    int count = 0;
    Cursor<T>
            cursor =
            Views.iterable(pRandomAccessibleInterval).cursor();

    if (lClearClImage.getChannelDataType() == ImageChannelDataType.SignedInt8 ||
            lClearClImage.getChannelDataType() == ImageChannelDataType.UnsignedInt8     )
    {

      byte[] inputArray = new byte[(int) sumDimensions];
      while (cursor.hasNext())
      {
        inputArray[count] = (byte) cursor.next().getRealFloat();
        count++;
      }
      lClearClImage.readFrom(inputArray, true);
    }
    else if (lClearClImage.getChannelDataType() == ImageChannelDataType.SignedInt16 ||
            lClearClImage.getChannelDataType() == ImageChannelDataType.UnsignedInt16     )
    {

      short[] inputArray = new short[(int) sumDimensions];
      while (cursor.hasNext())
      {
        inputArray[count] = (short) cursor.next().getRealFloat();
        count++;
      }
      lClearClImage.readFrom(inputArray, true);
    }
    else if (lClearClImage.getChannelDataType() == ImageChannelDataType.Float  )
    {
      float[] inputArray = new float[(int) sumDimensions];
      while (cursor.hasNext())
      {
        inputArray[count] = cursor.next().getRealFloat();
        count++;
      }
      lClearClImage.readFrom(inputArray, true);
    }

  }

  public <T extends RealType<T>> ClearCLImage convertRandomAccessibleIntervalToClearCLImage(
      RandomAccessibleInterval<T> pRandomAccessibleInterval)
  {
    long[]
            dimensions =
            new long[pRandomAccessibleInterval.numDimensions()];
    pRandomAccessibleInterval.dimensions(dimensions);

    ImageChannelDataType lImageChannelType = null;

    T
        lPixel =
        Views.iterable(pRandomAccessibleInterval).firstElement();

    if (lPixel instanceof UnsignedByteType)
    {
      //System.out.println("ubyte TYPE3");

      lImageChannelType = ImageChannelDataType.UnsignedInt8;
    }
    else if (lPixel instanceof ByteType)
    {
      //System.out.println("byte TYPE3");

      lImageChannelType = ImageChannelDataType.SignedInt8;
    }
    else if (lPixel instanceof UnsignedShortType)
    {
      //System.out.println("ushort TYPE3");

      lImageChannelType = ImageChannelDataType.UnsignedInt16;
    }
    else if (lPixel instanceof ShortType)
    {
      //System.out.println("short TYPE3");

      lImageChannelType = ImageChannelDataType.SignedInt16;
    }
    else if (lPixel instanceof FloatType)
    {
      //System.out.println("float TYPE3");

      lImageChannelType = ImageChannelDataType.Float;
    }
    else
    {
      throw new IllegalArgumentException(
          "Cannot convert image of type " + lImageChannelType.name());
    }

    ClearCLImage
        lClearClImage =
        mContext.createImage(HostAccessType.ReadWrite,
                             KernelAccessType.ReadWrite,
                             ImageChannelOrder.R,
                             lImageChannelType,
                             dimensions);

    copyRandomAccessibleIntervalToClearCLImage(pRandomAccessibleInterval, lClearClImage);

    return lClearClImage;
  }

  private ClearCLImage convertCLBufferToCLImage(ClearCLBuffer pBuffer) {
    ImageChannelDataType pType = nativeTypeToImageChannelType(pBuffer.getNativeType());

    ClearCLImage output = mCLIJ.createCLImage(pBuffer.getDimensions(), pType);
    Kernels.convert(mCLIJ, pBuffer, output);
    return output;
  }

  private ClearCLBuffer convertCLImageToCLBuffer(ClearCLImage pClImage) {
      System.out.println("Native type: " + pClImage.getNativeType());
    ClearCLBuffer output = mCLIJ.createCLBuffer(pClImage.getDimensions(), pClImage.getNativeType());
    Kernels.copy(mCLIJ, pClImage, output);
    return output;
  }

  private RandomAccessibleInterval<T> convertBufferToRandomAccessibleInterval(ClearCLBuffer pBuffer) {
      //ClearCLImage lCLImage = convertCLBufferToCLImage(pBuffer);
      //return convertClearClImageToRandomAccessibleInterval(mContext, lCLImage);
    return convertClearClBufferToRandomAccessibleInterval(mContext, pBuffer);
  }
}
