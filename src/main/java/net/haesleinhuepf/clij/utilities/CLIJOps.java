package net.haesleinhuepf.clij.utilities;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.utilities.CLKernelExecutor;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import java.nio.FloatBuffer;
import java.util.HashMap;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;
import static net.haesleinhuepf.clij.utilities.CLIJUtilities.sigmaToKernelSize;
// this is generated code. See src/test/java/net/haesleinhuepf/clij/codegenerator for details
public class CLIJOps {
   private CLIJ clij;
   public CLIJOps(CLIJ clij) {
       this.clij = clij;
   }
    public boolean absolute( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.absolute(clij, src, dst);
    }

    public boolean absolute( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.absolute(clij, src, dst);
    }

    public boolean addImages( ClearCLImage src,  ClearCLImage src1,  ClearCLImage dst ) {
        return Kernels.addImages(clij, src, src1, dst);
    }

    public boolean addImages( ClearCLBuffer src,  ClearCLBuffer src1,  ClearCLBuffer dst ) {
        return Kernels.addImages(clij, src, src1, dst);
    }

    public boolean addImageAndScalar( ClearCLImage src,  ClearCLImage dst,  Float scalar ) {
        return Kernels.addImageAndScalar(clij, src, dst, scalar);
    }

    public boolean addImageAndScalar( ClearCLBuffer src,  ClearCLBuffer dst,  Float scalar ) {
        return Kernels.addImageAndScalar(clij, src, dst, scalar);
    }

    public boolean addImagesWeighted( ClearCLImage src,  ClearCLImage src1,  ClearCLImage dst,  Float factor,  Float factor1 ) {
        return Kernels.addImagesWeighted(clij, src, src1, dst, factor, factor1);
    }

    public boolean addImagesWeighted( ClearCLBuffer src,  ClearCLBuffer src1,  ClearCLBuffer dst,  Float factor,  Float factor1 ) {
        return Kernels.addImagesWeighted(clij, src, src1, dst, factor, factor1);
    }

    public boolean argMaximumZProjection( ClearCLImage src,  ClearCLImage dst_max,  ClearCLImage dst_arg ) {
        return Kernels.argMaximumZProjection(clij, src, dst_max, dst_arg);
    }

    public boolean argMaximumZProjection( ClearCLBuffer src,  ClearCLBuffer dst_max,  ClearCLBuffer dst_arg ) {
        return Kernels.argMaximumZProjection(clij, src, dst_max, dst_arg);
    }

    public boolean binaryAnd( ClearCLImage src1,  ClearCLImage src2,  ClearCLImage dst ) {
        return Kernels.binaryAnd(clij, src1, src2, dst);
    }

    public boolean binaryAnd( ClearCLBuffer src1,  ClearCLBuffer src2,  ClearCLBuffer dst ) {
        return Kernels.binaryAnd(clij, src1, src2, dst);
    }

    public boolean binaryXOr( ClearCLImage src1,  ClearCLImage src2,  ClearCLImage dst ) {
        return Kernels.binaryXOr(clij, src1, src2, dst);
    }

    public boolean binaryXOr( ClearCLBuffer src1,  ClearCLBuffer src2,  ClearCLBuffer dst ) {
        return Kernels.binaryXOr(clij, src1, src2, dst);
    }

    public boolean binaryNot( ClearCLImage src1,  ClearCLImage dst ) {
        return Kernels.binaryNot(clij, src1, dst);
    }

    public boolean binaryNot( ClearCLBuffer src1,  ClearCLBuffer dst ) {
        return Kernels.binaryNot(clij, src1, dst);
    }

    public boolean binaryOr( ClearCLImage src1,  ClearCLImage src2,  ClearCLImage dst ) {
        return Kernels.binaryOr(clij, src1, src2, dst);
    }

    public boolean binaryOr( ClearCLBuffer src1,  ClearCLBuffer src2,  ClearCLBuffer dst ) {
        return Kernels.binaryOr(clij, src1, src2, dst);
    }

