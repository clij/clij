package net.haesleinhuepf.clij.demo;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.imagej.ImageJ;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.img.Img;
import net.imglib2.roi.Regions;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Pair;
import net.imglib2.view.Views;

import java.io.IOException;

/**
 * This class (rather its main method) applies a simple pipeline to a given image. It blurs it, creates a mask by
 * thresholding, does some erosion and dilatation to make the edges smoother and finally sets all background pixels in
 * the original image to zero. This pipeline is executed in the ImageJ1 way and in OpenCL to compare performance and
 * results.
 * <p>
 * Author: Robert Haase (http://haesleinhuepf.net) at MPI CBG (http://mpi-cbg.de)
 * March 2018
 */
public class BenchmarkingDemo {

    private static ImageJ ij;
    private static CLIJ clij;
    private static double sigma = 3;

    private static Img img;

    public static void main(String... args) throws IOException {

        // ---------------------------------------
        // Initialize ImageJ, CLIJ and test image
        ij = new ImageJ();
        ij.ui().showUI();
        clij = CLIJ.getInstance("TITAN");

        img = (Img) ij.io().open("src/main/resources/flybrain.tif");
        ij.ui().show(img);

        for (int i = 0; i < 100; i++) {
            // ---------------------------------------
            // two test scenarios
            long timestamp = System.currentTimeMillis();

            timestamp = System.currentTimeMillis();
            demoImageJ2();
            System.out.println("The ImageJ2 way took " + (System.currentTimeMillis() - timestamp) + " msec");

            timestamp = System.currentTimeMillis();
            demoClearCLIJ();
            System.out.println("The ClearCL way took " + (System.currentTimeMillis() - timestamp) + " msec");
            // ---------------------------------------
        }
    }

    private static <T extends RealType<T>, B extends BooleanType<B>> void demoImageJ2() {

        // Thanks to @imagejan, @tpietzsch and @Awalter from forum.imagej.net

        UnsignedByteType threshold = new UnsignedByteType();
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
        sample.forEach(pair -> pair.getA().set(pair.getB()));

        ij.ui().show(output);

    }

    private static RandomAccessibleInterval makeRai(IterableInterval ii) {
        RandomAccessibleInterval rai;
        if (ii instanceof RandomAccessibleInterval) {
            rai = (RandomAccessibleInterval) ii;
        } else {
            rai = ij.op().create().img(ii);
            ij.op().copy().iterableInterval((Img) rai, ii);
        }
        return rai;
    }

    private static void demoClearCLIJ() throws IOException {
        ClearCLBuffer input = clij.push(img);
        ClearCLBuffer flip = clij.create(input.getDimensions(), input.getNativeType());
        ClearCLBuffer flop = clij.create(input.getDimensions(), input.getNativeType());

        clij.op().blurFast(input, flop, (float) sigma, (float) sigma, (float) sigma);

        clij.op().threshold(flop, flip, 100.0f);

        clij.op().erodeSphere(flip, flop);
        clij.op().dilateSphere(flop, flip);

        clij.op().multiplyImages(flop, input, flip);

        ij.ui().show(clij.pull(flip));

        flip.close();
        flop.close();
        input.close();
    }
}
