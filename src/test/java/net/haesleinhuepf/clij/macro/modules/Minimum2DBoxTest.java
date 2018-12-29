package net.haesleinhuepf.clij.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class Minimum2DBoxTest {

    @Test
    public void minimum2dSeparable() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        ImagePlus testFlyBrain2D = new Duplicator().run(testFlyBrain3D, 20, 20);
        ClearCLImage src = clij.convert(testFlyBrain2D, ClearCLImage.class);
        ClearCLImage minimumCL = clij.createCLImage(src);
        ClearCLImage minimumSepCL = clij.createCLImage(src);

        Kernels.minimum(clij, src, minimumCL, 7,7);
        Kernels.minimumSeparable(clij, src, minimumSepCL, 3, 3, 3);

        ImagePlus minimumImp = clij.convert(minimumCL, ImagePlus.class);
        ImagePlus minimumSepImp = clij.convert(minimumSepCL, ImagePlus.class);

        // ignore edges
        minimumImp.setRoi(new Roi(1, 1, minimumImp.getWidth() - 2, minimumImp.getHeight() - 2));
        minimumSepImp.setRoi(new Roi(1, 1, minimumSepImp.getWidth() - 2, minimumSepImp.getHeight() - 2));
        minimumImp = new Duplicator().run(minimumImp);
        minimumSepImp = new Duplicator().run(minimumSepImp);

        src.close();
        minimumCL.close();
        minimumSepCL.close();

        assertTrue(TestUtilities.compareImages(minimumSepImp, minimumImp, 2.0));
    }

    @Test
    public void minimum2dSeparable_Buffer() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        ImagePlus testFlyBrain2D = new Duplicator().run(testFlyBrain3D, 20, 20);
        ClearCLBuffer src = clij.convert(testFlyBrain2D, ClearCLBuffer.class);
        ClearCLBuffer minimumCL = clij.createCLBuffer(src);
        ClearCLBuffer minimumSepCL = clij.createCLBuffer(src);

        Kernels.minimum(clij, src, minimumCL, 3,3);
        Kernels.minimumSeparable(clij, src, minimumSepCL, 1, 1, 1);

        ImagePlus minimumImp = clij.convert(minimumCL, ImagePlus.class);
        ImagePlus minimumSepImp = clij.convert(minimumSepCL, ImagePlus.class);


        // ignore edges
        minimumImp.setRoi(new Roi(1, 1, minimumImp.getWidth() - 2, minimumImp.getHeight() - 2));
        minimumSepImp.setRoi(new Roi(1, 1, minimumSepImp.getWidth() - 2, minimumSepImp.getHeight() - 2));
        minimumImp = new Duplicator().run(minimumImp);
        minimumSepImp = new Duplicator().run(minimumSepImp);

        src.close();
        minimumCL.close();
        minimumSepCL.close();

        assertTrue(TestUtilities.compareImages(minimumSepImp, minimumImp, 2.0));
    }

}