package net.haesleinhuepf.clij.macro.documentation;

import ij.plugin.PlugIn;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * OnlineDocumentationOpener
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
public class OnlineDocumentationOpener implements PlugIn {
    @Override
    public void run(String s) {
        try {
            Desktop.getDesktop().browse(new URI(s));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
