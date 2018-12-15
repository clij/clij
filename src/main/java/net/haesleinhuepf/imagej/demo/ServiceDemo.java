package net.haesleinhuepf.imagej.demo;

import net.haesleinhuepf.imagej.macro.CLIJMacroPluginService;
import net.imagej.ImageJ;

/**
 * ServiceDemo
 * <p>
 * Author: @haesleinhuepf
 * December 2018
 */
public class ServiceDemo {
    public static void main(String... args) {
        ImageJ ij = new ImageJ();

        CLIJMacroPluginService clijMacroPluginService = ij.get(CLIJMacroPluginService.class);

        for (String name : clijMacroPluginService.getCLIJMethodNames()) {
            System.out.println(name);
        }

    }
}
