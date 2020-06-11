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
 * Author: @haesleinhuepf
 * December 2018
 */
@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_copySlice")
public class CopySlice extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments() && clij.hasImageSupport()) {
            return Kernels.copySlice(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), asInteger(args[2]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.copySlice(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asInteger(args[2]));
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number sliceIndex";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        if (input.getDimension() == 2) {
            return clij.createCLBuffer(new long[]{ input.getWidth(), input.getHeight(), asInteger(args[2]) + 1}, input.getNativeType());
        } else  {
            return clij.createCLBuffer(new long[]{ input.getWidth(), input.getHeight()}, input.getNativeType());
        }
    }


    @Override
    public String getDescription() {
        return "This method has two purposes: \n" +
                "It copies a 2D image to a given slice z position in a 3D image stack or \n" +
                "It copies a given slice at position z in an image stack to a 2D image.\n\n" +
                "The first case is only available via ImageJ macro. If you are using it, it is recommended that the \n" +
                "target 3D image already pre-exists in GPU memory before calling this method. Otherwise, CLIJ create \n" +
                "the image stack with z planes." +
                "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D -> 2D and 2D -> 3D";
    }
}
