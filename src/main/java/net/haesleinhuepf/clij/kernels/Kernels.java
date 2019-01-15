package net.haesleinhuepf.clij.kernels;

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


/**
 * This class contains convenience access functions for OpenCL based
 * image processing.
 * <p>
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * March 2018
 */
public class Kernels {
    public static boolean absolute(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class,"math.cl", "absolute_" + src.getDimension() + "d", parameters);
    }

    public static boolean absolute(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "absolute_" + src.getDimension() + "d", parameters);
    }


    public static boolean addImages(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "addPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean addImages(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "addPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean addImageAndScalar(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float scalar) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "addScalar_" + src.getDimension() + "d", parameters);
    }


    public static boolean addImageAndScalar(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float scalar) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "addScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean addImagesWeighted(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst, Float factor, Float factor1) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("factor", factor);
        parameters.put("factor1", factor1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "addWeightedPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean addImagesWeighted(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst, Float factor, Float factor1) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("factor", factor);
        parameters.put("factor1", factor1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "addWeightedPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean argMaximumZProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst_max, ClearCLImage dst_arg) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("dst_arg", dst_arg);

        return clij.execute(Kernels.class, "projections.cl", "arg_max_project_3d_2d", parameters);
    }

    public static boolean argMaximumZProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_max, ClearCLBuffer dst_arg) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("dst_arg", dst_arg);

        return clij.execute(Kernels.class, "projections.cl", "arg_max_project_3d_2d", parameters);
    }

    public static boolean binaryAnd(CLIJ clij, ClearCLImage src1, ClearCLImage src2, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_and_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryAnd(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer src2, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_and_" + src1.getDimension() + "d", parameters);
    }


    public static boolean binaryXOr(CLIJ clij, ClearCLImage src1, ClearCLImage src2, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_xor_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryXOr(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer src2, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_xor_" + src1.getDimension() + "d", parameters);
    }



    public static boolean binaryNot(CLIJ clij, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_not_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryNot(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_not_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryOr(CLIJ clij, ClearCLImage src1, ClearCLImage src2, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_or_" + src1.getDimension() + "d", parameters);
    }

    public static boolean binaryOr(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer src2, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "binaryProcessing.cl", "binary_or_" + src1.getDimension() + "d", parameters);
    }


    public static boolean blur(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Float sigmaX, Float sigmaY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_image2d", parameters);
    }

    public static boolean blur(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Float sigmaX, Float sigmaY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_image2d", parameters);
    }

    public static boolean blur(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ, Float sigmaX, Float sigmaY, Float sigmaZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("sz", sigmaZ);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_image3d", parameters);
    }

    public static boolean blur(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ, Float sigmaX, Float sigmaY, Float sigmaZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("sz", sigmaZ);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_image3d", parameters);
    }

    public static boolean blurIJ(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float sigma) {
        int kernelSize = sigmaToKernelSize(sigma);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", kernelSize);
        parameters.put("Ny", kernelSize);
        parameters.put("sx", sigma);
        parameters.put("sy", sigma);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_image2d_ij", parameters);
    }

    public static boolean blurIJ(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float sigma) {
        int kernelSize = sigmaToKernelSize(sigma);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", kernelSize);
        parameters.put("Ny", kernelSize);
        parameters.put("sx", sigma);
        parameters.put("sy", sigma);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_image2d_ij", parameters);
    }

    public static boolean blurFast(CLIJ clij, ClearCLImage src, ClearCLImage dst, float blurSigmaX, float blurSigmaY, float blurSigmaZ) {
        return executeSeparableKernel(clij, src, dst, "blur.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(blurSigmaZ), blurSigmaX, blurSigmaY, blurSigmaZ, src.getDimension());
    }

    public static boolean blurFast(CLIJ clij, ClearCLImage src, ClearCLBuffer dst, float blurSigmaX, float blurSigmaY, float blurSigmaZ) {
        return executeSeparableKernel(clij, src, dst, "blur.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(blurSigmaZ), blurSigmaX, blurSigmaY, blurSigmaZ, src.getDimension());
    }

    public static boolean blurFast(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, float blurSigmaX, float blurSigmaY, float blurSigmaZ) {
        return executeSeparableKernel(clij, src, dst, "blur.cl", "gaussian_blur_sep_image" + src.getDimension() + "d", sigmaToKernelSize(blurSigmaX), sigmaToKernelSize(blurSigmaY), sigmaToKernelSize(blurSigmaZ), blurSigmaX, blurSigmaY, blurSigmaZ, src.getDimension());
    }


    private static boolean executeSeparableKernel(CLIJ clij, Object src, Object dst, String clFilename, String kernelname, int kernelSizeX, int kernelSizeY, int kernelSizeZ, float blurSigmaX, float blurSigmaY, float blurSigmaZ, long dimensions) {
        int[] n = new int[]{kernelSizeX, kernelSizeY, kernelSizeZ};
        float[] blurSigma = new float[]{blurSigmaX, blurSigmaY, blurSigmaZ};

        Object temp;
        if (src instanceof ClearCLBuffer) {
            temp = clij.createCLBuffer((ClearCLBuffer) src);
        } else if (src instanceof ClearCLImage) {
            temp = clij.createCLImage((ClearCLImage) src);
        } else {
            throw new IllegalArgumentException("Error: Wrong type of images in blurFast");
        }

        HashMap<String, Object> parameters = new HashMap<>();

        if (blurSigma[0] > 0) {
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
        } else {
            if (dimensions == 2) {
                Kernels.copyInternal(clij, src, temp, 2, 2);
            } else {
                Kernels.copyInternal(clij, src, dst, 3, 3);
            }
        }

        if (blurSigma[1] > 0) {
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
        } else {
            if (dimensions == 2) {
                Kernels.copyInternal(clij, temp, dst, 2, 2);
            } else {
                Kernels.copyInternal(clij, dst, temp, 3, 3);
            }
        }

        if (dimensions == 3) {
            if (blurSigma[2] > 0) {
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
            } else {
                Kernels.copyInternal(clij, temp, dst,3, 3);
            }
        }

        if (temp instanceof ClearCLBuffer) {
            ((ClearCLBuffer) temp).close();
        } else if (temp instanceof ClearCLImage) {
            ((ClearCLImage) temp).close();
        }

        return true;
    }

    public static boolean blurSliceBySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Float sigmaX, Float sigmaY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_slicewise_image3d", parameters);
    }

    public static boolean blurSliceBySlice(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int kernelSizeX, int kernelSizeY, float sigmaX, float sigmaY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("sx", sigmaX);
        parameters.put("sy", sigmaY);
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "blur.cl", "gaussian_blur_slicewise_image3d", parameters);
    }

    public static boolean copy(CLIJ clij, ClearCLImage src, ClearCLBuffer dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    private static boolean copyInternal(CLIJ clij, Object src, Object dst, long srcNumberOfDimensions, long dstNumberOfDimensions) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(srcNumberOfDimensions, dstNumberOfDimensions)) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "duplication.cl", "copy_" + srcNumberOfDimensions + "d", parameters);
    }

    public static boolean copy(CLIJ clij, ClearCLBuffer src, ClearCLImage dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    public static boolean copy(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    public static boolean copy(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        return copyInternal(clij, src, dst, src.getDimension(), dst.getDimension());
    }

    public static boolean copySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer planeIndex) {
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

    public static boolean copySlice(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer planeIndex) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("slice", planeIndex);
        return clij.execute(Kernels.class, "duplication.cl", "copySlice", parameters);

    }

    public static boolean crop(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer startX, Integer startY, Integer startZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        parameters.put("start_z", startZ);
        return clij.execute(Kernels.class, "duplication.cl", "crop_3d", parameters);
    }


    public static boolean crop(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer startX, Integer startY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        return clij.execute(Kernels.class, "duplication.cl", "crop_2d", parameters);
    }

    public static boolean crop(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer startX, Integer startY, Integer startZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        parameters.put("start_z", startZ);
        return clij.execute(Kernels.class, "duplication.cl","crop_3d", parameters);
    }

    public static boolean crop(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer startX, Integer startY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("start_x", startX);
        parameters.put("start_y", startY);
        return clij.execute(Kernels.class, "duplication.cl", "crop_2d", parameters);
    }

    public static boolean detectMaximaBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, true);
    }

    public static boolean detectMaximaBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, true);
    }

    public static boolean detectMaximaSliceBySliceBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, true);
    }

    public static boolean detectMaximaSliceBySliceBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, true);
    }

    public static boolean detectMinimaBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, false);
    }

    public static boolean detectMinimaBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptima(clij, src, dst, radius, false);
    }

    public static boolean detectMinimaSliceBySliceBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, false);
    }

    public static boolean detectMinimaSliceBySliceBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        return detectOptimaSliceBySlice(clij, src, dst, radius, false);
    }

    public static boolean detectOptima(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Boolean detectMaxima) {
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

    public static boolean detectOptima(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius, Boolean detectMaxima) {
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

    public static boolean detectOptimaSliceBySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Boolean detectMaxima) {
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

    public static boolean detectOptimaSliceBySlice(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius, Boolean detectMaxima) {
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

    public static boolean differenceOfGaussian(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Float sigmaMinuend, Float sigmaSubtrahend) {
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

    public static boolean differenceOfGaussianSliceBySlice(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius, Float sigmaMinuend, Float sigmaSubtrahend) {
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

    public static boolean dilateBox(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_box_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean dilateBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_box_neighborhood_" + src.getDimension() + "d", parameters);
    }


    public static boolean dilateSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean dilateSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }
        return clij.execute(Kernels.class, "binaryProcessing.cl", "dilate_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }



    public static boolean divideImages(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "dividePixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean divideImages(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "dividePixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean downsample(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float factorX, Float factorY, Float factorZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        parameters.put("factor_z", 1.f / factorZ);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_3d_nearest", parameters);
    }

    public static boolean downsample(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float factorX, Float factorY, Float factorZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        parameters.put("factor_z", 1.f / factorZ);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_3d_nearest", parameters);
    }

    public static boolean downsample(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float factorX, Float factorY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_2d_nearest", parameters);
    }

    public static boolean downsample(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float factorX, Float factorY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("factor_x", 1.f / factorX);
        parameters.put("factor_y", 1.f / factorY);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_2d_nearest", parameters);
    }

    public static boolean downsampleSliceBySliceHalfMedian(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_xy_by_half_median", parameters);
    }

    public static boolean downsampleSliceBySliceHalfMedian(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        return clij.execute(Kernels.class, "downsampling.cl", "downsample_xy_by_half_median", parameters);
    }

    public static boolean erodeSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean erodeSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_diamond_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean erodeBox(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_box_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean erodeBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (copy)");
        }

        return clij.execute(Kernels.class, "binaryProcessing.cl", "erode_box_neighborhood_" + src.getDimension() + "d", parameters);
    }

    public static boolean flip(CLIJ clij, ClearCLImage src, ClearCLImage dst, Boolean flipx, Boolean flipy, Boolean flipz) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        parameters.put("flipz", flipz ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_3d", parameters);
    }

    public static boolean flip(CLIJ clij, ClearCLImage src, ClearCLImage dst, Boolean flipx, Boolean flipy) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_2d", parameters);
    }

    public static boolean flip(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Boolean flipx, Boolean flipy, Boolean flipz) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        parameters.put("flipz", flipz ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_3d", parameters);
    }

    public static boolean flip(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Boolean flipx, Boolean flipy) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("flipx", flipx ? 1 : 0);
        parameters.put("flipy", flipy ? 1 : 0);
        return clij.execute(Kernels.class, "flip.cl", "flip_2d", parameters);
    }

    public static boolean invert(CLIJ clij, ClearCLImage input3d, ClearCLImage output3d) {
        return multiplyImageAndScalar(clij, input3d, output3d, -1f);
    }

    public static boolean invert(CLIJ clij, ClearCLBuffer input3d, ClearCLBuffer output3d) {
        return multiplyImageAndScalar(clij, input3d, output3d, -1f);
    }

    public static boolean localThreshold(CLIJ clij, ClearCLImage src, ClearCLImage dst, ClearCLImage threshold) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("local_threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "thresholding.cl", "apply_local_threshold_" + src.getDimension() + "d", parameters);
    }


    public static boolean localThreshold(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, ClearCLBuffer threshold) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("local_threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "thresholding.cl", "apply_local_threshold_" + src.getDimension() + "d", parameters);
    }

    public static boolean mask(CLIJ clij, ClearCLImage src, ClearCLImage mask, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (mask)");
        }
        return clij.execute(Kernels.class, "mask.cl", "mask_" + src.getDimension() + "d", parameters);
    }

    public static boolean mask(CLIJ clij, ClearCLBuffer src, ClearCLBuffer mask, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (mask)");
        }
        return clij.execute(Kernels.class, "mask.cl", "mask_" + src.getDimension() + "d", parameters);
    }

    public static boolean maskStackWithPlane(CLIJ clij, ClearCLImage src, ClearCLImage mask, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "mask.cl", "maskStackWithPlane", parameters);
    }

    public static boolean maskStackWithPlane(CLIJ clij, ClearCLBuffer src, ClearCLBuffer mask, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("mask", mask);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "mask.cl", "maskStackWithPlane", parameters);
    }

    public static boolean maximumSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d", parameters);
    }

    public static boolean maximumSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d", parameters);
    }

    public static boolean maximumSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image3d", parameters);
    }

    public static boolean maximumSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image3d", parameters);
    }

    public static boolean maximumIJ(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d_ij", parameters);
    }

    public static boolean maximumIJ(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_image2d_ij", parameters);
    }

    public static boolean maximumSliceBySliceSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_slicewise_image3d", parameters);
    }

    public static boolean maximumBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "max_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean maximumBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "max_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean maximumSliceBySliceSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "maximum_slicewise_image3d", parameters);
    }

    public static boolean maximumImages(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maximumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean maximumImages(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maximumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean maximumImageAndScalar(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float valueB) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maximumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean maximumImageAndScalar(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float valueB) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (maximumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "maxPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean minimumImages(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minimumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean minimumImages(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minimumImages)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean minimumImageAndScalar(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float valueB) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minimumImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean minimumImageAndScalar(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float valueB) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("valueB", valueB);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (minimumImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "minPixelwiseScalar_" + src.getDimension() + "d", parameters);
    }


    public static boolean maximumZProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst_max) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);

        clij.execute(Kernels.class, "projections.cl", "max_project_3d_2d", parameters);

        return true;
    }

    public static boolean maximumZProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_max) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);

        clij.execute(Kernels.class, "projections.cl", "max_project_3d_2d", parameters);

        return true;
    }

    public static boolean minimumZProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst_min) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_min", dst_min);

        clij.execute(Kernels.class, "projections.cl", "min_project_3d_2d", parameters);

        return true;
    }

    public static boolean minimumZProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_min) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_min", dst_min);

        clij.execute(Kernels.class, "projections.cl", "min_project_3d_2d", parameters);

        return true;
    }

    public static boolean meanZProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clij.execute(Kernels.class, "projections.cl", "mean_project_3d_2d", parameters);

        return true;
    }

    public static boolean meanZProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clij.execute(Kernels.class, "projections.cl", "mean_project_3d_2d", parameters);

        return true;
    }


    public static boolean maximumXYZProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst_max, Integer projectedDimensionX, Integer projectedDimensionY, Integer projectedDimension) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("projection_x", projectedDimensionX);
        parameters.put("projection_y", projectedDimensionY);
        parameters.put("projection_dim", projectedDimension);

        clij.execute(Kernels.class, "projections.cl", "max_project_dim_select_3d_2d", parameters);

        return true;
    }

    public static boolean maximumXYZProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst_max, Integer projectedDimensionX, Integer projectedDimensionY, Integer projectedDimension) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst_max", dst_max);
        parameters.put("projection_x", projectedDimensionX);
        parameters.put("projection_y", projectedDimensionY);
        parameters.put("projection_dim", projectedDimension);

        clij.execute(Kernels.class, "projections.cl", "max_project_dim_select_3d_2d", parameters);

        return true;
    }

    public static boolean meanSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d", parameters);
    }

    public static boolean meanSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d", parameters);
    }

    public static boolean meanIJ(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d_ij", parameters);
    }

    public static boolean meanIJ(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image2d_ij", parameters);
    }

    public static boolean meanSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image3d", parameters);
    }

    public static boolean meanSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "mean_image3d", parameters);
    }

    public static boolean meanBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "mean_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean meanBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "mean_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }


    public static boolean meanSliceBySliceSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_slicewise_image3d", parameters);
    }

    public static boolean meanSliceBySliceSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "mean_slicewise_image3d", parameters);
    }

    public static boolean medianSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_image2d", parameters);
    }

    public static boolean medianSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_image2d", parameters);
    }

    public static boolean medianSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        if (kernelSizeX * kernelSizeY * kernelSizeZ > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "median_image3d", parameters);
    }

    public static boolean medianSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        if (kernelSizeX * kernelSizeY * kernelSizeZ > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "median_image3d", parameters);
    }

    public static boolean medianSliceBySliceSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_slicewise_image3d", parameters);
    }

    public static boolean medianSliceBySliceSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        if (kernelSizeX * kernelSizeY > CLKernelExecutor.MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException("Error: kernels of the medianSphere filter is too big. Consider increasing MAX_ARRAY_SIZE.");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "median_slicewise_image3d", parameters);
    }

    public static boolean minimumSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d", parameters);
    }

    public static boolean minimumSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d", parameters);
    }

    public static boolean minimumSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image3d", parameters);
    }

    public static boolean minimumSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY, Integer kernelSizeZ) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);
        parameters.put("Nz", kernelSizeZ);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image3d", parameters);
    }

    public static boolean minimumIJ(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d_ij", parameters);
    }

    public static boolean minimumIJ(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer radius) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("radius", radius);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_image2d_ij", parameters);
    }


    public static boolean minimumBox(CLIJ clij, ClearCLImage src, ClearCLImage dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "min_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean minimumBox(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, int radiusX, int radiusY, int radiusZ) {
        return executeSeparableKernel(clij, src, dst, "filtering.cl", "min_sep_image" + src.getDimension() + "d", radiusToKernelSize(radiusX), radiusToKernelSize(radiusY), radiusToKernelSize(radiusZ), radiusX, radiusY, radiusZ, src.getDimension());
    }

    public static boolean minimumSliceBySliceSphere(CLIJ clij, ClearCLImage src, ClearCLImage dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_slicewise_image3d", parameters);
    }

    public static boolean minimumSliceBySliceSphere(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Integer kernelSizeX, Integer kernelSizeY) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("Nx", kernelSizeX);
        parameters.put("Ny", kernelSizeY);

        return clij.execute(Kernels.class, "filtering.cl", "minimum_slicewise_image3d", parameters);
    }

    public static boolean multiplyImages(CLIJ clij, ClearCLImage src, ClearCLImage src1, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "math.cl", "multiplyPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplyImages(CLIJ clij, ClearCLBuffer src, ClearCLBuffer src1, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("src1", src1);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), src1.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "multiplyPixelwise_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplyImageAndScalar(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float scalar) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "multiplyScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplyImageAndScalar(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float scalar) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }
        return clij.execute(Kernels.class, "math.cl", "multiplyScalar_" + src.getDimension() + "d", parameters);
    }

    public static boolean multiplySliceBySliceWithScalars(CLIJ clij, ClearCLImage src, ClearCLImage dst, float[] scalars) {
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

    public static boolean multiplySliceBySliceWithScalars(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, float[] scalars) {
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

    public static boolean multiplyStackWithPlane(CLIJ clij, ClearCLImage input3d, ClearCLImage input2d, ClearCLImage output3d) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input3d);
        parameters.put("src1", input2d);
        parameters.put("dst", output3d);
        return clij.execute(Kernels.class, "math.cl", "multiplyStackWithPlanePixelwise", parameters);
    }

    public static boolean multiplyStackWithPlane(CLIJ clij, ClearCLBuffer input3d, ClearCLBuffer input2d, ClearCLBuffer output3d) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", input3d);
        parameters.put("src1", input2d);
        parameters.put("dst", output3d);
        return clij.execute(Kernels.class, "math.cl", "multiplyStackWithPlanePixelwise", parameters);
    }

    public static boolean power(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float exponent) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("exponent", exponent);
        return clij.execute(Kernels.class, "math.cl", "power_" + src.getDimension() + "d", parameters);
    }

    public static boolean power(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float exponent) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("exponent", exponent);

        return clij.execute(Kernels.class, "math.cl", "power_" + src.getDimension() + "d", parameters);
    }

    public static boolean radialProjection(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float deltaAngle) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("deltaAngle", deltaAngle);

        return clij.execute(Kernels.class, "projections.cl", "radialProjection3d", parameters);
    }

    public static boolean radialProjection(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float deltaAngle) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);
        parameters.put("deltaAngle", deltaAngle);

        return clij.execute(Kernels.class, "projections.cl", "radialProjection3d", parameters);
    }

    public static boolean resliceBottom(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_bottom_3d", parameters);
    }

    public static boolean resliceBottom(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_bottom_3d", parameters);
    }

    public static boolean resliceLeft(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_left_3d", parameters);
    }

    public static boolean resliceLeft(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_left_3d", parameters);
    }

    public static boolean resliceRight(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_right_3d", parameters);
    }

    public static boolean resliceRight(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_right_3d", parameters);
    }

    public static boolean resliceTop(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_top_3d", parameters);
    }


    public static boolean resliceTop(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "reslicing.cl", "reslice_top_3d", parameters);
    }

    public static boolean rotateLeft(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_left_" + dst.getDimension() + "d", parameters);
    }

    public static boolean rotateLeft(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_left_" + dst.getDimension() + "d", parameters);
    }

    public static boolean rotateRight(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_right_" + dst.getDimension() + "d", parameters);
    }

    public static boolean rotateRight(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Kernels.class, "rotate.cl", "rotate_right_" + dst.getDimension() + "d", parameters);
    }

    public static boolean set(CLIJ clij, ClearCLImage clImage, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);

        return clij.execute(Kernels.class, "set.cl", "set_" + clImage.getDimension() + "d", parameters);
    }

    public static boolean set(CLIJ clij, ClearCLBuffer clImage, Float value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("dst", clImage);
        parameters.put("value", value);

        return clij.execute(Kernels.class, "set.cl", "set_" + clImage.getDimension() + "d", parameters);
    }

    public static boolean splitStack(CLIJ clij, ClearCLImage clImageIn, ClearCLImage... clImagesOut) {
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

    public static boolean splitStack(CLIJ clij, ClearCLBuffer clImageIn, ClearCLBuffer... clImagesOut) {
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

    public static boolean subtract(CLIJ clij, ClearCLImage source1, ClearCLImage source2, ClearCLImage destination) {
        return addImagesWeighted(clij, source1, source2, destination, 1f, -1f);
    }

    public static boolean subtract(CLIJ clij, ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return addImagesWeighted(clij, source1, source2, destination, 1f, -1f);
    }

    public static double maximumOfAllPixels(CLIJ clij, ClearCLImage clImage) {
        ClearCLImage clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLImage(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getChannelDataType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst_max", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "max_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float maximumGreyValue = -Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            if (maximumGreyValue < greyValue) {
                maximumGreyValue = greyValue;
            }
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return maximumGreyValue;
    }

    public static double maximumOfAllPixels(CLIJ clij, ClearCLBuffer clImage) {
        ClearCLBuffer clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLBuffer(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst_max", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "max_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float maximumGreyValue = -Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            if (maximumGreyValue < greyValue) {
                maximumGreyValue = greyValue;
            }
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return maximumGreyValue;
    }

    public static double minimumOfAllPixels(CLIJ clij, ClearCLImage clImage) {
        ClearCLImage clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLImage(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getChannelDataType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst_min", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "min_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float minimumGreyValue = Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            if (minimumGreyValue > greyValue) {
                minimumGreyValue = greyValue;
            }
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return minimumGreyValue;
    }

    public static double minimumOfAllPixels(CLIJ clij, ClearCLBuffer clImage) {
        ClearCLBuffer clReducedImage = clImage;
        if (clImage.getDimension() == 3) {
            clReducedImage = clij.createCLBuffer(new long[]{clImage.getWidth(), clImage.getHeight()}, clImage.getNativeType());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("src", clImage);
            parameters.put("dst_min", clReducedImage);
            clij.execute(Kernels.class, "projections.cl", "min_project_3d_2d", parameters);
        }

        RandomAccessibleInterval rai = clij.convert(clReducedImage, RandomAccessibleInterval.class);
        Cursor cursor = Views.iterable(rai).cursor();
        float minimumGreyValue = Float.MAX_VALUE;
        while (cursor.hasNext()) {
            float greyValue = ((RealType) cursor.next()).getRealFloat();
            if (minimumGreyValue > greyValue) {
                minimumGreyValue = greyValue;
            }
        }

        if (clImage != clReducedImage) {
            clReducedImage.close();
        }
        return minimumGreyValue;
    }

    public static double sumPixels(CLIJ clij, ClearCLImage clImage) {
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

    public static double sumPixels(CLIJ clij, ClearCLBuffer clImage) {
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

    public static double[] sumPixelsSliceBySlice(CLIJ clij, ClearCLImage input) {
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

    public static double[] sumPixelsSliceBySlice(CLIJ clij, ClearCLBuffer input) {
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

    public static boolean sumZProjection(CLIJ clij, ClearCLImage clImage, ClearCLImage clReducedImage) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImage);
        parameters.put("dst", clReducedImage);
        return clij.execute(Kernels.class, "projections.cl", "sum_project_3d_2d", parameters);
    }

    public static boolean sumZProjection(CLIJ clij, ClearCLBuffer clImage, ClearCLBuffer clReducedImage) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImage);
        parameters.put("dst", clReducedImage);
        return clij.execute(Kernels.class, "projections.cl", "sum_project_3d_2d", parameters);
    }

    public static boolean tenengradWeightsSliceBySlice(CLIJ clij, ClearCLImage clImageOut, ClearCLImage clImageIn) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", clImageIn);
        parameters.put("dst", clImageOut);
        return clij.execute(Kernels.class,"tenengradFusion.cl", "tenengrad_weight_unnormalized_slice_wise", parameters);
    }

    public static boolean tenengradFusion(CLIJ clij, ClearCLImage clImageOut, float[] blurSigmas, ClearCLImage... clImagesIn) {
        return tenengradFusion(clij, clImageOut, blurSigmas, 1.0f, clImagesIn);
    }

    public static boolean tenengradFusion(CLIJ clij, ClearCLImage clImageOut, float[] blurSigmas, float exponent, ClearCLImage... clImagesIn) {
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
                blurFast(clij, temporaryImage2, temporaryImages[i], blurSigmas[0], blurSigmas[1], blurSigmas[2]);
            } else {
                blurFast(clij, temporaryImage, temporaryImages[i], blurSigmas[0], blurSigmas[1], blurSigmas[2]);
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

    public static boolean threshold(CLIJ clij, ClearCLImage src, ClearCLImage dst, Float threshold) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
        }

        return clij.execute(Kernels.class, "thresholding.cl", "apply_threshold_" + src.getDimension() + "d", parameters);
    }

    public static boolean threshold(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst, Float threshold) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.clear();
        parameters.put("threshold", threshold);
        parameters.put("src", src);
        parameters.put("dst", dst);

        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (addImageAndScalar)");
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

