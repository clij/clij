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
import net.haesleinhuepf.imagej.converters.CLIJConverterService;
import org.apache.commons.lang3.math.NumberUtils;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
@Plugin(type = Command.class, menuPath = "Plugins>ImageJ on GPU (CLIJ)>CLIJ Macro Extensions")
public class CLIJMacroExtensions implements Command {

    @Parameter
    CLIJConverterService clijConverterService;

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

        // macro extensions and converters must use the same CLIJ instance in order to make everything run on the same GPU.
        ClearCLIJ clij = ClearCLIJ.getInstance(gd.getNextChoice());
        CLIJHandler.getInstance().setCLIJ(clij);
        clijConverterService.setCLIJ(clij);
        clij.setConverterService(clijConverterService);

        if (!IJ.macroRunning()) {
            IJ.error("Cannot install extensions from outside a macro.");
            return;
        }
        Functions.registerExtensions(CLIJHandler.getInstance());
    }
}
