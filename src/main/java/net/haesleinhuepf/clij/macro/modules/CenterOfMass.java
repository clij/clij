package net.haesleinhuepf.clij.macro.modules;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.time.temporal.Temporal;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_centerOfMass")
public class CenterOfMass extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double sum = 0;


        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();

        if (containsCLImageArguments() && clij.hasImageSupport()) {
            ClearCLImage input = (ClearCLImage)( args[0]);
            double[] center = clij.op().centerOfMass(input);

            table.addValue("MassX", center[0]);
            table.addValue("MassY", center[1]);
            if (input.getDimension() > 2 && input.getDepth() > 1) {
                table.addValue("MassZ", center[2]);
            }

        } else {
            Object[] args = openCLBufferArgs();
            ClearCLBuffer input = (ClearCLBuffer)( args[0]);

            double[] center = clij.op().centerOfMass(input);

            table.addValue("MassX", center[0]);
            table.addValue("MassY", center[1]);
            if (input.getDimension() > 2 && input.getDepth() > 1) {
                table.addValue("MassZ", center[2]);
            }
            releaseBuffers(args);
        }

        table.show("Results");
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the center of mass of an image or image stack and writes the result in the results table\n" +
                "in the columns MassX, MassY and MassZ." +
                "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
