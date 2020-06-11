package net.haesleinhuepf.clij.macro.modules;

import ij.ImagePlus;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 *
 *
 * Author: @haesleinhuepf
 * 12 2018
 */
@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_histogram")
public class Histogram extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {


    @Override
    public boolean executeCL() {
        Integer numberOfBins = asInteger(args[2]);
        Float minimumGreyValue = asFloat(args[3]);
        Float maximumGreyValue = asFloat(args[4]);
        Boolean determineMinMax = asBoolean(args[5]);

        ClearCLBuffer src = (ClearCLBuffer)( args[0]);

        // determine min and max intensity if necessary
        if (determineMinMax) {
            minimumGreyValue = new Double(Kernels.minimumOfAllPixels(clij, src)).floatValue();
            maximumGreyValue = new Double(Kernels.maximumOfAllPixels(clij, src)).floatValue();
        }

        // determine histogram
        Object[] args = openCLBufferArgs();
        boolean result = Kernels.fillHistogram(clij, src, (ClearCLBuffer)(args[1]), minimumGreyValue, maximumGreyValue);
        releaseBuffers(args);

        // the histogram is written in args[1] which is supposed to be a one-dimensional image
        ImagePlus histogramImp = clij.convert((ClearCLBuffer)(args[1]), ImagePlus.class);

        // plot without first eleement
        //histogramImp.setRoi(new Line(1,0.5, histogramImp.getWidth(), 0.5));
        //IJ.run(histogramImp, "Plot Profile", "");

        // plot properly
        float[] determinedHistogram = (float[])(histogramImp.getProcessor().getPixels());
        float[] xAxis = new float[asInteger(args[2])];
        xAxis[0] = minimumGreyValue;
        float step = (maximumGreyValue - minimumGreyValue) / (numberOfBins - 1);

        for (int i = 1 ; i < xAxis.length; i ++) {
            xAxis[i] = xAxis[i-1] + step;
        }
        //new Plot("Histogram", "grey value", "log(number of pixels)", xAxis, determinedHistogram, 0).show();

        // send result to results table
        ResultsTable table = ResultsTable.getResultsTable();
        for (int i = 0 ; i < xAxis.length; i ++) {
            table.incrementCounter();
            table.addValue("Grey value", xAxis[i]);
            table.addValue("Number of pixels", determinedHistogram[i]);
        }
        table.show(table.getTitle());

        return result;
    }


    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number numberOfBins, Number minimumGreyValue, Number maximumGreyValue, Boolean determineMinAndMax";
    }

    @Override
    public String getDescription() {
        return "Determines the histogram of a given image." +
                "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        Integer numberOfBins = asInteger(args[2]);

        return clij.createCLBuffer(new long[]{numberOfBins,1,1},NativeTypeEnum.Float);
    }

}