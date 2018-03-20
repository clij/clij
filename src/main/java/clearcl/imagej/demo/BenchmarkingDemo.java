package clearcl.imagej.demo;

import clearcl.ClearCLImage;
import clearcl.imagej.ClearCLIJ;
import clearcontrol.stack.OffHeapPlanarStack;
import fastfuse.tasks.GaussianBlurTask;
import ij.IJ;
import ij.ImagePlus;
import ij.Prefs;
import ij.gui.NewImage;
import ij.plugin.Duplicator;
import ij.plugin.ImageCalculator;
import net.imagej.ImageJ;
import net.imagej.ImageJPlugin;
import net.imagej.ops.OpService;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.view.Views;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.io.IOException;
import java.util.HashMap;

public class BenchmarkingDemo {

    private static ImageJ ij;
    private static ClearCLIJ lCLIJ;

    public static void main(String... args) throws IOException {
        ij = new ImageJ();
        ij.ui().showUI();

        lCLIJ = new ClearCLIJ("HD");

        input = NewImage.createByteImage("input",1024, 1024, 100, NewImage.FILL_RANDOM);
                //IJ.openImage("src/main/resources/flybrain.tif");
        input.show();
        IJ.run(input, "8-bit","");
        img = ImageJFunctions.wrapReal(input);

        long timestamp = System.currentTimeMillis();
        demoImageJ1();
        System.out.println("The ImageJ1 way took " + (System.currentTimeMillis() - timestamp) + " msec");

        timestamp = System.currentTimeMillis();
        demoImageJ2();
        System.out.println("The ImageJ2 way took " + (System.currentTimeMillis() - timestamp) + " msec");

        timestamp = System.currentTimeMillis();
        demoClearCLIJ();
        System.out.println("The ClearCL way took " + (System.currentTimeMillis() - timestamp) + " msec");
    }

    private static ImagePlus input;
    private static Img img;

    private static void demoImageJ1() {
        ImagePlus copy = new Duplicator().run(input, 1, input.getNSlices());
        //copy.show();
        Prefs.blackBackground = false;
        IJ.run(copy,"Gaussian Blur 3D...", "x=3 y=3 z=3");
        IJ.setRawThreshold(copy, 100, 255, null);
        IJ.run(copy, "Convert to Mask", "method=Default background=Dark");
        IJ.run(copy, "Erode", "stack");
        IJ.run(copy, "Dilate", "stack");
        IJ.run(copy, "Divide...", "value=255 stack");
        ImageCalculator calculator = new ImageCalculator();

        ImagePlus result = calculator.run("Multiply create stack", copy, input);
        result.show();
    }

    private static void demoImageJ2() {
        UnsignedByteType threshold = new UnsignedByteType();
        threshold.setReal(100);
        RandomAccessibleInterval gauss = ij.op().filter().gauss(img, 3);
        IterableInterval ii = ij.op().threshold().apply(Views.iterable(gauss), threshold);

        // apparently, there is no erosion/dilation availale in ops

        //IterableInterval multiply = ij.op().math().multiply(img, ii);

    }

    private static void demoClearCLIJ() throws IOException {
        HashMap<String, Object> lParameters = new HashMap<>();

        ClearCLImage input = lCLIJ.converter(img).getClearCLImage();
        ClearCLImage flip = lCLIJ.converter(img).getClearCLImage();
        ClearCLImage flop = lCLIJ.converter(img).getClearCLImage();

        lCLIJ.converter(input).getImagePlus().show();

        lParameters.clear();
        lParameters.put("Nx", 6);
        lParameters.put("Ny", 6);
        lParameters.put("Nz", 6);
        lParameters.put("sx", 3f);
        lParameters.put("sy", 3f);
        lParameters.put("sz", 3f);

        lParameters.put("src", flip);
        lParameters.put("dst", flop);

        lCLIJ.execute(GaussianBlurTask.class, "kernels/blur.cl", "gaussian_blur_image3d", lParameters);

        //lCLIJ.converter(flop).getImagePlus().show();

        lParameters.clear();
        lParameters.put("threshold", 100f);
        lParameters.put("src", flop);
        lParameters.put("dst", flip);
        lCLIJ.execute(BenchmarkingDemo.class, "kernels/thresholding.cl", "applyThreshold", lParameters);

        //lCLIJ.converter(flip).getImagePlus().show();

        lParameters.clear();
        lParameters.put("src", flip);
        lParameters.put("dst", flop);
        lCLIJ.execute(BenchmarkingDemo.class, "kernels/binaryProcessing.cl", "erode_4_neighborhood", lParameters);

        //lCLIJ.converter(flop).getImagePlus().show();

        lParameters.clear();
        lParameters.put("src", flop);
        lParameters.put("dst", flip);
        lCLIJ.execute(BenchmarkingDemo.class, "kernels/binaryProcessing.cl", "dilate_4_neighborhood", lParameters);

        //lCLIJ.converter(flip).getImagePlus().show();

        lParameters.clear();
        lParameters.put("src", flop);
        lParameters.put("src1", input);
        lParameters.put("dst", flip);
        lCLIJ.execute(BenchmarkingDemo.class, "kernels/math.cl", "multiplyPixelwise", lParameters);

        lCLIJ.converter(flip).getImagePlus().show();

        flip.close();
        flop.close();
        input.close();
    }
}
