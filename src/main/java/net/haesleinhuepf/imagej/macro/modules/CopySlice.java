package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.imagej.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_copySlice")
public class CopySlice extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
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
            return clij.createCLBuffer(new long[]{ input.getWidth(), input.getHeight(), asInteger(args[2])}, input.getNativeType());
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
                "the image stack with z planes.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D -> 2D and 2D -> 3D";
    }
}
