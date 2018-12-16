package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_blur3dSliceBySlice")
public class Blur3DSliceBySlice extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor {

    @Override
    public boolean executeCL() {
        float sigmaX = asFloat(args[2]);
        float sigmaY = asFloat(args[3]);
        int nX = radiusToKernelSize((int)sigmaX);
        int nY = radiusToKernelSize((int)sigmaY);

        if (containsCLBufferArguments()) {
            // convert all arguments to CLImages
            Object[] args = openCLImageArgs();
            boolean result = Kernels.blurSliceBySlice(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), nX, nY, sigmaX, sigmaY);
            // copy result back to the bufffer
            Kernels.copy(clij, (ClearCLImage)args[1], (ClearCLBuffer)this.args[1]);
            // cleanup
            releaseImages(args);
            return result;
        } else {
            return Kernels.blurSliceBySlice(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), nX, nY, sigmaX, sigmaY);
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number sigmaX, Number sigmaY";
    }
}
