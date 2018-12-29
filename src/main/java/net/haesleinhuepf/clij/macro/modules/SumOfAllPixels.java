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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_sumOfAllPixels")
public class SumOfAllPixels extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double sum = 0;
        if (containsCLImageArguments()) {
            sum = Kernels.sumPixels(clij, (ClearCLImage)( args[0]));
            } else {
            Object[] args = openCLBufferArgs();
            sum = Kernels.sumPixels(clij, (ClearCLBuffer)( args[0]));
            releaseBuffers(args);
        }

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Sum", sum);
        table.show("Results");
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Sum'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
