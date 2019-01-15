package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class Minimum3DBoxTest {
    @Ignore //ignore test as we know and need to accept that the tested method does not do the same its ImageJ counterpart
    @Test
    public void minimum3dSeparable() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        ClearCLImage src = clij.convert(testFlyBrain3D, ClearCLImage.class);;
        ClearCLImage minimumCL = clij.createCLImage(src);
        ClearCLImage minimumSepCL = clij.createCLImage(src);

        Kernels.minimumSphere(clij, src, minimumCL, 7,7, 7);
        Kernels.minimumBox(clij, src, minimumSepCL, 3, 3, 3);

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

        System.out.println("Todo: minimumSphere uses a elipsoid as mask while minimumBox uses a cuboid");
        assertTrue(TestUtilities.compareImages(minimumSepImp, minimumImp, 2.0));
        IJ.exit();
        clij.close();
    }

    @Ignore //ignore test as we know and need to accept that the tested method does not do the same its ImageJ counterpart
    @Test
    public void minimum3dSeparable_Buffer() throws InterruptedException {
        CLIJ clij = CLIJ.getInstance();
        ImagePlus testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");

        ClearCLBuffer src = clij.convert(testFlyBrain3D, ClearCLBuffer.class);
        ClearCLBuffer minimumCL = clij.createCLBuffer(src);
        ClearCLBuffer minimumSepCL = clij.createCLBuffer(src);

        Kernels.minimumSphere(clij, src, minimumCL, 3,3, 3);
        Kernels.minimumBox(clij, src, minimumSepCL, 1, 1, 1);

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

        System.out.println("Todo: minimumSphere uses a elipsoid as mask while minimumBox uses a cuboid");
        assertTrue(TestUtilities.compareImages(minimumSepImp, minimumImp, 2.0));
        IJ.exit();
        clij.close();
    }

}