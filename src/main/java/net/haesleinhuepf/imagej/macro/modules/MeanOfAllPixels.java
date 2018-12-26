package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.measure.ResultsTable;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_meanOfAllPixels")
public class MeanOfAllPixels extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor {

    @Override
    public boolean executeCL() {
        double sum = 0;
        double numberOfPixels = 0;
        if (containsCLImageArguments()) {
            ClearCLImage image = (ClearCLImage)( args[0]);
            sum = Kernels.sumPixels(clij, image);
            numberOfPixels = image.getWidth() * image.getHeight() * image.getDepth();

            } else {
            Object[] args = openCLBufferArgs();
            ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);
            sum = Kernels.sumPixels(clij, buffer);
            numberOfPixels = buffer.getWidth() * buffer.getHeight() * buffer.getDepth();
            releaseBuffers(args);
        }

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Mean", sum / numberOfPixels);
        table.show("Results");
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }
}
