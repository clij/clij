package net.haesleinhuepf.clij.test;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;
import ij.*;
import ij.gui.NewImage;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import ij.plugin.ImageCalculator;
import ij.process.ImageProcessor;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
 * Test class for all OpenCL kernels accessible via the Kernels class
 *
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * March 2018
 */
public class KernelsTest {

    ImagePlus testFlyBrain3D;
    ImagePlus testFlyBrain2D;
    ImagePlus testImp1;
    ImagePlus testImp2;
    ImagePlus testImp2D1;
    ImagePlus testImp2D2;
    ImagePlus mask3d;
    ImagePlus mask2d;
    CLIJ clij;

    @Before
    public void initTest() {
        testFlyBrain3D = IJ.openImage("src/main/resources/flybrain.tif");
        testFlyBrain2D = new Duplicator().run(testFlyBrain3D, 1, 1);


        testImp1 =
                NewImage.createImage("",
                        100,
                        100,
                        12,
                        16,
                        NewImage.FILL_BLACK);
        testImp2 =
                NewImage.createImage("",
                        100,
                        100,
                        12,
                        16,
                        NewImage.FILL_BLACK);
        mask3d =
                NewImage.createImage("",
                        100,
                        100,
                        12,
                        16,
                        NewImage.FILL_BLACK);

        for (int z = 0; z < 5; z++) {
            testImp1.setZ(z + 1);
            ImageProcessor ip1 = testImp1.getProcessor();
            ip1.set(5, 5, 1);
            ip1.set(6, 6, 1);
            ip1.set(7, 7, 1);

            testImp2.setZ(z + 1);
            ImageProcessor ip2 = testImp2.getProcessor();
            ip2.set(7, 5, 2);
            ip2.set(6, 6, 2);
            ip2.set(5, 7, 2);

            if (z < 3) {
                mask3d.setZ(z + 3);
                ImageProcessor ip3 = mask3d.getProcessor();
                ip3.set(2, 2, 1);
                ip3.set(2, 3, 1);
                ip3.set(2, 4, 1);
                ip3.set(3, 2, 1);
                ip3.set(3, 3, 1);
                ip3.set(3, 4, 1);
                ip3.set(4, 2, 1);
                ip3.set(4, 3, 1);
                ip3.set(4, 4, 1);
            }
        }

        testImp2D1 = new Duplicator().run(testImp1, 1, 1);
        testImp2D2 = new Duplicator().run(testImp1, 1, 1);
        mask2d = new Duplicator().run(mask3d, 3, 3);

        if (clij == null) {
            clij = CLIJ.getInstance();
            //new CLIJ("Geforce");
            //new CLIJ("HD");
        }
    }

    @Test
    public void differenceOfGaussian3d() {
        // do operation with ImageJ
        System.out.println("Todo: implement test for DoG");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp1, ClearCLImage.class);

        Kernels.differenceOfGaussian(clij, src, dst, 6, 1.1f, 3.3f);

        src.close();
        dst.close();
    }

    @Test
    public void differenceOfGaussian3dSliceBySlice() {
        // do operation with ImageJ
        System.out.println("Todo: implement test for DoG slice by slice");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp1, ClearCLImage.class);

        Kernels.differenceOfGaussianSliceBySlice(clij, src, dst, 6, 1.1f, 3.3f);

        src.close();
        dst.close();
    }


    @Test
    public void differenceOfGaussian2d() {
        // do operation with ImageJ
        System.out.println("Todo: implement test for DoG");

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        ClearCLImage dst = clij.convert(testImp2D1, ClearCLImage.class);

        Kernels.differenceOfGaussian(clij, src, dst, 6, 1.1f, 3.3f);

        src.close();
        dst.close();
    }


    @Test
    public void splitStack() {
        ClearCLImage clearCLImage = clij.convert(testFlyBrain3D, ClearCLImage.class);;
        ClearCLImage split1 = clij.createCLImage(new long[]{clearCLImage.getWidth(), clearCLImage.getHeight(), clearCLImage.getDepth() / 2}, clearCLImage.getChannelDataType());
        ClearCLImage split2 = clij.createCLImage(new long[]{clearCLImage.getWidth(), clearCLImage.getHeight(), clearCLImage.getDepth() / 2}, clearCLImage.getChannelDataType());

        Kernels.splitStack(clij, clearCLImage, split1, split2);

        assertTrue(Kernels.sumPixels(clij, split1) > 0);
        assertTrue(Kernels.sumPixels(clij, split2) > 0);
    }

    @Test
    public void splitStack_Buffers() {
        ClearCLBuffer clearCLImage = clij.convert(testFlyBrain3D, ClearCLBuffer.class);
        ClearCLBuffer split1 = clij.createCLBuffer(new long[]{clearCLImage.getWidth(), clearCLImage.getHeight(), clearCLImage.getDepth() / 2}, clearCLImage.getNativeType());
        ClearCLBuffer split2 = clij.createCLBuffer(new long[]{clearCLImage.getWidth(), clearCLImage.getHeight(), clearCLImage.getDepth() / 2}, clearCLImage.getNativeType());

        Kernels.splitStack(clij, clearCLImage, split1, split2);

        assertTrue(Kernels.sumPixels(clij, split1) > 0);
        assertTrue(Kernels.sumPixels(clij, split2) > 0);
    }

    @Test
    public void tenengradWeights() {
        System.out.println("Todo: implement test for Tenengrad weights");
    }

    @Test
    public void tenengradFusion() {
        System.out.println("Todo: implement test for Tenengrad fusion");
    }

}