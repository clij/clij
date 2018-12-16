package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.*;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_help")
public class Help extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor {

    @Override
    public boolean executeCL() {
        CLIJMacroPluginService pluginService = CLIJHandler.getInstance().getPluginService();
        String searchString = "";
        if (args.length > 0) {
            searchString = (String) (args[0]);
        }
        ArrayList<String> helpList = new ArrayList<String>();

        for (String name : pluginService.getCLIJMethodNames()) {
            if (searchString.length() == 0 || name.contains(searchString)) {

                helpList.add(name + "(" + pluginService.clijMacroPlugin(name).getParameterHelpText() + ")");
                //IJ.log(key + "(" + methodMap.get(key).parameters + ")");
            }
        }

        IJ.log("Found " + helpList.size() + " method(s) containing the pattern \"" + searchString + "\":");
        Collections.sort(helpList);
        for (String entry : helpList) {
            IJ.log("Ext." + entry + ";");
        }
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "String searchFor";
    }
}
