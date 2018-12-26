package net.haesleinhuepf.imagej.kernels;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.utilities.CLKernelExecutor;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import java.nio.FloatBuffer;
import java.util.HashMap;


/**
 * This class contains convenience access functions for OpenCL based
 * image processing.
 * <p>
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * March 2018
 */
public class Kernels {
    public static boolean absolute(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }

        return clij.execute(Kernels.class,"math.cl", "absolute_" + src.getDimension() + "d", parameters);
    }

    public static boolean absolute(ClearCLIJ clij,
                                   ClearCLBuffer src,
                                   ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "absolute_" + src.getDimension() + "d", parameters);
    }


    public static boolean addPixelwise(ClearCLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addPixelwise)");
        }
        return clij.execute(Kernels.class, "math.cl", "addPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean addPixelwise(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addPixelwise)");
        }
        return clij.execute(Kernels.class, "math.cl", "addPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean addScalar(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Float scalar) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "addScalar_" + src.getDimension() + "d", parameters);
    }


    public static boolean addScalar(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float scalar) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "addScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean addWeightedPixelwise(ClearCLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst, Float factor, Float factor1) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("factor", factor);
        parameters.put("factor1", factor1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "addWeightedPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean addWeightedPixelwise(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst, Float factor, Float factor1) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("factor", factor);
        parameters.put("factor1", factor1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "addWeightedPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean argMaxProjection(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst_max, ClearCLImage dst_arg) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("dst_arg", dst_arg);

        return clij.execute(Kernels.class, "projections.cl", "arg_max_project_3d_2d", parameters);
    }

    public static boolean argMaxProjection(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_max, ClearCLBuffer dst_arg) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("dst_arg", dst_arg);

        return clij.execute(Kernels.class, "projections.cl", "arg_max_project_3d_2d", parameters);
    }

    public static boolean binaryAnd(ClearCLIJ clij, ClearCLImage src1, ClearCLImage src2, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_and_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryAnd(ClearCLIJ clij, ClearCLBuffer src1, ClearCLBuffer src2, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_and_" + src1.getDimension() + "d", parameters);
    }


    public static boolean binaryXOr(ClearCLIJ clij, ClearCLImage src1, ClearCLImage src2, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_xor_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryXOr(ClearCLIJ clij, ClearCLBuffer src1, ClearCLBuffer src2, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_xor_" + src1.getDimension() + "d", parameters);
    }



    public static boolean binaryNot(ClearCLIJ clij, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_not_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryNot(ClearCLIJ clij, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_not_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryOr(ClearCLIJ clij, ClearCLImage src1, ClearCLImage src2, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_or_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryOr(ClearCLIJ clij, ClearCLBuffer src1, ClearCLBuffer src2, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_or_" + src1.getDimension() + "d", parameters);
    }


    public static boolean blur(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer nX, Integer nY, Float sigmaX, Float sigmaY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", nX);
        parameters.put("Ny", nY);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_image2d", parameters);
    }

    public static boolean blur(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer nX, Integer nY, Float sigmaX, Float sigmaY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", nX);
        parameters.put("Ny", nY);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_image2d", parameters);
    }

    public static boolean blur(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer nX, Integer nY, Integer nZ, Float sigmaX, Float sigmaY, Float sigmaZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", nX);
        parameters.put("Ny", nY);
        parameters.put("Nz", nZ);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("sz", sigmaZ);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_image3d", parameters);
    }

    public static boolean blur(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer nX, Integer nY, Integer nZ, Float sigmaX, Float sigmaY, Float sigmaZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", nX);
        parameters.put("Ny", nY);
        parameters.put("Nz", nZ);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("sz", sigmaZ);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_image3d", parameters);
    }

    public static boolean blurSeparable(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, float blurSigmaX, float blurSigmaY, float blurSigmaZ) {
        return executeSeparableKernel(clij, src, dst, "blur.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(blurSigmaZ), blurSigmaX, blurSigmaY, blurSigmaZ, src.getDimension());
    }

    public static boolean blurSeparable(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, float blurSigmaX, float blurSigmaY, float blurSigmaZ) {
        return executeSeparableKernel(clij, src, dst, "blur.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(blurSigmaZ), blurSigmaX, blurSigmaY, blurSigmaZ, src.getDimension());
    }

    private static int radiusToKernelSize(int radius) {
        int kernelSize = radius * 2 + 1;
        return kernelSize;
    }

    private static int sigmaToKernelSize(float sigma) {
        int n = (int)(sigma * 3.5);
        if (n % 2 == 0) {
            n++;
        }
        return n;
    }

    private static boolean executeSeparableKernel(ClearCLIJ clij, Object src, Object dst, String clFilename, String kernelname, int nX, int nY, int nZ, float blurSigmaX, float blurSigmaY, float blurSigmaZ, long dimensions) {
        int[] n = new int[]{nX, nY, nZ};
        float[] blurSigma = new float[]{blurSigmaX, blurSigmaY, blurSigmaZ};

        Object temp;
        if (src instanceof ClearCLBuffer) {
            temp = clij.createCLBuffer((ClearCLBuffer) src);
        } else if (src instanceof ClearCLImage) {
            temp = clij.createCLImage((ClearCLImage) src);
        } else {
            throw new IllegalArgumentException("Error: Wrong type of images in blurSeparable");
        }

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("N", n[0]);
        parameters.put("s", blurSigma[0]);
        parameters.put("dim", 0);
        parameters.put("src", src);
        if (dimensions == 2) {
            parameters.put("dst", temp);
        } else {
            parameters.put("dst", dst);
        }
        clij.execute(Kernels.class, clFilename, kernelname, parameters);

        parameters.clear();
        parameters.put("N", n[1]);
        parameters.put("s", blurSigma[1]);
        parameters.put("dim", 1);
        if (dimensions == 2) {
            parameters.put("src", temp);
            parameters.put("dst", dst);
        } else {
            parameters.put("src", dst);
            parameters.put("dst", temp);
        }
        clij.execute(Kernels.class, clFilename, kernelname, parameters);

        if (dimensions == 3) {
            parameters.clear();
            parameters.put("N", n[2]);
            parameters.put("s", blurSigma[2]);
            parameters.put("dim", 2);
            parameters.put("src", temp);
            parameters.put("dst", dst);
            clij.execute(Kernels.class,
                    clFilename,
                    kernelname,
                    parameters);
        }
        if (temp instanceof ClearCLBuffer) {
            ((ClearCLBuffer) temp).close();
        } else if (temp instanceof ClearCLImage) {
            ((ClearCLImage) temp).close();
        }

        return true;
    }

    public static boolean blurSliceBySlice(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer nX, Integer nY, Float sigmaX, Float sigmaY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", nX);
        parameters.put("Ny", nY);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_slicewise_image3d", parameters);
    }

//  public static boolean blurSliceBySlice(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int nX, int nY, float sigmaX, float sigmaY) {
//    HashMap<String, Object> parameters = new HashMap<>();
//    parameters.put("Nx", nX);
//    parameters.put("Ny", nY);
//    parameters.put("sx", sigmaX);
//    parameters.put("sy", sigmaY);
//    parameters.put("src", src);
//    parameters.put("dst", dst);
//    return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_slicewise_image3d", parameters);
//  }

    public static boolean copy(ClearCLIJ clij, ClearCLImage src, ClearCLBuffer dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    private static boolean copyInternal(ClearCLIJ clij, Object src, Object dst, long srcNumberOfDimensions, long dstNumberOfDimensions) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(srcNumberOfDimensions, dstNumberOfDimensions)) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "duplication.cl", "copy_" + srcNumberOfDimensions + "d", parameters);
    }

    public static boolean copy(ClearCLIJ clij, ClearCLBuffer src, ClearCLImage dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    public static boolean copy(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    public static boolean copy(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    public static boolean copySlice(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer planeIndex) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("slice", planeIndex);
        if (src.getDimension() == 2 && dst.getDimension() == 3) {
            return clij.execute(Kernels.class, "duplication.cl", "putSliceInStack", parameters);
        } else if (src.getDimension() == 3 && dst.getDimension() == 2) {
            return clij.execute(Kernels.class, "duplication.cl", "copySlice", parameters);
        } else {
            throw new IllegalArgumentException("Images have wrong dimension. Must be 3D->2D or 2D->3D.");
        }
    }

    public static boolean copySlice(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer planeIndex) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("slice", planeIndex);
        return clij.execute(Kernels.class, "duplication.cl", "copySlice", parameters);

    }

    public static boolean crop(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer startX, Integer startY, Integer startZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        parameters.put("start_z", startZ);
        return clij.execute(Kernels.class, "duplication.cl", "crop_3d", parameters);
    }


    public static boolean crop(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer startX, Integer startY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        return clij.execute(Kernels.class, "duplication.cl", "crop_2d", parameters);
    }

    public static boolean crop(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer startX, Integer startY, Integer startZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        parameters.put("start_z", startZ);
        return clij.execute(Kernels.class, "duplication.cl","crop_3d", parameters);
    }

    public static boolean crop(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer startX, Integer startY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        return clij.execute(Kernels.class, "duplication.cl", "crop_2d", parameters);
    }

    public static boolean detectMaxima(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, true);
    }

    public static boolean detectMaxima(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, true);
    }

    public static boolean detectMaximaSliceBySlice(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, true);
    }

    public static boolean detectMaximaSliceBySlice(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, true);
    }

    public static boolean detectMinima(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, false);
    }

    public static boolean detectMinima(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, false);
    }

    public static boolean detectMinimaSliceBySlice(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, false);
    }

    public static boolean detectMinimaSliceBySlice(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, false);
    }

    public static boolean detectOptima(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Boolean detectMaxima) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("detect_maxima", detectMaxima ? 1 : 0);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }
        return clij.execute(Kernels.class, "detection.cl", "detect_local_optima_" + src.getDimension() + "d", parameters);
    }

    public static boolean detectOptima(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius, Boolean detectMaxima) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("detect_maxima", detectMaxima ? 1 : 0);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }
        return clij.execute(Kernels.class, "detection.cl", "detect_local_optima_" + src.getDimension() + "d", parameters);
    }

    public static boolean detectOptimaSliceBySlice(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Boolean detectMaxima) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("detect_maxima", detectMaxima ? 1 : 0);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }
        return clij.execute(Kernels.class, "detection.cl", "detect_local_optima_" + src.getDimension() + "d_slice_by_slice", parameters);
    }

    public static boolean detectOptimaSliceBySlice(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius, Boolean detectMaxima) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("detect_maxima", detectMaxima ? 1 : 0);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (detectOptima)");
        }
        return clij.execute(Kernels.class, "detection.cl", "detect_local_optima_" + src.getDimension() + "d_slice_by_slice", parameters);
    }

    public static boolean differenceOfGaussian(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Float sigmaMinuend, Float sigmaSubtrahend) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("sigma_minuend", sigmaMinuend);
        parameters.put("sigma_subtrahend", sigmaSubtrahend);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "differenceOfGaussian.cl", "subtract_convolved_images_" + src.getDimension() + "d_fast", parameters);
    }

    public static boolean differenceOfGaussianSliceBySlice(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Float sigmaMinuend, Float sigmaSubtrahend) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);
        parameters.put("sigma_minuend", sigmaMinuend);
        parameters.put("sigma_subtrahend", sigmaSubtrahend);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "differenceOfGaussian.cl", "subtract_convolved_images_" + src.getDimension() + "d_slice_by_slice", parameters);
    }

    public static boolean dilateBox(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_box_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean dilateBox(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_box_neighborhood_" + src.getDimension() + "d", parameters);
    }


    public static boolean dilateSphere(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean dilateSphere(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }



    public static boolean dividePixelwise(ClearCLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "dividePixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean dividePixelwise(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "dividePixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean downsample(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Float factorX, Float factorY, Float factorZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        parameters.put("factor_z", 1.f / factorZ);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_3d_nearest", parameters);
    }

    public static boolean downsample(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float factorX, Float factorY, Float factorZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        parameters.put("factor_z", 1.f / factorZ);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_3d_nearest", parameters);
    }

    public static boolean downsample(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Float factorX, Float factorY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_2d_nearest", parameters);
    }

    public static boolean downsample(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float factorX, Float factorY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_2d_nearest", parameters);
    }

    public static boolean downsampleSliceBySliceHalfMedian(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_xy_by_half_median", parameters);
    }

    public static boolean downsampleSliceBySliceHalfMedian(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_xy_by_half_median", parameters);
    }

    public static boolean erodeSphere(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean erodeSphere(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean erodeBox(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_box_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean erodeBox(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_box_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean flip(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Boolean flipx, Boolean flipy, Boolean flipz) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        parameters.put("flipz", flipz ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_3d", parameters);
    }

    public static boolean flip(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Boolean flipx, Boolean flipy) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_2d", parameters);
    }

    public static boolean flip(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Boolean flipx, Boolean flipy, Boolean flipz) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        parameters.put("flipz", flipz ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_3d", parameters);
    }

    public static boolean flip(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Boolean flipx, Boolean flipy) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_2d", parameters);
    }

    public static boolean invert(ClearCLIJ clij, ClearCLImage input3d, ClearCLImage output3d) {
        return multiplyScalar(clij, input3d, output3d, -1f);
    }

    public static boolean invertBinary(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "invert_" + src.getDimension() + "d", parameters);
    }

    public static boolean invertBinary(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "invert_" + src.getDimension() + "d", parameters);
    }

    public static boolean localThreshold(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, ClearCLImage threshold) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("local_threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }

        return clij.execute(Kernels.class, "thresholding.cl", "apply_local_threshold_" + src.getDimension() + "d", parameters);
    }


    public static boolean localThreshold(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, ClearCLBuffer threshold) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("local_threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }

        return clij.execute(Kernels.class, "thresholding.cl", "apply_local_threshold_" + src.getDimension() + "d", parameters);
    }

    public static boolean mask(ClearCLIJ clij, ClearCLImage src, ClearCLImage mask, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (mask)");
        }
        return clij.execute(Kernels.class, "mask.cl", "mask_" + src.getDimension() + "d", parameters);
    }

    public static boolean mask(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer mask, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (mask)");
        }
        return clij.execute(Kernels.class, "mask.cl", "mask_" + src.getDimension() + "d", parameters);
    }

    public static boolean maskStackWithPlane(ClearCLIJ clij, ClearCLImage src, ClearCLImage mask, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "mask.cl", "maskStackWithPlane", parameters);
    }

    public static boolean maskStackWithPlane(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer mask, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "mask.cl", "maskStackWithPlane", parameters);
    }

    public static boolean maximum(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d", parameters);
    }

    public static boolean maximum(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d", parameters);
    }

    public static boolean maximum(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image3d", parameters);
    }

    public static boolean maximum(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image3d", parameters);
    }

    public static boolean maximumIJ(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d_ij", parameters);
    }

    public static boolean maximumIJ(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d_ij", parameters);
    }

    public static boolean maximumSliceBySlice(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_slicewise_image3d", parameters);
    }

    public static boolean maximumSeparable(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "max_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean maximumSeparable(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "max_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean maximumSliceBySlice(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_slicewise_image3d", parameters);
    }

    public static boolean maxPixelwise(ClearCLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maxPixelwise)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean maxPixelwise(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maxPixelwise)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean maxPixelwiseScalar(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Float valueB) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maxPixelwise)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean maxPixelwiseScalar(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float valueB) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maxPixelwise)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean minPixelwise(ClearCLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minPixelwise)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean minPixelwise(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minPixelwise)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean minPixelwiseScalar(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Float valueB) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minPixelwiseScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean minPixelwiseScalar(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float valueB) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minPixelwiseScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }


    public static boolean maxProjection(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst_max) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);

        clij.execute(Kernels.class, "projections.cl", "max_project_3d_2d", parameters);

        return true;
    }

    public static boolean maxProjection(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_max) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);

        clij.execute(Kernels.class, "projections.cl", "max_project_3d_2d", parameters);

        return true;
    }

    public static boolean maxProjection(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst_max, Integer projectedDimensionX, Integer projectedDimensionY, Integer projectedDimension) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("projection_x", projectedDimensionX);
        parameters.put("projection_y", projectedDimensionY);
        parameters.put("projection_dim", projectedDimension);

        clij.execute(Kernels.class, "projections.cl", "max_project_dim_select_3d_2d", parameters);

        return true;
    }

    public static boolean maxProjection(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_max, Integer projectedDimensionX, Integer projectedDimensionY, Integer projectedDimension) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("projection_x", projectedDimensionX);
        parameters.put("projection_y", projectedDimensionY);
        parameters.put("projection_dim", projectedDimension);

        clij.execute(Kernels.class, "projections.cl", "max_project_dim_select_3d_2d", parameters);

        return true;
    }

    public static boolean mean(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d", parameters);
    }

    public static boolean mean(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d", parameters);
    }

    public static boolean meanIJ(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d_ij", parameters);
    }

    public static boolean meanIJ(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d_ij", parameters);
    }

    public static boolean mean(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image3d", parameters);
    }

    public static boolean mean(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image3d", parameters);
    }

    public static boolean meanSeparable(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "mean_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean meanSeparable(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "mean_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }


    public static boolean meanSliceBySlice(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_slicewise_image3d", parameters);
    }

    public static boolean meanSliceBySlice(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_slicewise_image3d", parameters);
    }

    public static boolean median(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the median filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_image2d", parameters);
    }

    public static boolean median(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the median filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_image2d", parameters);
    }

    public static boolean median(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        if (kernelSizeX * kernelSizeY * kernelSizeZ > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the median filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "median_image3d", parameters);
    }

    public static boolean median(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        if (kernelSizeX * kernelSizeY * kernelSizeZ > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the median filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "median_image3d", parameters);
    }

    public static boolean medianSliceBySlice(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the median filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_slicewise_image3d", parameters);
    }

    public static boolean medianSliceBySlice(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the median filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_slicewise_image3d", parameters);
    }

    public static boolean minimum(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d", parameters);
    }

    public static boolean minimum(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d", parameters);
    }

    public static boolean minimum(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image3d", parameters);
    }

    public static boolean minimum(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image3d", parameters);
    }

    public static boolean minimumIJ(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d_ij", parameters);
    }

    public static boolean minimumIJ(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d_ij", parameters);
    }


    public static boolean minimumSeparable(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "min_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean minimumSeparable(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "min_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean minimumSliceBySlice(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_slicewise_image3d", parameters);
    }

    public static boolean minimumSliceBySlice(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_slicewise_image3d", parameters);
    }

    public static boolean multiplyPixelwise(ClearCLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "multiplyPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplyPixelwise(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "multiplyPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplyScalar(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Float scalar) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "multiplyScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplyScalar(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float scalar) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "multiplyScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplySliceBySliceWithScalars(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, float[] scalars) {
        if (dst.getDimensions()[2] != scalars.length) {
            throw new IllegalArgumentException("Error: Wrong number of scalars in array.");
        }

        FloatBuffer buffer = FloatBuffer.allocate(scalars.length);
        buffer.put(scalars);

        ClearCLBuffer clBuffer = clij.createCLBuffer(new long[]{scalars.length}, NativeTypeEnum.Float);
        clBuffer.readFrom(buffer, true);
        buffer.clear();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("src", src);
        map.put("scalars", clBuffer);
        map.put("dst", dst);
        boolean result = clij.execute(Kernels.class, "math.cl", "multiplySliceBySliceWithScalars", map);

        clBuffer.close();

        return result;
    }

    public static boolean multiplySliceBySliceWithScalars(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, float[] scalars) {
        if (dst.getDimensions()[2] != scalars.length) {
            throw new IllegalArgumentException("Error: Wrong number of scalars in array.");
        }

        FloatBuffer buffer = FloatBuffer.allocate(scalars.length);
        buffer.put(scalars);

        ClearCLBuffer clBuffer = clij.createCLBuffer(new long[]{scalars.length}, NativeTypeEnum.Float);
        clBuffer.readFrom(buffer, true);
        buffer.clear();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("src", src);
        map.put("scalars", clBuffer);
        map.put("dst", dst);
        boolean result = clij.execute(Kernels.class, "math.cl", "multiplySliceBySliceWithScalars", map);

        clBuffer.close();

        return result;
    }

    public static boolean multiplyStackWithPlane(ClearCLIJ clij, ClearCLImage input3d, ClearCLImage input2d, ClearCLImage output3d) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input3d);
        parameters.put("src1", input2d);
        parameters.put("dst", output3d);
        return clij.execute(Kernels.class, "math.cl", "multiplyStackWithPlanePixelwise", parameters);
    }

    public static boolean multiplyStackWithPlane(ClearCLIJ clij, ClearCLBuffer input3d, ClearCLBuffer input2d, ClearCLBuffer output3d) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input3d);
        parameters.put("src1", input2d);
        parameters.put("dst", output3d);
        return clij.execute(Kernels.class, "math.cl", "multiplyStackWithPlanePixelwise", parameters);
    }

    public static boolean power(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Float exponent) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("exponent", exponent);
        return clij.execute(Kernels.class, "math.cl", "power_" + src.getDimension() + "d", parameters);
    }

    public static boolean power(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float exponent) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("exponent", exponent);

        return clij.execute(Kernels.class, "math.cl", "power_" + src.getDimension() + "d", parameters);
    }

    public static boolean resliceBottom(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_bottom_3d", parameters);
    }

    public static boolean resliceBottom(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_bottom_3d", parameters);
    }

    public static boolean resliceLeft(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_left_3d", parameters);
    }

    public static boolean resliceLeft(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_left_3d", parameters);
    }

    public static boolean resliceRight(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_right_3d", parameters);
    }

    public static boolean resliceRight(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_right_3d", parameters);
    }

    public static boolean resliceTop(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_top_3d", parameters);
    }


    public static boolean resliceTop(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_top_3d", parameters);
    }

    public static boolean rotateLeft(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_left_" + dst.getDimension() + "d", parameters);
    }

    public static boolean rotateLeft(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_left_" + dst.getDimension() + "d", parameters);
    }

    public static boolean rotateRight(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_right_" + dst.getDimension() + "d", parameters);
    }

    public static boolean rotateRight(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_right_" + dst.getDimension() + "d", parameters);
    }

    public static boolean set(ClearCLIJ clij, ClearCLImage clImage, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);

        return clij.execute(Kernels.class, "set.cl", "set_" + clImage.getDimension() + "d", parameters);
    }

    public static boolean set(ClearCLIJ clij, ClearCLBuffer clImage, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);

        return clij.execute(Kernels.class, "set.cl", "set_" + clImage.getDimension() + "d", parameters);
    }

    public static boolean splitStack(ClearCLIJ clij, ClearCLImage clImageIn, ClearCLImage... clImagesOut) {
        if (clImagesOut.length > 12) {
            throw new IllegalArgumentException("Error: splitStack does not support more than 12 stacks.");
        }
        if (clImagesOut.length == 1) {
            return copy(clij, clImageIn, clImagesOut[0]);
        }
        if (clImagesOut.length == 0) {
            throw new IllegalArgumentException("Error: splitstack didn't get any output images.");
        }

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImageIn);
        for (int i = 0; i < clImagesOut.length; i++) {
            parameters.put("dst" + i, clImagesOut[i]);
        }

        return clij.execute(Kernels.class, "stacksplitting.cl", "split_" + clImagesOut.length + "_stacks", parameters);
    }

    public static boolean splitStack(ClearCLIJ clij, ClearCLBuffer clImageIn, ClearCLBuffer... clImagesOut) {
        if (clImagesOut.length > 12) {
            throw new IllegalArgumentException("Error: splitStack does not support more than 12 stacks.");
        }
        if (clImagesOut.length == 1) {
            return copy(clij, clImageIn, clImagesOut[0]);
        }
        if (clImagesOut.length == 0) {
            throw new IllegalArgumentException("Error: splitstack didn't get any output images.");
        }

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImageIn);
        for (int i = 0; i < clImagesOut.length; i++) {
            parameters.put("dst" + i, clImagesOut[i]);
        }

        return clij.execute(Kernels.class, "stacksplitting.cl", "split_" + clImagesOut.length + "_stacks", parameters);
    }

    public static double sumPixels(ClearCLIJ clij, ClearCLImage clImage) {
        ClearCLImage clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLImage(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getChannelDataType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "sum_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float sum = 0;
        while (cursor.hasNext()) {
            sum += ((RealType) cursor.next()).getRealFloat();
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return sum;
    }

    public static double sumPixels(ClearCLIJ clij, ClearCLBuffer clImage) {
        ClearCLBuffer clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLBuffer(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "sum_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float sum = 0;
        while (cursor.hasNext()) {
            sum += ((RealType) cursor.next()).getRealFloat();
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return sum;
    }

    public static double[] sumPixelsSliceBySlice(ClearCLIJ clij, ClearCLImage input) {
        if (input.getDimension() == 2) {
            return new double[]{sumPixels(clij, input)};
        }

        int numberOfImages = (int) input.getDepth();
        double[] result = new double[numberOfImages];

        ClearCLImage slice = clij.createCLImage(new long[]{input.getWidth(), input.getHeight()}, input.getChannelDataType());
        for (int z = 0; z < numberOfImages; z++) {
            copySlice(clij, input, slice, z);
            result[z] = sumPixels(clij, slice);
        }
        slice.close();
        return result;
    }

    public static double[] sumPixelsSliceBySlice(ClearCLIJ clij, ClearCLBuffer input) {
        if (input.getDimension() == 2) {
            return new double[]{sumPixels(clij, input)};
        }

        int numberOfImages = (int) input.getDepth();
        double[] result = new double[numberOfImages];

        ClearCLBuffer slice = clij.createCLBuffer(new long[]{input.getWidth(), input.getHeight()}, input.getNativeType());
        for (int z = 0; z < numberOfImages; z++) {
            copySlice(clij, input, slice, z);
            result[z] = sumPixels(clij, slice);
        }
        slice.close();
        return result;
    }

    public static boolean sumProjection(ClearCLIJ clij, ClearCLImage clImage, ClearCLImage clReducedImage) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImage);
        parameters.put("dst", clReducedImage);
        return clij.execute(Kernels.class, "projections.cl", "sum_project_3d_2d", parameters);
    }

    public static boolean sumProjection(ClearCLIJ clij, ClearCLBuffer clImage, ClearCLBuffer clReducedImage) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImage);
        parameters.put("dst", clReducedImage);
        return clij.execute(Kernels.class, "projections.cl", "sum_project_3d_2d", parameters);
    }

    public static boolean tenengradWeightsSliceBySlice(ClearCLIJ clij, ClearCLImage clImageOut, ClearCLImage clImageIn) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImageIn);
        parameters.put("dst", clImageOut);
        return clij.execute(Kernels.class,"tenengradFusion.cl", "tenengrad_weight_unnormalized_slice_wise", parameters);
    }

    public static boolean tenengradFusion(ClearCLIJ clij, ClearCLImage clImageOut, float[] blurSigmas, ClearCLImage... clImagesIn) {
        return tenengradFusion(clij, clImageOut, blurSigmas, 1.0f, clImagesIn);
    }

    public static boolean tenengradFusion(ClearCLIJ clij, ClearCLImage clImageOut, float[] blurSigmas, float exponent, ClearCLImage... clImagesIn) {
        if (clImagesIn.length > 12) {
            throw new IllegalArgumentException("Error: tenengradFusion does not support more than 12 stacks.");
        }
        if (clImagesIn.length == 1) {
            return copy(clij, clImagesIn[0], clImageOut);
        }
        if (clImagesIn.length == 0) {
            throw new IllegalArgumentException("Error: tenengradFusion didn't get any output images.");
        }
        if (!clImagesIn[0].isFloat()) {
            System.out.println("Warning: tenengradFusion may only work on float images!");
        }

        HashMap<String, Object> lFusionParameters = new HashMap<>();

        ClearCLImage temporaryImage = clij.createCLImage(clImagesIn[0]);
        ClearCLImage temporaryImage2 = null;
        if (Math.abs(exponent - 1.0f) > 0.0001) {
            temporaryImage2 = clij.createCLImage(clImagesIn[0]);
        }

        ClearCLImage[] temporaryImages = new ClearCLImage[clImagesIn.length];
        for (int i = 0; i < clImagesIn.length; i++) {
            HashMap<String, Object> parameters = new HashMap<>();
            temporaryImages[i] = clij.createCLImage(clImagesIn[i]);
            parameters.put("src", clImagesIn[i]);
            parameters.put("dst", temporaryImage);

            clij.execute(Kernels.class, "tenengradFusion.cl", "tenengrad_weight_unnormalized", parameters);

            if (temporaryImage2 != null) {
                power(clij, temporaryImage, temporaryImage2, exponent);
                blurSeparable(clij, temporaryImage2, temporaryImages[i], blurSigmas[0], blurSigmas[1], blurSigmas[2]);
            } else {
                blurSeparable(clij, temporaryImage, temporaryImages[i], blurSigmas[0], blurSigmas[1], blurSigmas[2]);
            }

            lFusionParameters.put("src" + i, clImagesIn[i]);
            lFusionParameters.put("weight" + i, temporaryImages[i]);
        }

        lFusionParameters.put("dst", clImageOut);
        lFusionParameters.put("factor", (int) (clImagesIn[0].getWidth() / temporaryImages[0].getWidth()));

        boolean success = clij.execute(Kernels.class, "tenengradFusion.cl", String.format("tenengrad_fusion_with_provided_weights_%d_images", clImagesIn.length), lFusionParameters);

        temporaryImage.close();
        for (int i = 0; i < temporaryImages.length; i++) {
            temporaryImages[i].close();
        }

        if (temporaryImage2 != null) {
            temporaryImage2.close();
        }

        return success;
    }

    public static boolean threshold(ClearCLIJ clij, ClearCLImage src, ClearCLImage dst, Float threshold) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }

        return clij.execute(Kernels.class, "thresholding.cl", "apply_threshold_" + src.getDimension() + "d", parameters);
    }

    public static boolean threshold(ClearCLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float threshold) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addScalar)");
        }

        return clij.execute(Kernels.class, "thresholding.cl", "apply_threshold_" + src.getDimension() + "d", parameters);
    }

    private static boolean checkDimensions(long... numberOfDimensions) {
        for (int i = 0; i < numberOfDimensions.length - 1; i++) {
            if (!(numberOfDimensions[i] == numberOfDimensions[i + 1])) {
                return false;
            }
        }
        return true;
    }

}

