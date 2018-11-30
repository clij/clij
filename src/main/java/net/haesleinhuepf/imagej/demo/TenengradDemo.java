package net.haesleinhuepf.imagej.demo;

import clearcl.ClearCLImage;
import clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;

/**
 * The TenengradDemo loads an image stack containing five images of the same scene. It fuses the first four of them via
 * Tenengrad fusion, which was taken from FastFuse:
 * https://github.com/ClearControl/FastFuse
 *
 * Author: @haesleinhuepf
 * 06 2018
 */
public class TenengradDemo {
    public static void main(String... args) {
        new ImageJ();

        String folder = "C:/structure/data/Stack50PlanesDeltaZ2/";
        String inputFile = "Image 21.tif";

        // ------------------------------------------------------------------------
        // Maximum projection using ImageJ stuff
        {
            ImagePlus imp = IJ.openImage(folder + inputFile);
            IJ.run(imp,"Stack to Hyperstack...", "order=xyczt(default) channels=1 slices=5 frames=" + (imp.getNChannels() * imp.getNSlices() * imp.getNFrames() / 5) + " display=Color");

            imp = new Duplicator().run(imp, 1, 1,1, 4, 1, imp.getNFrames());
            imp.show();


            IJ.run(imp, "Z Project...", "projection=[Max Intensity] all");
            IJ.saveAsTiff(IJ.getImage(), folder + "maximum_projection.tif");

            //IJ.run("Close All");
        }

        // ------------------------------------------------------------------------
        // Tenengrad fusion on GPU
        {
            ImagePlus imp = IJ.openImage(folder + inputFile);

            ClearCLIJ clij = ClearCLIJ.getInstance();
            ClearCLImage src = clij.converter(imp).getClearCLImage();

            long[] targetSize = new long[]{src.getWidth(), src.getHeight(), src.getDepth() / 5};

            ClearCLImage dst0 = clij.createCLImage(targetSize, ImageChannelDataType.Float);
            ClearCLImage dst1 = clij.createCLImage(targetSize, ImageChannelDataType.Float);
            ClearCLImage dst2 = clij.createCLImage(targetSize, ImageChannelDataType.Float);
            ClearCLImage dst3 = clij.createCLImage(targetSize, ImageChannelDataType.Float);
            ClearCLImage dst4 = clij.createCLImage(targetSize, ImageChannelDataType.Float);

            Kernels.splitStack(clij, src, dst0, dst1, dst2, dst3, dst4);

            // save input data individually
            clij.show(dst0, "light sheet 0");
            IJ.saveAsTiff(IJ.getImage(), folder + "light_sheets_0.tif");
            clij.show(dst1, "light sheet 1");
            IJ.saveAsTiff(IJ.getImage(), folder + "light_sheets_1.tif");
            clij.show(dst2, "light sheet 2");
            IJ.saveAsTiff(IJ.getImage(), folder + "light_sheets_2.tif");
            clij.show(dst3, "light sheet 3");
            IJ.saveAsTiff(IJ.getImage(), folder + "light_sheets_3.tif");

            // save hardware fused stack
            clij.show(dst4, "all light sheets on");
            IJ.saveAsTiff(IJ.getImage(), folder + "all_light_sheets_on.tif");

            // tenengrad fusion
            ClearCLImage result = clij.createCLImage(targetSize, src.getChannelDataType()); // re-using memory; result should not be closed later on

            float[] sigmas = new float[]
                    { 15, 15, 5 };

            Kernels.tenengradFusion(clij, result, sigmas, dst0, dst1, dst2);
            clij.show(result, "result");
            IJ.saveAsTiff(IJ.getImage(), folder + "tenengrad_fusion.tif");

            // cleanup memory
            src.close();
            dst0.close();
            dst1.close();
            dst2.close();
            dst3.close();
            dst4.close();
            result.close();
        }
    }
}
