package net.haesleinhuepf.clij.macro.modules;

import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
import ij.Macro;
import ij.WindowManager;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Release
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */

@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_create2D")
public class Create2D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image destination, Number width, Number height, Number bitDepth";
    }

    @Override
    public String getDescription() {
        return "Allocated memory for a new 2D image in the GPU memory. BitDepth must be 8 (unsigned byte), 16 (unsigned short) or 32 (float)." +
                "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        NativeTypeEnum typeEnum = NativeTypeEnum.Float;
        if (asInteger(args[3]) == 8) {
            typeEnum = NativeTypeEnum.UnsignedByte;
        } else if (asInteger(args[3]) == 16) {
            typeEnum = NativeTypeEnum.UnsignedShort;
        }
        return clij.create(new long[]{asInteger(args[1]), asInteger(args[2])}, typeEnum);
    }

}
