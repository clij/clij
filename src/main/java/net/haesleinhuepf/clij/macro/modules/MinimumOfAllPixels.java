package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_minimumOfAllPixels")
public class MinimumOfAllPixels extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double minimumGreyValue = 0;
        if (containsCLImageArguments()) {
            minimumGreyValue = Kernels.minimumOfAllPixels(clij, (ClearCLImage)( args[0]));
            } else {
            Object[] args = openCLBufferArgs();
            minimumGreyValue = Kernels.minimumOfAllPixels(clij, (ClearCLBuffer)( args[0]));
            releaseBuffers(args);
        }

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Min", minimumGreyValue);
        table.show("Results");
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the minimum of all pixels in a given image. It will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Min'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
