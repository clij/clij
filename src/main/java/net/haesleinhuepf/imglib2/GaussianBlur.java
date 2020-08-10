package net.haesleinhuepf.imglib2;

import ij.IJ;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJImglib2Processor;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.view.Views;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "Imglib2_gaussianBlur")
public class GaussianBlur extends AbstractCLIJPlugin implements CLIJImglib2Processor {
    @Override
    public boolean executeImglib2() {
        RandomAccessibleInterval rai_in = (RandomAccessibleInterval) args[0];
        RandomAccessibleInterval rai_out = (RandomAccessibleInterval) args[1];


        Float sigma_x = asFloat(args[2]);
        Float sigma_y = asFloat(args[3]);
        Float sigma_z = asFloat(args[4]);

        double[] sigmas = new double[rai_in.numDimensions()];
        long[] border = new long[rai_in.numDimensions()];
        sigmas[0] = sigma_x;
        border[0] = (long) (sigma_x * 4 + 1);
        sigmas[1] = sigma_y;
        border[1] = (long) (sigma_y * 4 + 1);
        if (sigmas.length > 2) {
            sigmas[2] = sigma_z;
            border[2] = (long) (sigma_z * 4 + 1);
        }

        Gauss3.gauss(sigmas, Views.expandBorder(rai_in, border), rai_out);

        return true;
    }

    @Override
    public RandomAccessibleInterval create() {
        RandomAccessibleInterval rai = (RandomAccessibleInterval) args[0];
        long[] dimensions = new long[rai.numDimensions()];
        rai.dimensions(dimensions);
        return ArrayImgs.floats(dimensions);
    }

    @Override
    public String getParameterHelpText() {
        return "Image input, ByRef Image destination, Number sigma_x, Number sigma_y, Number sigma_z";
    }
}