    public boolean blur( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY,  Float sigmaX,  Float sigmaY ) {
        return Kernels.blur(clij, src, dst, kernelSizeX, kernelSizeY, sigmaX, sigmaY);
    }

    public boolean blur( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY,  Float sigmaX,  Float sigmaY ) {
        return Kernels.blur(clij, src, dst, kernelSizeX, kernelSizeY, sigmaX, sigmaY);
    }

    public boolean blur( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY,  Integer kernelSizeZ,  Float sigmaX,  Float sigmaY,  Float sigmaZ ) {
        return Kernels.blur(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ, sigmaX, sigmaY, sigmaZ);
    }

    public boolean blur( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY,  Integer kernelSizeZ,  Float sigmaX,  Float sigmaY,  Float sigmaZ ) {
        return Kernels.blur(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ, sigmaX, sigmaY, sigmaZ);
    }

    public boolean blurIJ( ClearCLImage src,  ClearCLImage dst,  Float sigma ) {
        return Kernels.blurIJ(clij, src, dst, sigma);
    }

    public boolean blurIJ( ClearCLBuffer src,  ClearCLBuffer dst,  Float sigma ) {
        return Kernels.blurIJ(clij, src, dst, sigma);
    }

    public boolean blurFast( ClearCLImage src,  ClearCLImage dst,  float blurSigmaX,  float blurSigmaY,  float blurSigmaZ ) {
        return Kernels.blurFast(clij, src, dst, blurSigmaX, blurSigmaY, blurSigmaZ);
    }

    public boolean blurFast( ClearCLImage src,  ClearCLBuffer dst,  float blurSigmaX,  float blurSigmaY,  float blurSigmaZ ) {
        return Kernels.blurFast(clij, src, dst, blurSigmaX, blurSigmaY, blurSigmaZ);
    }

    public boolean blurFast( ClearCLBuffer src,  ClearCLBuffer dst,  float blurSigmaX,  float blurSigmaY,  float blurSigmaZ ) {
        return Kernels.blurFast(clij, src, dst, blurSigmaX, blurSigmaY, blurSigmaZ);
    }

    public boolean blurSliceBySlice( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY,  Float sigmaX,  Float sigmaY ) {
        return Kernels.blurSliceBySlice(clij, src, dst, kernelSizeX, kernelSizeY, sigmaX, sigmaY);
    }

    public boolean blurSliceBySlice( ClearCLBuffer src,  ClearCLBuffer dst,  int kernelSizeX,  int kernelSizeY,  float sigmaX,  float sigmaY ) {
        return Kernels.blurSliceBySlice(clij, src, dst, kernelSizeX, kernelSizeY, sigmaX, sigmaY);
    }

    public boolean copy( ClearCLImage src,  ClearCLBuffer dst ) {
        return Kernels.copy(clij, src, dst);
    }

    public boolean copy( ClearCLBuffer src,  ClearCLImage dst ) {
        return Kernels.copy(clij, src, dst);
    }

    public boolean copy( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.copy(clij, src, dst);
    }

    public boolean copy( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.copy(clij, src, dst);
    }

    public boolean copySlice( ClearCLImage src,  ClearCLImage dst,  Integer planeIndex ) {
        return Kernels.copySlice(clij, src, dst, planeIndex);
    }

    public boolean copySlice( ClearCLBuffer src,  ClearCLBuffer dst,  Integer planeIndex ) {
        return Kernels.copySlice(clij, src, dst, planeIndex);
    }

    public boolean crop( ClearCLImage src,  ClearCLImage dst,  Integer startX,  Integer startY,  Integer startZ ) {
        return Kernels.crop(clij, src, dst, startX, startY, startZ);
    }

    public boolean crop( ClearCLImage src,  ClearCLImage dst,  Integer startX,  Integer startY ) {
        return Kernels.crop(clij, src, dst, startX, startY);
    }

