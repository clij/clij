package net.haesleinhuepf.clij.macro.modules;

import ij.IJ;
import net.haesleinhuepf.clij.macro.*;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_help")
public class Help extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        CLIJMacroPluginService pluginService = CLIJHandler.getInstance().getPluginService();
        String searchString = "";
        if (args.length > 0) {
            searchString = (String) (args[0]);
        }
        searchString = searchString.toLowerCase();
        ArrayList<String> helpList = new ArrayList<String>();

        for (String name : pluginService.getCLIJMethodNames()) {
            if (searchString.length() == 0 || name.toLowerCase().contains(searchString)) {

                helpList.add(name + "(" + pluginService.getCLIJMacroPlugin(name).getParameterHelpText() + ")");
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

    @Override
    public String getDescription() {
        return "Searches in the list of CLIJ commands for a given pattern. Lists all commands in case\"\" is handed\n" +
                "over as parameter." +
                "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "-";
    }
}
