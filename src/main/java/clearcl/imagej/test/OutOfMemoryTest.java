package clearcl.imagej.test;

import clearcl.ClearCLImage;
import clearcl.enums.ImageChannelDataType;
import clearcl.imagej.ClearCLIJ;
import clearcl.imagej.kernels.Kernels;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

/**
 * OutOfMemoryTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 09 2018
 */
public class OutOfMemoryTest {
    @Ignore
    @Test
    public void runOutOfMemory() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
            ClearCLImage image1 = clij.createCLImage(new long[]{1024, 1024, 50}, ImageChannelDataType.Float);
            ClearCLImage image2 = clij.createCLImage(new long[]{1024, 1024, 50}, ImageChannelDataType.Float);
            //ClearCLImage image3 = clij.createCLImage(new long[]{1024, 1024, 50}, ImageChannelDataType.Float);

            for (int j = 0; j < 1000; j++) {

                System.out.println(">" + j);
              ClearCLImage image4 = clij.createCLImage(new long[]{1024, 1024, 2000}, ImageChannelDataType.Float);
              ClearCLImage image5 = clij.createCLImage(new long[]{1024, 1024, 2000}, ImageChannelDataType.Float);

              ClearCLImage image3 = clij.createCLImage(new long[]{1024, 1024, 50}, ImageChannelDataType.Float);

              //Kernels.tenengradFusion(clij, image1, new float[]{15, 15, 5}, image2, image3);
              Kernels.blurSeparable(clij, image1,image3, new float[]{15,15,5});
              /*HashMap<String, Object> lParameters = new HashMap<>();

              lParameters.clear();
              lParameters.put("N", 6);
              lParameters.put("s", 3f);
              lParameters.put("dim", 0);
              lParameters.put("src", image1);
              lParameters.put("dst", image2);
              clij.execute(Kernels.class,
                           "blur.cl",
                           "gaussian_blur_sep_image3d",
                           lParameters);
*/
              image3.close();
              image4.close();
              image5.close();
            }

            //Kernels.sumPixels(clij, image);
            image1.close();
            image2.close();
            //image3.close();
            //image4.close();
            //image5.close();
        }


    }
}