    public boolean crop( ClearCLBuffer src,  ClearCLBuffer dst,  Integer startX,  Integer startY,  Integer startZ ) {
        return Kernels.crop(clij, src, dst, startX, startY, startZ);
    }

    public boolean crop( ClearCLBuffer src,  ClearCLBuffer dst,  Integer startX,  Integer startY ) {
        return Kernels.crop(clij, src, dst, startX, startY);
    }

    public boolean detectMaximaBox( ClearCLImage src,  ClearCLImage dst,  Integer radius ) {
        return Kernels.detectMaximaBox(clij, src, dst, radius);
    }

    public boolean detectMaximaBox( ClearCLBuffer src,  ClearCLBuffer dst,  Integer radius ) {
        return Kernels.detectMaximaBox(clij, src, dst, radius);
    }

    public boolean detectMaximaSliceBySliceBox( ClearCLImage src,  ClearCLImage dst,  Integer radius ) {
        return Kernels.detectMaximaSliceBySliceBox(clij, src, dst, radius);
    }

    public boolean detectMaximaSliceBySliceBox( ClearCLBuffer src,  ClearCLBuffer dst,  Integer radius ) {
        return Kernels.detectMaximaSliceBySliceBox(clij, src, dst, radius);
    }

    public boolean detectMinimaBox( ClearCLImage src,  ClearCLImage dst,  Integer radius ) {
        return Kernels.detectMinimaBox(clij, src, dst, radius);
    }

    public boolean detectMinimaBox( ClearCLBuffer src,  ClearCLBuffer dst,  Integer radius ) {
        return Kernels.detectMinimaBox(clij, src, dst, radius);
    }

    public boolean detectMinimaSliceBySliceBox( ClearCLImage src,  ClearCLImage dst,  Integer radius ) {
        return Kernels.detectMinimaSliceBySliceBox(clij, src, dst, radius);
    }

    public boolean detectMinimaSliceBySliceBox( ClearCLBuffer src,  ClearCLBuffer dst,  Integer radius ) {
        return Kernels.detectMinimaSliceBySliceBox(clij, src, dst, radius);
    }

    public boolean detectOptima( ClearCLImage src,  ClearCLImage dst,  Integer radius,  Boolean detectMaxima ) {
        return Kernels.detectOptima(clij, src, dst, radius, detectMaxima);
    }

    public boolean detectOptima( ClearCLBuffer src,  ClearCLBuffer dst,  Integer radius,  Boolean detectMaxima ) {
        return Kernels.detectOptima(clij, src, dst, radius, detectMaxima);
    }

    public boolean detectOptimaSliceBySlice( ClearCLImage src,  ClearCLImage dst,  Integer radius,  Boolean detectMaxima ) {
        return Kernels.detectOptimaSliceBySlice(clij, src, dst, radius, detectMaxima);
    }

    public boolean detectOptimaSliceBySlice( ClearCLBuffer src,  ClearCLBuffer dst,  Integer radius,  Boolean detectMaxima ) {
        return Kernels.detectOptimaSliceBySlice(clij, src, dst, radius, detectMaxima);
    }

    public boolean differenceOfGaussian( ClearCLImage src,  ClearCLImage dst,  Integer radius,  Float sigmaMinuend,  Float sigmaSubtrahend ) {
        return Kernels.differenceOfGaussian(clij, src, dst, radius, sigmaMinuend, sigmaSubtrahend);
    }

    public boolean differenceOfGaussianSliceBySlice( ClearCLImage src,  ClearCLImage dst,  Integer radius,  Float sigmaMinuend,  Float sigmaSubtrahend ) {
        return Kernels.differenceOfGaussianSliceBySlice(clij, src, dst, radius, sigmaMinuend, sigmaSubtrahend);
    }

