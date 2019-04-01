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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_centerOfMass")
public class CenterOfMass extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double sum = 0;


        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();

        if (containsCLImageArguments()) {
            ClearCLImage input = (ClearCLImage)( args[0]);

            ClearCLImage multipliedWithCoordinate = clij.create(input.getDimensions(), ImageChannelDataType.Float);
            sum = clij.op().sumPixels(input);


            // X:
            clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 0);
            double sumX = clij.op().sumPixels(multipliedWithCoordinate);
            table.addValue("MassX", sumX / sum);

            // Y:
            clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 1);
            double sumY = clij.op().sumPixels(multipliedWithCoordinate);
            table.addValue("MassY", sumY / sum);

            // Z:
            if (input.getDimension() > 2 && input.getDepth() > 1) {
                clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 2);
                double sumZ = clij.op().sumPixels(multipliedWithCoordinate);
                table.addValue("MassZ", sumZ / sum);
            }
            multipliedWithCoordinate.close();

        } else {
            Object[] args = openCLBufferArgs();
            ClearCLBuffer input = (ClearCLBuffer)( args[0]);

            ClearCLBuffer multipliedWithCoordinate = clij.create(input.getDimensions(), NativeTypeEnum.Float);
            sum = clij.op().sumPixels(input);


            // X:
            clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 0);
            double sumX = clij.op().sumPixels(multipliedWithCoordinate);
            table.addValue("MassX", sumX / sum);

            // Y:
            clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 1);
            double sumY = clij.op().sumPixels(multipliedWithCoordinate);
            table.addValue("MassY", sumY / sum);

            // Z:
            if (input.getDimension() > 2 && input.getDepth() > 1) {
                clij.op().multiplyImageAndCoordinate(input, multipliedWithCoordinate, 2);
                double sumZ = clij.op().sumPixels(multipliedWithCoordinate);
                table.addValue("MassZ", sumZ / sum);
            }
            multipliedWithCoordinate.close();
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
        return "Determines the center of mass of an image or image stack.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
