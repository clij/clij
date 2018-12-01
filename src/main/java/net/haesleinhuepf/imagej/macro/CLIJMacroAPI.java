package net.haesleinhuepf.imagej.macro;

import clearcl.ClearCLBuffer;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;

/**
 * The CLIJMacroAPI contains all methods which can be called from ImageJ macro
 *
 * Author: @haesleinhuepf
 * 11 2018
 */
public class CLIJMacroAPI {

    public final static String parameter_doc_absolute = "Image source, Image destination";
    public static boolean absolute(ClearCLIJ clij,
                                   ClearCLBuffer source,
                                   ClearCLBuffer destination)
    {
        return Kernels.absolute(clij, source, destination);
    }

    public final static String parameter_doc_addPixelwise = "Image summand1, Image summand2, Image destination";
    public static boolean addPixelwise(ClearCLIJ clij,
                                       ClearCLBuffer summand1,
                                       ClearCLBuffer summand2,
                                       ClearCLBuffer destination)
    {
        return Kernels.addPixelwise(clij, summand1, summand2, destination);
    }

    public final static String parameter_doc_addScalar = "Image source, Image destination, Number scalar";
    public static boolean addScalar(ClearCLIJ clij,
                                    ClearCLBuffer source,
                                    ClearCLBuffer destination,
                                    Float scalar)
    {
        return Kernels.addScalar(clij, source, destination, scalar);
    }

    public final static String parameter_doc_addWeightedPixelwise = "Image summand1, Image summand2, Image destination, Number factor1, Number factor2";
    public static boolean addWeightedPixelwise(ClearCLIJ clij,
                                               ClearCLBuffer summand1,
                                               ClearCLBuffer summand2,
                                               ClearCLBuffer destination,
                                               Float factor1,
                                               Float factor2)
    {
        return Kernels.addWeightedPixelwise(clij, summand1, summand2, destination, factor1, factor2);
    }

    public final static String parameter_doc_argMaxProjection = "Image source, Image destination_max, Image destination_arg_max";
    public static boolean argMaxProjection(ClearCLIJ clij,
                                           ClearCLBuffer source,
                                           ClearCLBuffer destination_max,
                                           ClearCLBuffer destination_arg_max)
    {
        return Kernels.argMaxProjection(clij, source, destination_max, destination_arg_max);
    }

    public final static String parameter_doc_binaryAnd = "Image source1, Image source2, Image destination";
    public static boolean binaryAnd(ClearCLIJ clij,
                                    ClearCLBuffer source1,
                                    ClearCLBuffer source2,
                                    ClearCLBuffer destination)
    {
        return Kernels.binaryAnd(clij, source1, source2, destination);
    }

    public final static String parameter_doc_binaryNot = "Image source, Image destination";
    public static boolean binaryNot(ClearCLIJ clij,
                                    ClearCLBuffer source,
                                    ClearCLBuffer destination)
    {
        return Kernels.binaryNot(clij, source, destination);
    }

    public final static String parameter_doc_binaryOr = "Image source1, Image source2, Image destination";
    public static boolean binaryOr(ClearCLIJ clij,
                                   ClearCLBuffer source1,
                                   ClearCLBuffer source2,
                                   ClearCLBuffer destination)
    {
        return Kernels.binaryOr(clij, source1, source2, destination);
    }

    public final static String parameter_doc_blur2d = "Image source, Image destination, Number radiusX, Number radiusY, Number sigmaX, Number sigmaY";
    public static boolean blur2d(ClearCLIJ clij,
                               ClearCLBuffer source,
                               ClearCLBuffer destination,
                               Integer nX,
                               Integer nY,
                               Float sigmaX,
                               Float sigmaY)
    {
        return Kernels.blur(clij, source, destination, nX, nY, sigmaX, sigmaY);
    }