    public boolean dilateBox( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.dilateBox(clij, src, dst);
    }

    public boolean dilateBox( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.dilateBox(clij, src, dst);
    }

    public boolean dilateSphere( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.dilateSphere(clij, src, dst);
    }

    public boolean dilateSphere( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.dilateSphere(clij, src, dst);
    }

    public boolean divideImages( ClearCLImage src,  ClearCLImage src1,  ClearCLImage dst ) {
        return Kernels.divideImages(clij, src, src1, dst);
    }

    public boolean divideImages( ClearCLBuffer src,  ClearCLBuffer src1,  ClearCLBuffer dst ) {
        return Kernels.divideImages(clij, src, src1, dst);
    }

    public boolean downsample( ClearCLImage src,  ClearCLImage dst,  Float factorX,  Float factorY,  Float factorZ ) {
        return Kernels.downsample(clij, src, dst, factorX, factorY, factorZ);
    }

    public boolean downsample( ClearCLBuffer src,  ClearCLBuffer dst,  Float factorX,  Float factorY,  Float factorZ ) {
        return Kernels.downsample(clij, src, dst, factorX, factorY, factorZ);
    }

    public boolean downsample( ClearCLImage src,  ClearCLImage dst,  Float factorX,  Float factorY ) {
        return Kernels.downsample(clij, src, dst, factorX, factorY);
    }

    public boolean downsample( ClearCLBuffer src,  ClearCLBuffer dst,  Float factorX,  Float factorY ) {
        return Kernels.downsample(clij, src, dst, factorX, factorY);
    }

    public boolean downsampleSliceBySliceHalfMedian( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.downsampleSliceBySliceHalfMedian(clij, src, dst);
    }

    public boolean downsampleSliceBySliceHalfMedian( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.downsampleSliceBySliceHalfMedian(clij, src, dst);
    }

    public boolean erodeSphere( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.erodeSphere(clij, src, dst);
    }

    public boolean erodeSphere( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.erodeSphere(clij, src, dst);
    }

    public boolean erodeBox( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.erodeBox(clij, src, dst);
    }

    public boolean erodeBox( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.erodeBox(clij, src, dst);
    }

    public boolean flip( ClearCLImage src,  ClearCLImage dst,  Boolean flipx,  Boolean flipy,  Boolean flipz ) {
        return Kernels.flip(clij, src, dst, flipx, flipy, flipz);
    }

    public boolean flip( ClearCLImage src,  ClearCLImage dst,  Boolean flipx,  Boolean flipy ) {
        return Kernels.flip(clij, src, dst, flipx, flipy);
    }

    public boolean flip( ClearCLBuffer src,  ClearCLBuffer dst,  Boolean flipx,  Boolean flipy,  Boolean flipz ) {
        return Kernels.flip(clij, src, dst, flipx, flipy, flipz);
    }

    public boolean flip( ClearCLBuffer src,  ClearCLBuffer dst,  Boolean flipx,  Boolean flipy ) {
        return Kernels.flip(clij, src, dst, flipx, flipy);
    }

    public boolean invert( ClearCLImage input3d,  ClearCLImage output3d ) {
        return Kernels.invert(clij, input3d, output3d);
    }

    public boolean invert( ClearCLBuffer input3d,  ClearCLBuffer output3d ) {
        return Kernels.invert(clij, input3d, output3d);
    }

    public boolean localThreshold( ClearCLImage src,  ClearCLImage dst,  ClearCLImage threshold ) {
        return Kernels.localThreshold(clij, src, dst, threshold);
    }

    public boolean localThreshold( ClearCLBuffer src,  ClearCLBuffer dst,  ClearCLBuffer threshold ) {
        return Kernels.localThreshold(clij, src, dst, threshold);
    }

    public boolean mask( ClearCLImage src,  ClearCLImage mask,  ClearCLImage dst ) {
        return Kernels.mask(clij, src, mask, dst);
    }

