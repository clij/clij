package net.haesleinhuepf.clij.utilities;

import ij.IJ;
import ij.plugin.PlugIn;
import net.haesleinhuepf.clij.CLIJ;
import org.scijava.plugin.PluginInfo;

/**
 * DebugToggle is an ImageJ plugin which can turn on/off CLIJs debug mode.
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 01 2019
 */
public class DebugToggle implements PlugIn {
    @Override
    public void run(String s) {
        CLIJ.debug = !CLIJ.debug;
        IJ.log("CLIJ debug is " + (CLIJ.debug?"ON":"OFF") + " now.");
    }
}
