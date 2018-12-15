package net.haesleinhuepf.imagej.macro;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.macro.ExtensionDescriptor;
import ij.macro.Functions;
import ij.macro.MacroExtension;
import net.haesleinhuepf.imagej.ClearCLIJ;
import org.apache.commons.lang3.math.NumberUtils;
import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * CLIJMacroExtensions
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 11 2018
 */
@Plugin(type = Command.class, menuPath = "Plugins>CLIJ>CLIJ Macro Extensions")
public class CLIJMacroExtensions implements Command {

    /*
    public static void main(String... args) {
        new ImageJ();
        CLIJMacroExtensions ext = new CLIJMacroExtensions();
        ext.clij = ClearCLIJ.getInstance("gfx902");
        ext.getExtensionFunctions();

        IJ.open("C:/structure/code/haesleinhuepf_clearclij/src/main/resources/flybrain.tif");
        ext.pushToGPU("flybrain.tif");
        IJ.getImage().setTitle("out");
        ext.pushToGPU("out");

        Object[] arguments = new Object[]{
                "flybrain.tif",
                "out",
                new Double(3),
                new Double(3),
                new Double(1)
        };
        ext.handleExtension("CLIJ_mean3d", arguments);
        //ext.handleExtension("CLIJ_erode", arguments);

        ext.pullFromGPU("out");

        ext.handleExtension("CLIJ_clear", new Object[]{});
    }*/

    static int radiusToKernelSize(int radius) {
        int kernelSize = radius * 2 + 1;
        return kernelSize;
    }

    @Override
    public void run() {
        ArrayList<String> deviceList = ClearCLIJ.getAvailableDeviceNames();
        String[] deviceArray = new String[deviceList.size()];
        deviceList.toArray(deviceArray);

        GenericDialog gd = new GenericDialog("CLIJ");
        gd.addChoice("CL_Device", deviceArray, deviceArray[0]);
        gd.showDialog();

        if (gd.wasCanceled()) {
            return;
        }
        CLIJHandler.getInstance().setCLIJ(ClearCLIJ.getInstance(gd.getNextChoice()));

        if (!IJ.macroRunning()) {
            IJ.error("Cannot install extensions from outside a macro.");
            return;
        }
        Functions.registerExtensions(CLIJHandler.getInstance());
    }


    private boolean isNumeric(Object text) {
        if (text instanceof Double) {
            return true;
        }
        return NumberUtils.isNumber((String) text);
    }

}
