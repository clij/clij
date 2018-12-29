package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_maximumOfAllPixels")
public class MaximumOfAllPixels extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double maximumGreyValue = 0;
        if (containsCLImageArguments()) {
            maximumGreyValue = Kernels.maximumOfAllPixels(clij, (ClearCLImage)( args[0]));
            } else {
            Object[] args = openCLBufferArgs();
            maximumGreyValue = Kernels.maximumOfAllPixels(clij, (ClearCLBuffer)( args[0]));
            releaseBuffers(args);
        }

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Max", maximumGreyValue);
        table.show("Results");
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the maximum of all pixels in a given image. It will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Max'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
