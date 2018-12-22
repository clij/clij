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
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_blur3D")
public class Blur3D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor {

    @Override
    public boolean executeCL() {
        float sigmaX = asFloat(args[2]);
        float sigmaY = asFloat(args[3]);
        float sigmaZ = asFloat(args[4]);
        int nX = radiusToKernelSize((int)sigmaX);
        int nY = radiusToKernelSize((int)sigmaY);
        int nZ = radiusToKernelSize((int)sigmaZ);

        if (containsCLImageArguments()) {
            return Kernels.blur(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), nX, nY, nZ, sigmaX, sigmaY, sigmaZ);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.blur(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), nX, nY, nZ, sigmaX, sigmaY, sigmaZ);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number sigmaX, Number sigmaY, Number sigmaZ";
    }
}