    public final static String parameter_doc_blur3d = "Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ, Number sigmaX, Number sigmaY, Number sigmaZ";
    public static boolean blur3d(ClearCLIJ clij,
                               ClearCLBuffer source,
                               ClearCLBuffer destination,
                               Integer nX,
                               Integer nY,
                               Integer nZ,
                               Float sigmaX,
                               Float sigmaY,
                               Float sigmaZ)
    {
        return Kernels.blur(clij, source, destination, nX, nY, nZ, sigmaX, sigmaY, sigmaZ);
    }

    public final static String parameter_doc_copy = "Image source, Image destination";
    public static boolean copy(ClearCLIJ clij,
                               ClearCLBuffer source,
                               ClearCLBuffer destination)
    {
        return Kernels.copy(clij, source, destination);
    }

    public final static String parameter_doc_copySlice = "Image source, Image destination, Number sliceIndex";
    public static boolean copySlice(ClearCLIJ clij,
                                    ClearCLBuffer source,
                                    ClearCLBuffer destination,
                                    Integer sliceIndex)
    {
        return Kernels.copySlice(clij, source, destination, sliceIndex);
    }

    public final static String parameter_doc_crop3d = "Image source, Image destination, Number startX, Number startY, Number startZ";
    public static boolean crop3d(ClearCLIJ clij,
                               ClearCLBuffer source,
                               ClearCLBuffer destination,
                               Integer startX,
                               Integer startY,
                               Integer startZ)
    {
        return Kernels.crop(clij, source, destination, startX, startY, startZ);
    }

    public final static String parameter_doc_crop2d = "Image source, Image destination, Number startX, Number startY";
    public static boolean crop2d(ClearCLIJ clij,
                               ClearCLBuffer source,
                               ClearCLBuffer destination,
                               Integer startX,
                               Integer startY)
    {
        return Kernels.crop(clij, source, destination, startX, startY);
    }

    public final static String parameter_doc_detectMaxima = "Image source, Image destination, Number radius";
    public static boolean detectMaxima(ClearCLIJ clij,
                                       ClearCLBuffer source,
                                       ClearCLBuffer destination,
                                       Integer radius)
    {
        return Kernels.detectMaxima(clij, source, destination, radius);
    }

    public final static String parameter_doc_detectMaximaSliceBySlice = "Image source, Image destination, Number radius";
    public static boolean detectMaximaSliceBySlice(ClearCLIJ clij,
                                                   ClearCLBuffer source,
                                                   ClearCLBuffer destination,
                                                   Integer radius)
    {
        return Kernels.detectMaximaSliceBySlice(clij, source, destination, radius);
    }

    public final static String parameter_doc_detectMinima = "Image source, Image destination, Number radius";
    public static boolean detectMinima(ClearCLIJ clij,
                                       ClearCLBuffer source,
                                       ClearCLBuffer destination,
                                       Integer radius)
    {
        return Kernels.detectMinima(clij, source, destination, radius);
    }

    public final static String parameter_doc_detectMinimaSliceBySlice = "Image source, Image destination, Number radius";
    public static boolean detectMinimaSliceBySlice(ClearCLIJ clij,
                                                   ClearCLBuffer source,
                                                   ClearCLBuffer destination,
                                                   Integer radius)
    {
        return Kernels.detectMinimaSliceBySlice(clij, source, destination, radius);
    }

    public final static String parameter_doc_dilate = "Image source, Image destination";
    public static boolean dilate(ClearCLIJ clij,
                                 ClearCLBuffer source,
                                 ClearCLBuffer destination)
    {
        return Kernels.dilate(clij, source, destination);
    }

    public final static String parameter_doc_dividePixelwise = "Image dividend, Image divisor, Image destination";
    public static boolean dividePixelwise(ClearCLIJ clij,
                                          ClearCLBuffer dividend,
                                          ClearCLBuffer divisor,
                                          ClearCLBuffer destination)
    {
        return Kernels.dividePixelwise(clij, dividend, divisor, destination);
    }

    public final static String parameter_doc_downsample3d = "Image source, Image destination, Number factorX, Number factorY, Number factorZ";
    public static boolean downsample3d(ClearCLIJ clij,
                                     ClearCLBuffer source,
                                     ClearCLBuffer destination,
                                     Float factorX,
                                     Float factorY,
                                     Float factorZ)
    {
        return Kernels.downsample(clij, source, destination, factorX, factorY, factorZ);
    }

