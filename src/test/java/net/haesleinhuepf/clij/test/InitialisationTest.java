package net.haesleinhuepf.clij.test;

import net.haesleinhuepf.clij.CLIJ;
import org.jruby.RubyProcess;
import org.junit.Test;

/**
 * InitialisationTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 01 2019
 */
public class InitialisationTest {
    @Test
    public void testInitContext() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("reinit " + i);
            CLIJ clij = CLIJ.getInstance();
            clij.close();
        }
    }
}
