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
}