    public final static String parameter_doc_downsample2d = "Image source, Image destination, Number factorX, Number factorY";
    public static boolean downsample2d(ClearCLIJ clij,
                                     ClearCLBuffer source,
                                     ClearCLBuffer destination,
                                     Float factorX,
                                     Float factorY)
    {
        return Kernels.downsample(clij, source, destination, factorX, factorY);
    }

    public final static String parameter_doc_downsampleSliceBySliceHalfMedian = "Image source, Image destination";
    public static boolean downsampleSliceBySliceHalfMedian(ClearCLIJ clij, ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.downsampleSliceBySliceHalfMedian(clij, source, destination);
    }

    public final static String parameter_doc_erode = "Image source, Image destination";
    public static boolean erode(ClearCLIJ clij,
                                ClearCLBuffer source,
                                ClearCLBuffer destination)
    {
        return Kernels.erode(clij, source, destination);
    }

    public final static String parameter_doc_flip3d = "Image source, Image destination, Boolean flipX, Boolean flipY, Boolean flipZ";
    public static boolean flip3d(ClearCLIJ clij,
                               ClearCLBuffer source,
                               ClearCLBuffer destination,
                               Boolean flipX,
                               Boolean flipY,
                               Boolean flipZ)
    {
        return Kernels.flip(clij, source, destination, flipX, flipY, flipZ);
    }

    public final static String parameter_doc_flip2d = "Image source, Image destination, Boolean flipX, Boolean flipY";
    public static boolean flip2d(ClearCLIJ clij,
                               ClearCLBuffer source,
                               ClearCLBuffer destination,
                               Boolean flipX,
                               Boolean flipY)
    {
        return Kernels.flip(clij, source, destination, flipX, flipY);
    }

    public final static String parameter_doc_invertBinary = "Image source, Image destination";
    public static boolean invertBinary(ClearCLIJ clij,
                                       ClearCLBuffer source,
                                       ClearCLBuffer destination)
    {
        return Kernels.invertBinary(clij, source, destination);
    }

    public final static String parameter_doc_mask = "Image source, Image mask, Image destination";
    public static boolean mask(ClearCLIJ clij,
                               ClearCLBuffer source,
                               ClearCLBuffer mask,
                               ClearCLBuffer destination)
    {
        return Kernels.mask(clij, source, mask, destination);
    }

    public final static String parameter_doc_maskStackWithPlane = "Image source3d, Image mask2d, Image destination3d";
    public static boolean maskStackWithPlane(ClearCLIJ clij,
                                             ClearCLBuffer source,
                                             ClearCLBuffer mask,
                                             ClearCLBuffer destination)
    {
        return Kernels.maskStackWithPlane(clij, source, mask, destination);
    }

    public final static String parameter_doc_maximum2d = "Image source, Image destination, Number radiusX, Number radiusY";
    public static boolean maximum2d(ClearCLIJ clij,
                                  ClearCLBuffer source,
                                  ClearCLBuffer destination,
                                  Integer radiusX,
                                  Integer radiusY) {
        return Kernels.maximum(clij, source, destination, radiusX, radiusY);
    }

