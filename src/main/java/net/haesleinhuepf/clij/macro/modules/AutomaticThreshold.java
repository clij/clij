package net.haesleinhuepf.clij.macro.modules;

import ij.ImagePlus;
import ij.process.AutoThresholder;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.Arrays;

/**
 * AutomaticThreshold
 * <p>
 * Author: @haesleinhuepf
 * January 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_automaticThreshold")
public class AutomaticThreshold extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer src = (ClearCLBuffer) (args[0]);
        ClearCLBuffer dst = (ClearCLBuffer) (args[1]);
        String userSelectedMethod = (String)args[2];

        return Kernels.automaticThreshold(clij, src, dst, userSelectedMethod);
    }


    @Override
    public String getDescription() {
        StringBuilder doc = new StringBuilder();
        doc.append("The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on \n" +
                "the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one \n" +
                "of these methods in the method text field:\n" +
                Arrays.toString(AutoThresholder.getMethods()) );
        return doc.toString();
    }


    @Override
    public String getParameterHelpText() {
        return "Image input, Image destination, String method";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
