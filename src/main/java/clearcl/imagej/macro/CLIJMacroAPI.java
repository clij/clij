package clearcl.imagej.macro;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLContext;
import clearcl.ClearCLImage;
import clearcl.ClearCLPeerPointer;
import clearcl.enums.HostAccessType;
import clearcl.enums.KernelAccessType;
import clearcl.enums.MemAllocMode;
import clearcl.imagej.ClearCLIJ;
import clearcl.imagej.kernels.Kernels;
import coremem.enums.NativeTypeEnum;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import java.awt.image.Kernel;
import java.nio.FloatBuffer;
import java.util.HashMap;

import static clearcl.imagej.utilities.CLKernelExecutor.MAX_ARRAY_SIZE;

/**
 * The CLIJMacroAPI contains all methods which can be called from ImageJ macro
 *
 * Author: @haesleinhuepf
 * 11 2018
 */
public class CLIJMacroAPI {

    public static boolean absolute(ClearCLIJ clij,
                                   ClearCLBuffer src,
                                   ClearCLBuffer dst)
    {
        return Kernels.absolute(clij, src, dst);
    }

    public static boolean addPixelwise(ClearCLIJ clij,
                                       ClearCLBuffer src,
                                       ClearCLBuffer src1,
                                       ClearCLBuffer dst)
    {
        return Kernels.addPixelwise(clij, src, src1, dst);
    }

    public static boolean addScalar(ClearCLIJ clij,
                                    ClearCLBuffer src,
                                    ClearCLBuffer dst,
                                    Float scalar)
    {
        return Kernels.addScalar(clij, src, dst, scalar);
    }

    public static boolean addWeightedPixelwise(ClearCLIJ clij,
                                               ClearCLBuffer src,
                                               ClearCLBuffer src1,
                                               ClearCLBuffer dst,
                                               Float factor,
                                               Float factor1)
    {
        return Kernels.addWeightedPixelwise(clij, src, src1, dst, factor, factor1);
    }

    public static boolean argMaxProjection(ClearCLIJ clij,
                                           ClearCLBuffer src,
                                           ClearCLBuffer dst_max,
                                           ClearCLBuffer dst_arg)
    {
        return Kernels.argMaxProjection(clij, src, dst_max, dst_arg);
    }

    public static boolean binaryAnd(ClearCLIJ clij,
                                    ClearCLBuffer src1,
                                    ClearCLBuffer src2,
                                    ClearCLBuffer dst)
    {
        return Kernels.binaryAnd(clij, src1, src2, dst);
    }

    public static boolean binaryNot(ClearCLIJ clij,
                                    ClearCLBuffer src1,
                                    ClearCLBuffer dst)
    {
        return Kernels.binaryNot(clij, src1, dst);
    }

    public static boolean binaryOr(ClearCLIJ clij,
                                   ClearCLBuffer src1,
                                   ClearCLBuffer src2,
                                   ClearCLBuffer dst)
    {
        return Kernels.binaryOr(clij, src1, src2, dst);
    }

    public static boolean blur(ClearCLIJ clij,
                               ClearCLBuffer src,
                               ClearCLBuffer dst,
                               Integer nX,
                               Integer nY,
                               Float sigmaX,
                               Float sigmaY)
    {
        return Kernels.blur(clij, src, dst, nX, nY, sigmaX, sigmaY);
    }

    public static boolean blur(ClearCLIJ clij,
                               ClearCLBuffer src,
                               ClearCLBuffer dst,
                               Integer nX,
                               Integer nY,
                               Integer nZ,
                               Float sigmaX,
                               Float sigmaY,
                               Float sigmaZ)
    {
        return Kernels.blur(clij, src, dst, nX, nY, nZ, sigmaX, sigmaY, sigmaZ);
    }

    public static boolean copy(ClearCLIJ clij,
                               ClearCLBuffer src,
                               ClearCLBuffer dst)
    {
        return Kernels.copy(clij, src, dst);
    }

    public static boolean copySlice(ClearCLIJ clij,
                                    ClearCLBuffer src,
                                    ClearCLBuffer dst,
                                    Integer planeIndex)
    {
        return Kernels.copySlice(clij, src, dst, planeIndex);
    }

    public static boolean crop(ClearCLIJ clij,
                               ClearCLBuffer src,
                               ClearCLBuffer dst,
                               Integer startX,
                               Integer startY,
                               Integer startZ)
    {
        return Kernels.crop(clij, src, dst, startX, startY, startZ);
    }

    public static boolean crop(ClearCLIJ clij,
                               ClearCLBuffer src,
                               ClearCLBuffer dst,
                               Integer startX,
                               Integer startY)
    {
        return Kernels.crop(clij, src, dst, startX, startY);
    }

    public static boolean detectMaxima(ClearCLIJ clij,
                                       ClearCLBuffer src,
                                       ClearCLBuffer dst,
                                       Integer radius)
    {
        return Kernels.detectMaxima(clij, src, dst, radius);
    }

    public static boolean detectMaximaSliceBySlice(ClearCLIJ clij,
                                                   ClearCLBuffer src,
                                                   ClearCLBuffer dst,
                                                   Integer radius)
    {
        return Kernels.detectMaximaSliceBySlice(clij, src, dst, radius);
    }