    public final static String parameter_doc_maximum3d = "Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    public static boolean maximum3d(ClearCLIJ clij,
                                  ClearCLBuffer source,
                                  ClearCLBuffer destination,
                                  Integer radiusX,
                                  Integer radiusY,
                                  Integer radiusZ) {
        return Kernels.maximum(clij, source, destination, radiusX, radiusY, radiusZ);
    }

    public final static String parameter_doc_maximumSliceBySlice = "Image source, Image destination, Number radiusX, Number radiusY";
    public static boolean maximumSliceBySlice(ClearCLIJ clij,
                                              ClearCLBuffer source,
                                              ClearCLBuffer destination,
                                              Integer radiusX,
                                              Integer radiusY) {
        return Kernels.maximumSliceBySlice(clij, source, destination, radiusX, radiusY);
    }

    public final static String parameter_doc_maxPixelwise = "Image source, Image source2, Image destination";
    public static boolean maxPixelwise(ClearCLIJ clij,
                                       ClearCLBuffer source1,
                                       ClearCLBuffer source2,
                                       ClearCLBuffer destination)
    {
        return Kernels.maxPixelwise(clij, source1, source2, destination);
    }

    public final static String parameter_doc_maxProjection = "Image source, Image destination";
    public static boolean maxProjection(ClearCLIJ clij,
                                        ClearCLBuffer source,
                                        ClearCLBuffer destination)
    {
        return Kernels.maxProjection(clij, source, destination);
    }

    public final static String parameter_doc_maxProjectionDimSelect = "Image source, Image destination, Number projectedX, Number projectedY, Number projectedDimension";
    public static boolean maxProjectionDimSelect(ClearCLIJ clij,
                                        ClearCLBuffer source,
                                        ClearCLBuffer destination,
                                        Integer projectedX,
                                        Integer projectedY,
                                        Integer projectedDimension)
    {
        return Kernels.maxProjection(clij, source, destination, projectedX, projectedY, projectedDimension);
    }

    public final static String parameter_doc_mean2d = "Image source, Image destination, Number radiusX, Number radiusY";
    public static boolean mean2d(ClearCLIJ clij,
                               ClearCLBuffer source,
                               ClearCLBuffer destination,
                               Integer radiusX,
                               Integer radiusY) {
        return Kernels.mean(clij, source, destination, radiusX, radiusY);
    }

    public final static String parameter_doc_mean3d = "Image source, Image destination, Number radiusX, Number radiusY, Number radius Z";
    public static boolean mean3d(ClearCLIJ clij,
                               ClearCLBuffer source,
                               ClearCLBuffer destination,
                               Integer radiusX,
                               Integer radiusY,
                               Integer radiusZ) {
        return Kernels.mean(clij, source, destination, radiusX, radiusY, radiusZ);
    }

    public final static String parameter_doc_meanSliceBySlice = "Image source, Image destination, Number radiusX, Number radiusY";
    public static boolean meanSliceBySlice(ClearCLIJ clij,
                                           ClearCLBuffer source,
                                           ClearCLBuffer destination,
                                           Integer radiusX,
                                           Integer radiusY) {
        return Kernels.meanSliceBySlice(clij, source, destination, radiusX, radiusY);
    }

    public final static String parameter_doc_median2d = "Image source, Image destination, Number radiusX, Number radiusY";
    public static boolean median2d(ClearCLIJ clij,
                                 ClearCLBuffer source,
                                 ClearCLBuffer destination,
                                 Integer radiusX,
                                 Integer radiusY) {
        return Kernels.median(clij, source, destination, radiusX, radiusY);
    }

    public final static String parameter_doc_median3d = "Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    public static boolean median3d(ClearCLIJ clij,
                                 ClearCLBuffer source,
                                 ClearCLBuffer destination,
                                 Integer radiusX,
                                 Integer radiusY,
                                 Integer radiusZ) {
        return Kernels.median(clij, source, destination, radiusX, radiusY, radiusZ);
    }

    public final static String parameter_doc_medianSliceBySlice = "Image source, Image destination, Number radiusX, Number radiusY";
    public static boolean medianSliceBySlice(ClearCLIJ clij,
                                             ClearCLBuffer source,
                                             ClearCLBuffer destination,
                                             Integer radiusX,
                                             Integer radiusY) {
        return Kernels.medianSliceBySlice(clij, source, destination, radiusX, radiusY);
    }

    public final static String parameter_doc_minimum2d = "Image source, Image destination, Number radiusX, Number radiusY";
    public static boolean minimum2d(ClearCLIJ clij,
                                  ClearCLBuffer source,
                                  ClearCLBuffer destination,
                                  Integer radiusX,
                                  Integer radiusY) {
        return Kernels.minimum(clij, source, destination, radiusX, radiusY);
    }

    public final static String parameter_doc_minimum3d = "Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    public static boolean minimum3d(ClearCLIJ clij,
                                  ClearCLBuffer source,
                                  ClearCLBuffer destination,
                                  Integer radiusX,
                                  Integer radiusY,
                                  Integer radiusZ) {
        return Kernels.minimum(clij, source, destination, radiusX, radiusY, radiusZ);
    }

    public final static String parameter_doc_minimumSliceBySlice = "Image source, Image destination, Number radiusX, Number radiusY";
    public static boolean minimumSliceBySlice(ClearCLIJ clij,
                                              ClearCLBuffer source,
                                              ClearCLBuffer destination,
                                              Integer radiusX,
                                              Integer radiusY) {
        return Kernels.minimumSliceBySlice(clij, source, destination, radiusX, radiusY);
    }

    public final static String parameter_doc_multiplyPixelwise = "Image factor1, Image factor2, Image destination";
    public static boolean multiplyPixelwise(ClearCLIJ clij,
                                            ClearCLBuffer factor1,
                                            ClearCLBuffer factor2,
                                            ClearCLBuffer destination)
    {
        return Kernels.multiplyPixelwise(clij, factor1, factor2, destination);
    }

    public final static String parameter_doc_multiplyScalar = "Image source, Image destination";
    public static boolean multiplyScalar(ClearCLIJ clij,
                                         ClearCLBuffer source,
                                         ClearCLBuffer destination,
                                         Float scalar)
    {
        return Kernels.multiplyScalar(clij, source, destination, scalar);
    }

    public final static String parameter_doc_multiplyStackWithPlane = "Image source3d, Image source2d, Image destination3d";
    public static boolean multiplyStackWithPlane(ClearCLIJ clij,
                                                 ClearCLBuffer source3d,
                                                 ClearCLBuffer source2d,
                                                 ClearCLBuffer destination3d)
    {
        return Kernels.multiplyStackWithPlane(clij, source3d, source2d, destination3d);
    }

    public final static String parameter_doc_power = "Image source, Image destination, Number exponent";
    public static boolean power(ClearCLIJ clij,
                                ClearCLBuffer source,
                                ClearCLBuffer destination,
                                Float exponent) {
        return Kernels.power(clij, source, destination, exponent);
    }

    public final static String parameter_doc_resliceBottom = "Image source, Image destination";
    public static boolean resliceBottom(ClearCLIJ clij,
                                        ClearCLBuffer source,
                                        ClearCLBuffer destination){
        return Kernels.resliceBottom(clij, source, destination);
    }

    public final static String parameter_doc_resliceLeft = "Image source, Image destination";
    public static boolean resliceLeft(ClearCLIJ clij,
                                      ClearCLBuffer source,
                                      ClearCLBuffer destination){
        return Kernels.resliceLeft(clij, source, destination);
    }

    public final static String parameter_doc_resliceRight = "Image source, Image destination";
    public static boolean resliceRight(ClearCLIJ clij,
                                       ClearCLBuffer source,
                                       ClearCLBuffer destination){
        return Kernels.resliceRight(clij, source, destination);
    }

    public final static String parameter_doc_resliceTop = "Image source, Image destination";
    public static boolean resliceTop(ClearCLIJ clij,
                                     ClearCLBuffer source,
                                     ClearCLBuffer destination){
        return Kernels.resliceTop(clij, source, destination);
    }

    public final static String parameter_doc_set = "Image image, Number value";
    public static boolean set(ClearCLIJ clij,
                              ClearCLBuffer image,
                              Float value)
    {
        return Kernels.set(clij, image, value);
    }

    public final static String parameter_doc_threshold = "Image source, Image destination, Number threshold";
    public static boolean threshold(ClearCLIJ clij,
                                    ClearCLBuffer source,
                                    ClearCLBuffer destination,
                                    Float threshold)
    {
        return Kernels.threshold(clij, source, destination, threshold);
    }
}
