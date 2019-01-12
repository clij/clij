package net.haesleinhuepf.clij.test;

import ij.IJ;
import net.haesleinhuepf.clij.CLIJ;
import org.jruby.RubyProcess;
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

    @Test
    public void testContextInitialisation() {
        for( int i = 0; i < 100; i++) {
            System.out.println(i);
            CLIJ clij = new CLIJ(0);
            clij.close();
        }
    }

    @Test
    public void testGetCLDeviceList() {
        for (String name : CLIJ.getAvailableDeviceNames()) {
            System.out.println(name);
        }

        //CLIJ clij1 = new CLIJ(0);
        //CLIJ clij2 = new CLIJ(0);

        CLIJ clij = CLIJ.getInstance();
        clij.close();
        CLIJ.getInstance();
    }
}
