package clearcl.imagej.utilities;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLContext;
import clearcl.ClearCLImage;
import clearcl.enums.HostAccessType;
import clearcl.enums.ImageChannelDataType;
import clearcl.enums.ImageChannelOrder;
import clearcl.enums.KernelAccessType;
import clearcontrol.stack.OffHeapPlanarStack;
import coremem.ContiguousMemoryInterface;
import coremem.enums.NativeTypeEnum;
import coremem.offheap.OffHeapMemory;
import coremem.offheap.OffHeapMemoryAccess;
import ij.ImagePlus;
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
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import javax.lang.model.type.UnknownTypeException;
import java.util.UnknownFormatConversionException;

/**
 * This class converts many kinds of images to each other. Hand over
 * whatever type of image you have to the constructor and afterwards
 * ask explictly for the tpye of image you want. So far, ClearCL
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
  private RandomAccessibleInterval<T>
      mRandomAccessibleInterval =
      null;
  private ClearCLImage mClearCLImage = null;
  private OffHeapPlanarStack mImageStack = null;

  public ImageTypeConverter(ClearCLContext pClearCLContext,
                            RandomAccessibleInterval<T> pRandomAccessibleInterval) throws
                                                                                   UnknownTypeException
  {
    mContext = pClearCLContext;
    mRandomAccessibleInterval = pRandomAccessibleInterval;
  }

  public ImageTypeConverter(ClearCLContext pClearCLContext,
                            ImagePlus pImagePlus)
  {
    mContext = pClearCLContext;
    mRandomAccessibleInterval = ImageJFunctions.wrapReal(pImagePlus);
  }

  public ImageTypeConverter(ClearCLContext pClearCLContext,
                            ClearCLImage pImage)
  {
    mContext = pClearCLContext;
    mClearCLImage = pImage;
  }

  public ImageTypeConverter(ClearCLContext pClearCLContext,
                            OffHeapPlanarStack pStack)
  {
    mContext = pClearCLContext;
    mImageStack = pStack;
  }

  public ClearCLImage getClearCLImage()
  {
    if (mClearCLImage == null)
    {
      if (mRandomAccessibleInterval != null)
      {
        mClearCLImage =
            convertRanomdAccessibleIntervalToClearCLImage(mContext,
                                                          mRandomAccessibleInterval);
      }
      else if (mImageStack != null)
      {
        mClearCLImage =
            convertOffHeapPlanarStackToClearCLImage(mContext,
                                                    mImageStack);
      }
    }
    return mClearCLImage;
  }

  public OffHeapPlanarStack getOffHeapPlanarStack()
  {
    if (mImageStack == null)
    {
      if (mClearCLImage != null)
      {
        mImageStack =
            convertClearClImageToOffHeapPlanarStack(mClearCLImage);
      }
      else if (mRandomAccessibleInterval != null)
      {
        mImageStack =
            convertRandomAccessibleIntervalToOffHeapPlanarStack(
                mRandomAccessibleInterval);
      }
    }
    return mImageStack;
  }

  public RandomAccessibleInterval<T> getRandomAccessibleInterval()
  {
    if (mRandomAccessibleInterval == null)
    {
      if (mImageStack != null)
      {
        mRandomAccessibleInterval =
            convertOffHeapPlanarStackToRandomAccessibleInterval(
                mImageStack);
      }
      else if (mClearCLImage != null)
      {
        mRandomAccessibleInterval =
            convertClearClImageToRandomAccessibleInterval(mContext,
                                                          mClearCLImage);
      }
    }
    return mRandomAccessibleInterval;
  }

  public ImagePlus getImagePlus()
  {
    return ImageJFunctions.wrap(getRandomAccessibleInterval(), "img");
  }

  public static <T extends RealType<T>> OffHeapPlanarStack convertClearClImageToOffHeapPlanarStack(
      ClearCLImage pClearCLImage)
  {
    OffHeapPlanarStack
        lResultStack =
        new OffHeapPlanarStack(true,
                               0,
                               pClearCLImage.getNativeType(),
                               pClearCLImage.getNumberOfChannels(),
                               pClearCLImage.getDimensions());
    pClearCLImage.writeTo(lResultStack.getContiguousMemory(), true);

    return lResultStack;

  }

  public static <T extends RealType<T>> OffHeapPlanarStack convertRandomAccessibleIntervalToOffHeapPlanarStack(
      RandomAccessibleInterval<T> pRandomAccessibleInterval)
  {
    long[]
        lDimensions =
        new long[pRandomAccessibleInterval.numDimensions()];
    pRandomAccessibleInterval.dimensions(lDimensions);

    T
        pPixel =
        Views.iterable(pRandomAccessibleInterval).firstElement();
    NativeTypeEnum lType = null;
    if (pPixel instanceof UnsignedShortType)
    {
      lType = NativeTypeEnum.UnsignedShort;
    }
    else if (pPixel instanceof ShortType)
    {
      lType = NativeTypeEnum.Short;
    }
    else if (pPixel instanceof UnsignedByteType)
    {
      lType = NativeTypeEnum.UnsignedByte;
    }
    else if (pPixel instanceof ByteType)
    {
      lType = NativeTypeEnum.Byte;
    }
    else if (pPixel instanceof FloatType)
    {
      lType = NativeTypeEnum.Float;
    }
    else
    {
      throw new UnknownFormatConversionException("Unknown type: "
                                                 + pPixel
                                                     .getClass()
                                                     .getCanonicalName());
    }

    OffHeapPlanarStack
        lStack =
        new OffHeapPlanarStack(true, 0, lType, 1, lDimensions);

    Cursor<T>
        cursor =
        Views.iterable(pRandomAccessibleInterval).cursor();

    long lOffSet = 0;
    long lElementSize = lType.getSizeInBytes();
    if (lType == NativeTypeEnum.UnsignedShort || lType == NativeTypeEnum.Short)
    {
      while (cursor.hasNext())
      {
        T element = cursor.next();
        lStack.getContiguousMemory()
              .setShort(lOffSet, (short) element.getRealDouble());
        lOffSet += lElementSize;
      }
    }
    else if (lType == NativeTypeEnum.UnsignedByte || lType == NativeTypeEnum.Byte)
    {
      while (cursor.hasNext())
      {
        T element = cursor.next();
        lStack.getContiguousMemory()
              .setByte(lOffSet, (byte) element.getRealDouble());
        lOffSet += lElementSize;
      }
    }
    else if (lType == NativeTypeEnum.Float)
    {
      while (cursor.hasNext())
      {
        T element = cursor.next();
        lStack.getContiguousMemory()
              .setFloat(lOffSet, (float) element.getRealDouble());
        lOffSet += lElementSize;
      }
    }
    else
    {
      throw new UnknownFormatConversionException("Unknown type: "
                                                 + pRandomAccessibleInterval
                                                     .getClass()
                                                     .getCanonicalName());
    }

    return lStack;
  }

  public static <T extends RealType<T>> RandomAccessibleInterval<T> convertOffHeapPlanarStackToRandomAccessibleInterval(
      OffHeapPlanarStack pImageStack)
  {
    Img<T> lReturnImg = null;

    final ContiguousMemoryInterface
        contiguousMemory =
        pImageStack.getContiguousMemory();

    int numDimensions = pImageStack.getNumberOfDimensions();
    if (pImageStack.getNumberOfChannels() > 1)
    {
      // Channels are an additional dimension in imglib2 world
      numDimensions++;
    }

    long[] dimensions = new long[numDimensions];
    dimensions[0] = pImageStack.getWidth();
    dimensions[1] = pImageStack.getHeight();
    if (dimensions.length > 2)
    {
      dimensions[2] = pImageStack.getDepth();
    }
    if (dimensions.length > 3)
    {
      dimensions[3] = pImageStack.getNumberOfChannels();
    }

    if (pImageStack.getDataType() == NativeTypeEnum.Float
        || pImageStack.getDataType() == NativeTypeEnum.HalfFloat)
    {
      System.out.println("float TYPE");
      float[]
          pixelArray =
          new float[(int) (contiguousMemory.getSizeInBytes()
                           / pImageStack.getBytesPerVoxel())
                    % Integer.MAX_VALUE];
      contiguousMemory.copyTo(pixelArray);
      lReturnImg = (Img<T>) ArrayImgs.floats(pixelArray, dimensions);
    }
    else if (pImageStack.getDataType() == NativeTypeEnum.Short
             || pImageStack.getDataType()
                == NativeTypeEnum.UnsignedShort)
    {
      System.out.println("short TYPE");
      short[]
          pixelArray =
          new short[(int) (contiguousMemory.getSizeInBytes()
                           / pImageStack.getBytesPerVoxel())
                    % Integer.MAX_VALUE];
      contiguousMemory.copyTo(pixelArray);
      lReturnImg = (Img<T>) ArrayImgs.shorts(pixelArray, dimensions);
    }
    else if (pImageStack.getDataType() == NativeTypeEnum.Byte
             || pImageStack.getDataType()
                == NativeTypeEnum.UnsignedByte)
    {

      System.out.println("byte TYPE");
      byte[]
          pixelArray =
          new byte[(int) (contiguousMemory.getSizeInBytes()
                          / pImageStack.getBytesPerVoxel())
                   % Integer.MAX_VALUE];
      contiguousMemory.copyTo(pixelArray);
      lReturnImg = (Img<T>) ArrayImgs.bytes(pixelArray, dimensions);
    }
    else if (pImageStack.getDataType() == NativeTypeEnum.Int
             || pImageStack.getDataType()
                == NativeTypeEnum.UnsignedInt)
    {

      System.out.println("int TYPE");
      int[]
          pixelArray =
          new int[(int) (contiguousMemory.getSizeInBytes()
                         / pImageStack.getBytesPerVoxel())
                  % Integer.MAX_VALUE];
      contiguousMemory.copyTo(pixelArray);
      lReturnImg = (Img<T>) ArrayImgs.ints(pixelArray, dimensions);
    }
    else if (pImageStack.getDataType() == NativeTypeEnum.Long
             || pImageStack.getDataType()
                == NativeTypeEnum.UnsignedLong)
    {

      System.out.println("long TYPE");
      long[]
          pixelArray =
          new long[(int) (contiguousMemory.getSizeInBytes()
                          / pImageStack.getBytesPerVoxel())
                   % Integer.MAX_VALUE];
      contiguousMemory.copyTo(pixelArray);
      lReturnImg = (Img<T>) ArrayImgs.longs(pixelArray, dimensions);
    }
    else
    {
      throw new UnknownFormatConversionException("Unknown type: "
                                                 + pImageStack.getDataType());
    }

    lReturnImg.cursor().reset();

    // in ImageJ, the dimension order must be X, Y, C, Z
    if (dimensions.length == 4)
    {
      return Views.permute(lReturnImg, 2, 3);
    }

    return lReturnImg;
  }

  public static ClearCLImage convertOffHeapPlanarStackToClearCLImage(
      ClearCLContext pContext,
      OffHeapPlanarStack pImageStack)
  {
    long[] dimensions = pImageStack.getDimensions();

    ImageChannelDataType lImageChannelType = null;

    NativeTypeEnum lType = pImageStack.getDataType();

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

    ClearCLImage
        lClearClImage =
        pContext.createImage(HostAccessType.ReadWrite,
                             KernelAccessType.ReadWrite,
                             ImageChannelOrder.Intensity,
                             lImageChannelType,
                             dimensions);

    lClearClImage.readFrom(pImageStack.getContiguousMemory(), true);
    return lClearClImage;
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
      System.out.println("byte TYPE2");

      lPixel = (T) new ByteType();
      lFactory = (ImgFactory<T>) new ArrayImgFactory<ByteType>();
    }
    else if (pClearCLImage.getChannelDataType()
             == ImageChannelDataType.UnsignedInt8)
    {
      System.out.println("ubyte TYPE2");

      lPixel = (T) new UnsignedByteType();
      lFactory =
          (ImgFactory<T>) new ArrayImgFactory<UnsignedByteType>();
    }
    else if (pClearCLImage.getChannelDataType()
             == ImageChannelDataType.SignedInt16)
    {
      System.out.println("short TYPE2");

      lPixel = (T) new ShortType();
      lFactory = (ImgFactory<T>) new ArrayImgFactory<ShortType>();
    }
    else if (pClearCLImage.getChannelDataType()
             == ImageChannelDataType.UnsignedInt16)
    {
      System.out.println("ushort TYPE2");

      lPixel = (T) new UnsignedShortType();
      lFactory =
          (ImgFactory<T>) new ArrayImgFactory<UnsignedShortType>();
    }
    else if (pClearCLImage.getChannelDataType()
             == ImageChannelDataType.Float)
    {

      System.out.println("float TYPE2");

      lPixel = (T) new FloatType();
      lFactory = (ImgFactory<T>) new ArrayImgFactory<FloatType>();
    } else {
      throw new UnknownFormatConversionException("Cannot convert image of type " + pClearCLImage.getChannelDataType().name());
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
      T pPixel)
  {

    Dimensions lDimensions = new Dimensions()
    {
      @Override public void dimensions(long[] longs)
      {
        System.arraycopy(pClearCLImage.getDimensions(),
                         0,
                         longs,
                         0,
                         pClearCLImage.getDimensions().length);
      }

      @Override public long dimension(int i)
      {
        return pClearCLImage.getDimensions()[i];
      }

      @Override public int numDimensions()
      {
        return pClearCLImage.getDimensions().length;
      }
    };

    Img<T> img = Imgs.create(pFactory, lDimensions, pPixel);

    NativeTypeEnum lInputType = pClearCLImage.getNativeType();

    long lBytesPerPixel = lInputType.getSizeInBytes();
    System.out.println("lBytesPerPixel " + lBytesPerPixel);
    long
        lNumberOfPixels =
        pClearCLImage.getWidth()
        * pClearCLImage.getHeight()
        * pClearCLImage.getDepth();

    long numberOfBytesToAllocate = lBytesPerPixel * lNumberOfPixels;

    ContiguousMemoryInterface
        contOut =
        new OffHeapMemory("memmm",
                          null,
                          OffHeapMemoryAccess.allocateMemory(
                              numberOfBytesToAllocate),
                          numberOfBytesToAllocate);

    ClearCLBuffer
        buffer =
        pContext.createBuffer(lInputType, lNumberOfPixels);

    System.out.println("numberOfPixels " + lNumberOfPixels);
    System.out.println("buffer.getSizeInBytes() " + buffer.getSizeInBytes());
    System.out.println("contOut.getSizeInBytes() " + contOut.getSizeInBytes());

    pClearCLImage.copyTo(buffer, true);
    buffer.writeTo(contOut, true);

    int lMemoryOffset = 0;
    Cursor<T> lCursor = img.cursor();
    double sum = 0;
    while (lCursor.hasNext())
    {
      if (lInputType == NativeTypeEnum.Byte
          || lInputType == NativeTypeEnum.UnsignedByte)
      {
        lCursor.next().setReal(contOut.getByte(lMemoryOffset));
      }
      else if (lInputType == NativeTypeEnum.Short
               || lInputType == NativeTypeEnum.UnsignedShort)
      {
        lCursor.next().setReal(contOut.getShort(lMemoryOffset));
      }
      else if (lInputType == NativeTypeEnum.Float)
      {
        lCursor.next().setReal(contOut.getFloat(lMemoryOffset));
      }
      else
      {
        throw new UnknownFormatConversionException(
            "Cannot convert object of type " + lInputType.getClass()
                                                         .getCanonicalName());
      }
      sum += lCursor.get().getRealDouble();
      lMemoryOffset += lBytesPerPixel;
    }
    System.out.println("sumss "+  sum);
    return img;
  }

  public static <T extends RealType<T>> ClearCLImage convertRanomdAccessibleIntervalToClearCLImage(
      ClearCLContext pContext,
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
      System.out.println("ubyte TYPE3");

      lImageChannelType = ImageChannelDataType.UnsignedInt8;
    }
    else if (lPixel instanceof ByteType)
    {
      System.out.println("byte TYPE3");

      lImageChannelType = ImageChannelDataType.SignedInt8;
    }
    else if (lPixel instanceof UnsignedShortType)
    {
      System.out.println("ushort TYPE3");

      lImageChannelType = ImageChannelDataType.UnsignedInt16;
    }
    else if (lPixel instanceof ShortType)
    {
      System.out.println("short TYPE3");

      lImageChannelType = ImageChannelDataType.SignedInt16;
    }
    else if (lPixel instanceof FloatType)
    {
      System.out.println("float TYPE3");

      lImageChannelType = ImageChannelDataType.Float;
    } else {
      throw new IllegalArgumentException("Cannot convert image of type " + lImageChannelType.name());
    }

    ClearCLImage
        lClearClImage =
        pContext.createImage(HostAccessType.ReadWrite,
                             KernelAccessType.ReadWrite,
                             ImageChannelOrder.Intensity,
                             lImageChannelType,
                             dimensions);

    long sumDimensions = 1;
    for (int i = 0; i < dimensions.length; i++)
    {
      sumDimensions *= dimensions[i];
    }

    int count = 0;
    Cursor<T>
        cursor =
        Views.iterable(pRandomAccessibleInterval).cursor();

    if (lPixel instanceof UnsignedByteType
        || lPixel instanceof ByteType)
    {

      byte[] inputArray = new byte[(int) sumDimensions];
      while (cursor.hasNext())
      {
        inputArray[count] = (byte) cursor.next().getRealFloat();
        count++;
      }
      lClearClImage.readFrom(inputArray, true);
    }
    else if (lPixel instanceof UnsignedShortType
             || lPixel instanceof ShortType)
    {

      short[] inputArray = new short[(int) sumDimensions];
      while (cursor.hasNext())
      {
        inputArray[count] = (short) cursor.next().getRealFloat();
        count++;
      }
      lClearClImage.readFrom(inputArray, true);
    }
    else if (lPixel instanceof FloatType)
    {

      float[] inputArray = new float[(int) sumDimensions];
      while (cursor.hasNext())
      {
        inputArray[count] = cursor.next().getRealFloat();
        count++;
      }
      lClearClImage.readFrom(inputArray, true);
    }

    return lClearClImage;
  }
}
