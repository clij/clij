package net.haesleinhuepf.clij.macro.modules;

import ij.IJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.realtransform.RealTransformSequence;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_affineTransform")
public class AffineTransform extends net.haesleinhuepf.clij.macro.modules.AffineTransform3D {

    private static boolean notifiedDeprecated = false;
    @Override
    public boolean executeCL() {
        if (!notifiedDeprecated) {
            IJ.log("CLIJ affineTransform is deprecated. Use affineTransform2D or affineTransform3D instead.");
            notifiedDeprecated = true;
        }
        return super.executeCL();
    }

    @Override
    public String getDescription() {
        return "CLIJ affineTransform is deprecated. Use affineTransform2D or affineTransform3D instead.\n\n" + super.getDescription() + "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
