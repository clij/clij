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
import net.haesleinhuepf.clij.utilities.AffineTransform;
import net.imglib2.realtransform.AffineTransform2D;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_scale")
public class Scale extends Scale3D {

    private static boolean notifiedDeprecated = false;
    @Override
    public boolean executeCL() {
        if (!notifiedDeprecated) {
            IJ.log("CLIJ scale() is deprecated. Use scale2D or scale3D instead.");
            notifiedDeprecated = true;
        }
        return super.executeCL();
    }

    @Override
    public String getDescription() {
        return "DEPRECATED: CLIJ scale() is deprecated. Use scale2D or scale3D instead!" +
                "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }
}
