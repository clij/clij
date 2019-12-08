package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.imglib2.realtransform.AffineTransform2D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_translate2D")
public class Translate2D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        float translateX = -asFloat(args[2]);
        float translateY = -asFloat(args[3]);

        AffineTransform2D at = new AffineTransform2D();
        Object[] args = openCLBufferArgs();

        at.translate(translateX, translateY);

        //boolean result = Kernels.affineTransform(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), AffineTransform.matrixToFloatArray(at));
        //releaseBuffers(args);
        //return result;
        if (!clij.hasImageSupport()) {
            ClearCLBuffer input = ((ClearCLBuffer) args[0]);
            ClearCLBuffer output = ((ClearCLBuffer) args[1]);

            return Kernels.affineTransform2D(clij, input, output, net.haesleinhuepf.clij.utilities.AffineTransform.matrixToFloatArray2D(at));
        } else {

            ClearCLImage input = CLIJHandler.getInstance().getChachedImageByBuffer((ClearCLBuffer) args[0]);
            ClearCLImage output = CLIJHandler.getInstance().getChachedImageByBuffer((ClearCLBuffer) args[1]);

            boolean result = Kernels.affineTransform2D(clij, input, output, net.haesleinhuepf.clij.utilities.AffineTransform.matrixToFloatArray2D(at));

            Kernels.copy(clij, output, (ClearCLBuffer) args[1]);

            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number translateX, Number translateY";
    }

    @Override
    public String getDescription() {
        return "Translate an image stack in X and Y.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