    public boolean mask( ClearCLBuffer src,  ClearCLBuffer mask,  ClearCLBuffer dst ) {
        return Kernels.mask(clij, src, mask, dst);
    }

    public boolean maskStackWithPlane( ClearCLImage src,  ClearCLImage mask,  ClearCLImage dst ) {
        return Kernels.maskStackWithPlane(clij, src, mask, dst);
    }

    public boolean maskStackWithPlane( ClearCLBuffer src,  ClearCLBuffer mask,  ClearCLBuffer dst ) {
        return Kernels.maskStackWithPlane(clij, src, mask, dst);
    }

    public boolean maximumSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.maximumSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean maximumSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.maximumSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean maximumSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY,  Integer kernelSizeZ ) {
        return Kernels.maximumSphere(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public boolean maximumSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY,  Integer kernelSizeZ ) {
        return Kernels.maximumSphere(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public boolean maximumIJ( ClearCLImage src,  ClearCLImage dst,  Integer radius ) {
        return Kernels.maximumIJ(clij, src, dst, radius);
    }

    public boolean maximumIJ( ClearCLBuffer src,  ClearCLBuffer dst,  Integer radius ) {
        return Kernels.maximumIJ(clij, src, dst, radius);
    }

    public boolean maximumSliceBySliceSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.maximumSliceBySliceSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean maximumBox( ClearCLImage src,  ClearCLImage dst,  int radiusX,  int radiusY,  int radiusZ ) {
        return Kernels.maximumBox(clij, src, dst, radiusX, radiusY, radiusZ);
    }

    public boolean maximumBox( ClearCLBuffer src,  ClearCLBuffer dst,  int radiusX,  int radiusY,  int radiusZ ) {
        return Kernels.maximumBox(clij, src, dst, radiusX, radiusY, radiusZ);
    }

    public boolean maximumSliceBySliceSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.maximumSliceBySliceSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean maximumImages( ClearCLImage src,  ClearCLImage src1,  ClearCLImage dst ) {
        return Kernels.maximumImages(clij, src, src1, dst);
    }

    public boolean maximumImages( ClearCLBuffer src,  ClearCLBuffer src1,  ClearCLBuffer dst ) {
        return Kernels.maximumImages(clij, src, src1, dst);
    }

    public boolean maximumImageAndScalar( ClearCLImage src,  ClearCLImage dst,  Float valueB ) {
        return Kernels.maximumImageAndScalar(clij, src, dst, valueB);
    }

    public boolean maximumImageAndScalar( ClearCLBuffer src,  ClearCLBuffer dst,  Float valueB ) {
        return Kernels.maximumImageAndScalar(clij, src, dst, valueB);
    }

    public boolean minimumImages( ClearCLImage src,  ClearCLImage src1,  ClearCLImage dst ) {
        return Kernels.minimumImages(clij, src, src1, dst);
    }

    public boolean minimumImages( ClearCLBuffer src,  ClearCLBuffer src1,  ClearCLBuffer dst ) {
        return Kernels.minimumImages(clij, src, src1, dst);
    }

    public boolean minimumImageAndScalar( ClearCLImage src,  ClearCLImage dst,  Float valueB ) {
        return Kernels.minimumImageAndScalar(clij, src, dst, valueB);
    }

    public boolean minimumImageAndScalar( ClearCLBuffer src,  ClearCLBuffer dst,  Float valueB ) {
        return Kernels.minimumImageAndScalar(clij, src, dst, valueB);
    }

    public boolean maximumZProjection( ClearCLImage src,  ClearCLImage dst_max ) {
        return Kernels.maximumZProjection(clij, src, dst_max);
    }

    public boolean maximumZProjection( ClearCLBuffer src,  ClearCLBuffer dst_max ) {
        return Kernels.maximumZProjection(clij, src, dst_max);
    }

    public boolean minimumZProjection( ClearCLImage src,  ClearCLImage dst_min ) {
        return Kernels.minimumZProjection(clij, src, dst_min);
    }

    public boolean minimumZProjection( ClearCLBuffer src,  ClearCLBuffer dst_min ) {
        return Kernels.minimumZProjection(clij, src, dst_min);
    }

    public boolean meanZProjection( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.meanZProjection(clij, src, dst);
    }

    public boolean meanZProjection( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.meanZProjection(clij, src, dst);
    }

    public boolean maximumXYZProjection( ClearCLImage src,  ClearCLImage dst_max,  Integer projectedDimensionX,  Integer projectedDimensionY,  Integer projectedDimension ) {
        return Kernels.maximumXYZProjection(clij, src, dst_max, projectedDimensionX, projectedDimensionY, projectedDimension);
    }

    public boolean maximumXYZProjection( ClearCLBuffer src,  ClearCLBuffer dst_max,  Integer projectedDimensionX,  Integer projectedDimensionY,  Integer projectedDimension ) {
        return Kernels.maximumXYZProjection(clij, src, dst_max, projectedDimensionX, projectedDimensionY, projectedDimension);
    }

    public boolean meanSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.meanSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean meanSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.meanSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean meanIJ( ClearCLImage src,  ClearCLImage dst,  Integer radius ) {
        return Kernels.meanIJ(clij, src, dst, radius);
    }

    public boolean meanIJ( ClearCLBuffer src,  ClearCLBuffer dst,  Integer radius ) {
        return Kernels.meanIJ(clij, src, dst, radius);
    }

    public boolean meanSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY,  Integer kernelSizeZ ) {
        return Kernels.meanSphere(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public boolean meanSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY,  Integer kernelSizeZ ) {
        return Kernels.meanSphere(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public boolean meanBox( ClearCLImage src,  ClearCLImage dst,  int radiusX,  int radiusY,  int radiusZ ) {
        return Kernels.meanBox(clij, src, dst, radiusX, radiusY, radiusZ);
    }

    public boolean meanBox( ClearCLBuffer src,  ClearCLBuffer dst,  int radiusX,  int radiusY,  int radiusZ ) {
        return Kernels.meanBox(clij, src, dst, radiusX, radiusY, radiusZ);
    }

    public boolean meanSliceBySliceSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.meanSliceBySliceSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean meanSliceBySliceSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.meanSliceBySliceSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean medianSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.medianSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean medianSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.medianSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean medianSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY,  Integer kernelSizeZ ) {
        return Kernels.medianSphere(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public boolean medianSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY,  Integer kernelSizeZ ) {
        return Kernels.medianSphere(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public boolean medianSliceBySliceSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.medianSliceBySliceSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean medianSliceBySliceSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.medianSliceBySliceSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean minimumSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.minimumSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean minimumSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.minimumSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean minimumSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY,  Integer kernelSizeZ ) {
        return Kernels.minimumSphere(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public boolean minimumSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY,  Integer kernelSizeZ ) {
        return Kernels.minimumSphere(clij, src, dst, kernelSizeX, kernelSizeY, kernelSizeZ);
    }

    public boolean minimumIJ( ClearCLImage src,  ClearCLImage dst,  Integer radius ) {
        return Kernels.minimumIJ(clij, src, dst, radius);
    }

    public boolean minimumIJ( ClearCLBuffer src,  ClearCLBuffer dst,  Integer radius ) {
        return Kernels.minimumIJ(clij, src, dst, radius);
    }

    public boolean minimumBox( ClearCLImage src,  ClearCLImage dst,  int radiusX,  int radiusY,  int radiusZ ) {
        return Kernels.minimumBox(clij, src, dst, radiusX, radiusY, radiusZ);
    }

    public boolean minimumBox( ClearCLBuffer src,  ClearCLBuffer dst,  int radiusX,  int radiusY,  int radiusZ ) {
        return Kernels.minimumBox(clij, src, dst, radiusX, radiusY, radiusZ);
    }

    public boolean minimumSliceBySliceSphere( ClearCLImage src,  ClearCLImage dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.minimumSliceBySliceSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean minimumSliceBySliceSphere( ClearCLBuffer src,  ClearCLBuffer dst,  Integer kernelSizeX,  Integer kernelSizeY ) {
        return Kernels.minimumSliceBySliceSphere(clij, src, dst, kernelSizeX, kernelSizeY);
    }

    public boolean multiplyImages( ClearCLImage src,  ClearCLImage src1,  ClearCLImage dst ) {
        return Kernels.multiplyImages(clij, src, src1, dst);
    }

    public boolean multiplyImages( ClearCLBuffer src,  ClearCLBuffer src1,  ClearCLBuffer dst ) {
        return Kernels.multiplyImages(clij, src, src1, dst);
    }

    public boolean multiplyImageAndScalar( ClearCLImage src,  ClearCLImage dst,  Float scalar ) {
        return Kernels.multiplyImageAndScalar(clij, src, dst, scalar);
    }

    public boolean multiplyImageAndScalar( ClearCLBuffer src,  ClearCLBuffer dst,  Float scalar ) {
        return Kernels.multiplyImageAndScalar(clij, src, dst, scalar);
    }

    public boolean multiplySliceBySliceWithScalars( ClearCLImage src,  ClearCLImage dst,  float[] scalars ) {
        return Kernels.multiplySliceBySliceWithScalars(clij, src, dst, scalars);
    }

    public boolean multiplySliceBySliceWithScalars( ClearCLBuffer src,  ClearCLBuffer dst,  float[] scalars ) {
        return Kernels.multiplySliceBySliceWithScalars(clij, src, dst, scalars);
    }

    public boolean multiplyStackWithPlane( ClearCLImage input3d,  ClearCLImage input2d,  ClearCLImage output3d ) {
        return Kernels.multiplyStackWithPlane(clij, input3d, input2d, output3d);
    }

    public boolean multiplyStackWithPlane( ClearCLBuffer input3d,  ClearCLBuffer input2d,  ClearCLBuffer output3d ) {
        return Kernels.multiplyStackWithPlane(clij, input3d, input2d, output3d);
    }

    public boolean power( ClearCLImage src,  ClearCLImage dst,  Float exponent ) {
        return Kernels.power(clij, src, dst, exponent);
    }

    public boolean power( ClearCLBuffer src,  ClearCLBuffer dst,  Float exponent ) {
        return Kernels.power(clij, src, dst, exponent);
    }

    public boolean radialProjection( ClearCLImage src,  ClearCLImage dst,  Float deltaAngle ) {
        return Kernels.radialProjection(clij, src, dst, deltaAngle);
    }

    public boolean radialProjection( ClearCLBuffer src,  ClearCLBuffer dst,  Float deltaAngle ) {
        return Kernels.radialProjection(clij, src, dst, deltaAngle);
    }

    public boolean resliceBottom( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.resliceBottom(clij, src, dst);
    }

    public boolean resliceBottom( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.resliceBottom(clij, src, dst);
    }

    public boolean resliceLeft( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.resliceLeft(clij, src, dst);
    }

    public boolean resliceLeft( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.resliceLeft(clij, src, dst);
    }

    public boolean resliceRight( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.resliceRight(clij, src, dst);
    }

    public boolean resliceRight( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.resliceRight(clij, src, dst);
    }

    public boolean resliceTop( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.resliceTop(clij, src, dst);
    }

    public boolean resliceTop( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.resliceTop(clij, src, dst);
    }

    public boolean rotateLeft( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.rotateLeft(clij, src, dst);
    }

    public boolean rotateLeft( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.rotateLeft(clij, src, dst);
    }

    public boolean rotateRight( ClearCLBuffer src,  ClearCLBuffer dst ) {
        return Kernels.rotateRight(clij, src, dst);
    }

    public boolean rotateRight( ClearCLImage src,  ClearCLImage dst ) {
        return Kernels.rotateRight(clij, src, dst);
    }

    public boolean set( ClearCLImage clImage,  Float value ) {
        return Kernels.set(clij, clImage, value);
    }

    public boolean set( ClearCLBuffer clImage,  Float value ) {
        return Kernels.set(clij, clImage, value);
    }

    public boolean splitStack( ClearCLImage clImageIn,  ClearCLImage... clImagesOut ) {
        return Kernels.splitStack(clij, clImageIn, clImagesOut);
    }

    public boolean splitStack( ClearCLBuffer clImageIn,  ClearCLBuffer... clImagesOut ) {
        return Kernels.splitStack(clij, clImageIn, clImagesOut);
    }

    public boolean subtract( ClearCLImage source1,  ClearCLImage source2,  ClearCLImage destination ) {
        return Kernels.subtract(clij, source1, source2, destination);
    }

    public boolean subtract( ClearCLBuffer source1,  ClearCLBuffer source2,  ClearCLBuffer destination ) {
        return Kernels.subtract(clij, source1, source2, destination);
    }

    public double maximumOfAllPixels( ClearCLImage clImage ) {
        return Kernels.maximumOfAllPixels(clij, clImage);
    }

    public double maximumOfAllPixels( ClearCLBuffer clImage ) {
        return Kernels.maximumOfAllPixels(clij, clImage);
    }

    public double minimumOfAllPixels( ClearCLImage clImage ) {
        return Kernels.minimumOfAllPixels(clij, clImage);
    }

    public double minimumOfAllPixels( ClearCLBuffer clImage ) {
        return Kernels.minimumOfAllPixels(clij, clImage);
    }

    public double sumPixels( ClearCLImage clImage ) {
        return Kernels.sumPixels(clij, clImage);
    }

    public double sumPixels( ClearCLBuffer clImage ) {
        return Kernels.sumPixels(clij, clImage);
    }

    public double[] sumPixelsSliceBySlice( ClearCLImage input ) {
        return Kernels.sumPixelsSliceBySlice(clij, input);
    }

    public double[] sumPixelsSliceBySlice( ClearCLBuffer input ) {
        return Kernels.sumPixelsSliceBySlice(clij, input);
    }

    public boolean sumZProjection( ClearCLImage clImage,  ClearCLImage clReducedImage ) {
        return Kernels.sumZProjection(clij, clImage, clReducedImage);
    }

    public boolean sumZProjection( ClearCLBuffer clImage,  ClearCLBuffer clReducedImage ) {
        return Kernels.sumZProjection(clij, clImage, clReducedImage);
    }

    public boolean tenengradWeightsSliceBySlice( ClearCLImage clImageOut,  ClearCLImage clImageIn ) {
        return Kernels.tenengradWeightsSliceBySlice(clij, clImageOut, clImageIn);
    }

    public boolean tenengradFusion( ClearCLImage clImageOut,  float[] blurSigmas,  ClearCLImage... clImagesIn ) {
        return Kernels.tenengradFusion(clij, clImageOut, blurSigmas, clImagesIn);
    }

    public boolean tenengradFusion( ClearCLImage clImageOut,  float[] blurSigmas,  float exponent,  ClearCLImage... clImagesIn ) {
        return Kernels.tenengradFusion(clij, clImageOut, blurSigmas, exponent, clImagesIn);
    }

    public boolean threshold( ClearCLImage src,  ClearCLImage dst,  Float threshold ) {
        return Kernels.threshold(clij, src, dst, threshold);
    }

    public boolean threshold( ClearCLBuffer src,  ClearCLBuffer dst,  Float threshold ) {
        return Kernels.threshold(clij, src, dst, threshold);
    }

}
