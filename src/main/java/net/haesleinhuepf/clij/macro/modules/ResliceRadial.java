package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * ResliceRadial
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_resliceRadial")
public class ResliceRadial extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments() && clij.hasImageSupport()) {
            return Kernels.radialProjection(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asFloat(args[3]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.radialProjection(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[3]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number numberOfAngles, Number angleStepSize";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        int numberOfAngles = asInteger(args[2]);
        float angleStepSize = asFloat(args[3]);
        int effectiveNumberOfAngles = (int)((float)numberOfAngles / angleStepSize);
        int maximumRadius = (int)Math.sqrt(Math.pow(input.getWidth() / 2, 2) + Math.pow(input.getHeight() / 2, 2));
        return clij.createCLBuffer(new long[]{maximumRadius, input.getDepth(), effectiveNumberOfAngles}, input.getNativeType());
    }

    @Override
    public String getDescription() {
        return "Computes a radial projection of an image stack. Starting point for the line is the center in any \n" +
                "X/Y-plane of a given input image stack. " +
                "This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }

}