    public static boolean detectMinima(ClearCLIJ clij,
                                       ClearCLBuffer src,
                                       ClearCLBuffer dst,
                                       Integer radius)
    {
        return Kernels.detectMinima(clij, src, dst, radius);
    }

    public static boolean detectMinimaSliceBySlice(ClearCLIJ clij,
                                                   ClearCLBuffer src,
                                                   ClearCLBuffer dst,
                                                   Integer radius)
    {
        return Kernels.detectMinimaSliceBySlice(clij, src, dst, radius);
    }

    public static boolean detectOptima(ClearCLIJ clij,
                                       ClearCLBuffer src,
                                       ClearCLBuffer dst,
                                       Integer radius,
                                       Boolean detectMaxima)
    {
        return Kernels.detectOptima(clij, src, dst, radius, detectMaxima);
    }

    public static boolean detectOptimaSliceBySlice(ClearCLIJ clij,
                                                   ClearCLBuffer src,
                                                   ClearCLBuffer dst,
                                                   Integer radius,
                                                   Boolean detectMaxima)
    {
        return Kernels.detectOptimaSliceBySlice(clij, src, dst, radius, detectMaxima);
    }

    public static boolean dilate(ClearCLIJ clij,
                                 ClearCLBuffer src,
                                 ClearCLBuffer dst)
    {
        return Kernels.dilate(clij, src, dst);
    }

    public static boolean dividePixelwise(ClearCLIJ clij,
                                          ClearCLBuffer src,
                                          ClearCLBuffer src1,
                                          ClearCLBuffer dst)
    {
        return Kernels.dividePixelwise(clij, src, src1, dst);
    }

    public static boolean downsample(ClearCLIJ clij,
                                     ClearCLBuffer src,
                                     ClearCLBuffer dst,
                                     Float factorX,
                                     Float factorY,
                                     Float factorZ)
    {
        return Kernels.downsample(clij, src, dst, factorX, factorY, factorZ);
    }

    public static boolean downsample(ClearCLIJ clij,
                                     ClearCLBuffer src,
                                     ClearCLBuffer dst,
                                     Float factorX,
                                     Float factorY)
    {
        return Kernels.downsample(clij, src, dst, factorX, factorY);
    }

    public static boolean downsampleSliceBySliceHalfMedian(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        return Kernels.downsampleSliceBySliceHalfMedian(clij, src, dst);
    }

    public static boolean erode(ClearCLIJ clij,
                                ClearCLBuffer src,
                                ClearCLBuffer dst)
    {
        return Kernels.erode(clij, src, dst);
    }

    public static boolean flip(ClearCLIJ clij,
                               ClearCLBuffer src,
                               ClearCLBuffer dst,
                               Boolean flipx,
                               Boolean flipy,
                               Boolean flipz)
    {
        return Kernels.flip(clij, src, dst, flipx, flipy, flipz);
    }

    public static boolean flip(ClearCLIJ clij,
                               ClearCLBuffer src,
                               ClearCLBuffer dst,
                               Boolean flipx,
                               Boolean flipy)
    {
        return Kernels.flip(clij, src, dst, flipx, flipy);
    }

    public static boolean invertBinary(ClearCLIJ clij,
                                       ClearCLBuffer src,
                                       ClearCLBuffer dst)
    {
        return Kernels.invertBinary(clij, src, dst);
    }

    public static boolean mask(ClearCLIJ clij,
                               ClearCLBuffer src,
                               ClearCLBuffer mask,
                               ClearCLBuffer dst)
    {
        return Kernels.mask(clij, src, mask, dst);
    }

    public static boolean maskStackWithPlane(ClearCLIJ clij,
                                             ClearCLBuffer src,
                                             ClearCLBuffer mask,
                                             ClearCLBuffer dst)
    {
        return Kernels.maskStackWithPlane(clij, src, mask, dst);
    }

