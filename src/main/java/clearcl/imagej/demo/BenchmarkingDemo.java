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
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.roi.Regions;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Pair;
import net.imglib2.view.Views;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class (rather its main method) applies a simple pipeline to a given image. It blurs it, creates a mask by
 * thresholding, does some erosion and dilatation to make the edges smoother and finally sets all background pixels in
 * the original image to zero. This pipeline is executed in the ImageJ1 way and in OpenCL to compare performance and
 * results.
 *
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * March 2018
 */
public class BenchmarkingDemo {

    private static ImageJ ij;
    private static ClearCLIJ lCLIJ;
    private static double sigma = 3;

    public static void main(String... args) throws IOException {

        // ---------------------------------------
        // Initialize ImageJ, ClearCLIJ and test image
        ij = new ImageJ();
        ij.ui().showUI();
        lCLIJ = new ClearCLIJ("HD");

        input = IJ.openImage("src/main/resources/flybrain.tif");
        input.show();
        IJ.run(input, "8-bit","");
        img = ImageJFunctions.wrapReal(input);

        // ---------------------------------------
        // three test scenarios
        long timestamp = System.currentTimeMillis();
        demoImageJ1();
        System.out.println("The ImageJ1 way took " + (System.currentTimeMillis() - timestamp) + " msec");

        timestamp = System.currentTimeMillis();
        demoImageJ2();
        System.out.println("The ImageJ2 way took " + (System.currentTimeMillis() - timestamp) + " msec");

        timestamp = System.currentTimeMillis();
        demoClearCLIJ();
        System.out.println("The ClearCL way took " + (System.currentTimeMillis() - timestamp) + " msec");
        // ---------------------------------------
    }

    private static ImagePlus input;
    private static Img img;

    private static void demoImageJ1() {
        ImagePlus copy = new Duplicator().run(input, 1, input.getNSlices());
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

    private static <T extends RealType<T>, B extends BooleanType<B>> void demoImageJ2() {

        // Thanks to @imagejan, @tpietscht and @Awalter from forum.imagej.net

        UnsignedByteType threshold = new UnsignedByteType();
        //FloatType threshold = new FloatType();

        threshold.setReal(100);
        RandomAccessibleInterval gauss = ij.op().filter().gauss(img, sigma);

        // Gaussian blur
        IterableInterval blurredIi = ij.op().threshold().apply(Views.iterable(gauss), threshold);
        RandomAccessibleInterval blurredImg = makeRai(blurredIi);

        // erode
        IterableInterval erodedIi = ij.op().morphology().erode(blurredImg, new DiamondShape(1));
        RandomAccessibleInterval erodedImg = makeRai(erodedIi);

        // dilate
        IterableInterval dilatedIi = ij.op().morphology().dilate(erodedImg, new DiamondShape(1));
        RandomAccessibleInterval dilatedImg = makeRai(dilatedIi);

        // mask original image
        Img output = ij.op().create().img(img);
        IterableInterval<Pair<T, T>> sample = Regions.sample(
                Regions.iterable(dilatedImg),
                Views.pair(output, img)
        );
        sample.forEach(pair -> pair.getA().set(pair.getB()) );

        ImageJFunctions.show(output);

    }

    private static RandomAccessibleInterval makeRai(IterableInterval ii) {
        RandomAccessibleInterval rai;
        if (ii instanceof RandomAccessibleInterval) {
            rai = (RandomAccessibleInterval)ii;
        } else {
            rai = ij.op().create().img(ii);
            ij.op().copy().iterableInterval((Img) rai, ii);
        }
        return rai;
    }

    private static void demoClearCLIJ() throws IOException {
        HashMap<String, Object> lParameters = new HashMap<>();

        ClearCLImage input = lCLIJ.converter(img).getClearCLImage();
        ClearCLImage flip = lCLIJ.createCLImage(input.getDimensions(), input.getChannelDataType());
        ClearCLImage flop = lCLIJ.createCLImage(input.getDimensions(), input.getChannelDataType());

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
