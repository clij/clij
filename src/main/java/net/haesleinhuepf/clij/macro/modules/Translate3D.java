package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.imglib2.realtransform.AffineTransform3D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_translate3D")
public class Translate3D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float translateX = -asFloat(args[2]);
        float translateY = -asFloat(args[3]);
        float translateZ = -asFloat(args[4]);

        AffineTransform3D at = new AffineTransform3D();
        Object[] args = openCLBufferArgs();

        at.translate(translateX, translateY, translateZ);

        boolean result = Kernels.affineTransform(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), AffineTransform.matrixToFloatArray(at));
        releaseBuffers(args);
        return result;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number translateX, Number translateY, Number translateZ";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
