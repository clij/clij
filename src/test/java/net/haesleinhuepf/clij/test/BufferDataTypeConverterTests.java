package net.haesleinhuepf.clij.test;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import ij.IJ;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import org.junit.Test;

/**
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * April 2018
 */
public class BufferDataTypeConverterTests
{
  @Test
  public void testFloatToByteConversion() {
    CLIJ clij = CLIJ.getInstance();
    ClearCLBuffer bufferIn = clij.createCLBuffer(new long[]{10,10,10}, NativeTypeEnum.Float);
    ClearCLBuffer bufferOut = clij.createCLBuffer(new long[]{10,10,10}, NativeTypeEnum.Byte);

    Kernels.copy(clij, bufferIn, bufferOut);
    clij.close();
  }


  @Test
  public void testFloatToUnsignedShortConversion() {
    CLIJ clij = CLIJ.getInstance();
    ClearCLBuffer bufferIn = clij.createCLBuffer(new long[]{10,10,10}, NativeTypeEnum.Float);
    ClearCLBuffer bufferOut = clij.createCLBuffer(new long[]{10,10,10}, NativeTypeEnum.UnsignedShort);

    Kernels.copy(clij, bufferIn, bufferOut);
    clij.close();
  }


  @Test
  public void testUnsignedShortToByteConversion() {
    CLIJ clij = CLIJ.getInstance();
    ClearCLBuffer bufferIn = clij.createCLBuffer(new long[]{10,10,10}, NativeTypeEnum.UnsignedShort);
    ClearCLBuffer bufferOut = clij.createCLBuffer(new long[]{10,10,10}, NativeTypeEnum.Byte);

    Kernels.copy(clij, bufferIn, bufferOut);
    clij.close();
  }
}