    public static boolean maximum(ClearCLIJ clij,
                                  ClearCLBuffer src,
                                  ClearCLBuffer dst,
                                  Integer kernelSizeX,
                                  Integer kernelSizeY) {
        return Kernels.maximum(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public static boolean maximum(ClearCLIJ clij,
                                  ClearCLBuffer src,
                                  ClearCLBuffer dst,
                                  Integer kernelSizeX,
                                  Integer kernelSizeY,
                                  Integer kernelSizeZ) {
        return Kernels.maximum(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public static boolean maximumSliceBySlice(ClearCLIJ clij,
                                              ClearCLBuffer src,
                                              ClearCLBuffer dst,
                                              Integer kernelSizeX,
                                              Integer kernelSizeY) {
        return Kernels.maximumSliceBySlice(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public static boolean maxPixelwise(ClearCLIJ clij,
                                       ClearCLBuffer src,
                                       ClearCLBuffer src1,
                                       ClearCLBuffer dst)
    {
        return Kernels.maxPixelwise(clij, src, src1, dst);
    }

    public static boolean maxProjection(ClearCLIJ clij,
                                        ClearCLBuffer src,
                                        ClearCLBuffer dst_max)
    {
        return Kernels.maxProjection(clij, src, dst_max);
    }

    public static boolean maxProjection(ClearCLIJ clij,
                                        ClearCLBuffer src,
                                        ClearCLBuffer dst_max,
                                        Integer projectedDimensionX,
                                        Integer projectedDimensionY,
                                        Integer projectedDimension)
    {
        return Kernels.maxProjection(clij, src, dst_max, projectedDimensionX, projectedDimensionY, projectedDimension);
    }

    public static boolean mean(ClearCLIJ clij,
                               ClearCLBuffer src,
                               ClearCLBuffer dst,
                               Integer kernelSizeX,
                               Integer kernelSizeY) {
        return Kernels.mean(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public static boolean mean(ClearCLIJ clij,
                               ClearCLBuffer src,
                               ClearCLBuffer dst,
                               Integer kernelSizeX,
                               Integer kernelSizeY,
                               Integer kernelSizeZ) {
        return Kernels.mean(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public static boolean meanSliceBySlice(ClearCLIJ clij,
                                           ClearCLBuffer src,
                                           ClearCLBuffer dst,
                                           Integer kernelSizeX,
                                           Integer kernelSizeY) {
        return Kernels.meanSliceBySlice(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public static boolean median(ClearCLIJ clij,
                                 ClearCLBuffer src,
                                 ClearCLBuffer dst,
                                 Integer kernelSizeX,
                                 Integer kernelSizeY) {
        return Kernels.median(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public static boolean median(ClearCLIJ clij,
                                 ClearCLBuffer src,
                                 ClearCLBuffer dst,
                                 Integer kernelSizeX,
                                 Integer kernelSizeY,
                                 Integer kernelSizeZ) {
        return Kernels.median(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public static boolean medianSliceBySlice(ClearCLIJ clij,
                                             ClearCLBuffer src,
                                             ClearCLBuffer dst,
                                             Integer kernelSizeX,
                                             Integer kernelSizeY) {
        return Kernels.medianSliceBySlice(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public static boolean minimum(ClearCLIJ clij,
                                  ClearCLBuffer src,
                                  ClearCLBuffer dst,
                                  Integer kernelSizeX,
                                  Integer kernelSizeY) {
        return Kernels.minimum(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public static boolean minimum(ClearCLIJ clij,
                                  ClearCLBuffer src,
                                  ClearCLBuffer dst,
                                  Integer kernelSizeX,
                                  Integer kernelSizeY,
                                  Integer kernelSizeZ) {
        return Kernels.minimum(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public static boolean minimumSliceBySlice(ClearCLIJ clij,
                                              ClearCLBuffer src,
                                              ClearCLBuffer dst,
                                              Integer kernelSizeX,
                                              Integer kernelSizeY) {
        return Kernels.minimumSliceBySlice(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public static boolean multiplyPixelwise(ClearCLIJ clij,
                                            ClearCLBuffer src,
                                            ClearCLBuffer src1,
                                            ClearCLBuffer dst)
    {
        return Kernels.multiplyPixelwise(clij, src, src1, dst);
    }

    public static boolean multiplyScalar(ClearCLIJ clij,
                                         ClearCLBuffer src,
                                         ClearCLBuffer dst,
                                         Float scalar)
    {
        return Kernels.multiplyScalar(clij, src, dst, scalar);
    }

    public static boolean multiplyStackWithPlane(ClearCLIJ clij,
                                                 ClearCLBuffer input3d,
                                                 ClearCLBuffer input2d,
                                                 ClearCLBuffer output3d)
    {
        return Kernels.multiplyStackWithPlane(clij, input3d, input3d, output3d);
    }

    public static boolean power(ClearCLIJ clij,
                                ClearCLBuffer src,
                                ClearCLBuffer dst,
                                Float exponent) {
        return Kernels.power(clij, src, dst, exponent);
    }

    public static boolean resliceBottom(ClearCLIJ clij,
                                        ClearCLBuffer src,
                                        ClearCLBuffer dst){
        return Kernels.resliceBottom(clij, src, dst);
    }

    public static boolean resliceLeft(ClearCLIJ clij,
                                      ClearCLBuffer src,
                                      ClearCLBuffer dst){
        return Kernels.resliceLeft(clij, src, dst);
    }

    public static boolean resliceRight(ClearCLIJ clij,
                                       ClearCLBuffer src,
                                       ClearCLBuffer dst){
        return Kernels.resliceRight(clij, src, dst);
    }

    public static boolean resliceTop(ClearCLIJ clij,
                                     ClearCLBuffer src,
                                     ClearCLBuffer dst){
        return Kernels.resliceTop(clij, src, dst);
    }

    public static boolean set(ClearCLIJ clij,
                              ClearCLBuffer clImage,
                              Float value)
    {
        return Kernels.set(clij, clImage, value);
    }

    public static boolean threshold(ClearCLIJ clij,
                                    ClearCLBuffer src,
                                    ClearCLBuffer dst,
                                    Float threshold)
    {
        return Kernels.threshold(clij, src, dst, threshold);
    }
}
