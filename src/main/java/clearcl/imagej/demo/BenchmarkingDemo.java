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
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.io.IOException;
import java.util.HashMap;

public class BenchmarkingDemo {

    private static ImageJ ij;
    private static ClearCLIJ lCLIJ;
    private static double sigma = 3;

    public static void main(String... args) throws IOException {
        ij = new ImageJ();
        ij.ui().showUI();

        lCLIJ = new ClearCLIJ("HD");

        input = IJ.openImage("src/main/resources/flybrain.tif");
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
        IJ.run(copy,"Gaussian Blur 3D...", "x=" + sigma + " y=" + sigma + " z=" + sigma + "");
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
        //FloatType threshold = new FloatType();

        threshold.setReal(100);
        RandomAccessibleInterval gauss = ij.op().filter().gauss(img, sigma);
        IterableInterval ii = ij.op().threshold().apply(Views.iterable(gauss), threshold);

        // apparently, there is no erosion/dilation availale in ops

        //IterableInterval multiply = ij.op().math().multiply(img, ii);

    }

    private static void demoClearCLIJ() throws IOException {
        HashMap<String, Object> lParameters = new HashMap<>();

        long timestamp = System.currentTimeMillis();
        ClearCLImage input = lCLIJ.converter(img).getClearCLImage();
        ClearCLImage flip = lCLIJ.createCLImage(input.getDimensions(), input.getChannelDataType());
        //ClearCLImage flip = lCLIJ.converter(img).getClearCLImage();
        ClearCLImage flop = lCLIJ.createCLImage(input.getDimensions(), input.getChannelDataType());
        //ClearCLImage flop = lCLIJ.converter(img).getClearCLImage();
        System.out.println("copy took " + (System.currentTimeMillis() - timestamp));

        lParameters.clear();
        lParameters.put("Nx", new Integer((int)(sigma * 3)));
        lParameters.put("Ny", new Integer((int)(sigma * 3)));
        lParameters.put("Nz", new Integer((int)(sigma * 3)));
        lParameters.put("sx", new Float(sigma));
        lParameters.put("sy", new Float(sigma));
        lParameters.put("sz", new Float(sigma));

        lParameters.put("src", input);
        lParameters.put("dst", flop);

        lCLIJ.execute(GaussianBlurTask.class, "kernels/blur.cl", "gaussian_blur_image3d", lParameters);

        //lCLIJ.converter(flop).getImagePlus().show();

        lParameters.clear();
        lParameters.put("threshold", new Float(100));
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
