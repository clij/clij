package net.haesleinhuepf.clij.test;

import ij.IJ;
import net.haesleinhuepf.clij.CLIJ;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * CLDeviceSwitchTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 01 2019
 */
public class CLDeviceSwitchTest {
    @Test
    public void testDeviceSwitching() {
        CLIJ.debug = true;

        ArrayList<String> availableDeviceNames = CLIJ.getAvailableDeviceNames();
        CLIJ fromBefore = null;
        for (String deviceName : availableDeviceNames) {
            CLIJ clij = CLIJ.getInstance(deviceName);
            assertTrue(clij != fromBefore);
            fromBefore = clij;
        }
        CLIJ.getInstance().close();
    }
}
