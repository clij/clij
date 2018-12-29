package net.haesleinhuepf.imagej.demo;

import ij.IJ;
import ij.macro.ExtensionDescriptor;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.macro.CLIJHandler;
import net.haesleinhuepf.imagej.macro.CLIJMacroPluginService;
import net.imagej.ImageJ;

import java.util.Arrays;

/**
 * ServiceDemo
 * <p>
 * Author: @haesleinhuepf
 * December 2018
 */
public class ServiceDemo {
    public static void main(String... args) {
        ImageJ ij = new ImageJ();
        ij.ui().showUI();
        //new ij.ImageJ();

        CLIJMacroPluginService clijMacroPluginService = ij.get(CLIJMacroPluginService.class);

        for (String name : clijMacroPluginService.getCLIJMethodNames()) {
            System.out.println(name);
        }

        for (ExtensionDescriptor ed : CLIJHandler.getInstance().getExtensionFunctions()) {
            System.out.println(ed.name + " " + Arrays.toString(ed.argTypes));
        }

        //new ImageJ();
        //CLIJMacroExtensions ext = new CLIJMacroExtensions();
        CLIJHandler.getInstance().setCLIJ(ClearCLIJ.getInstance());
        //ext.clij = ClearCLIJ.getInstance("gfx902");
        //ext.getExtensionFunctions();

        IJ.open("c:/structure/code/haesleinhuepf_clearclij/src/main/resources/flybrain.tif");
        CLIJHandler.getInstance().handleExtension("CLIJ_push", new Object[] {"flybrain.tif"});

        IJ.getImage().setTitle("out");
        CLIJHandler.getInstance().handleExtension("CLIJ_push", new Object[] {"out"});

        Object[] arguments = new Object[]{
                "flybrain.tif",
                "out",
                new Double(5),
                new Double(5),
                new Double(5)
        };
        CLIJHandler.getInstance().handleExtension("CLIJ_mean3d", arguments);

        //ext.handleExtension("CLIJ_erode", arguments);

        CLIJHandler.getInstance().handleExtension("CLIJ_pull", new Object[] {"out"});

        CLIJHandler.getInstance().handleExtension("CLIJ_clear", new Object[]{});

    }
}
