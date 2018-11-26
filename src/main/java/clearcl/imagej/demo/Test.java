package clearcl.imagej.demo;

import clearcl.ClearCLImage;
import clearcl.enums.ImageChannelDataType;
import clearcl.imagej.ClearCLIJ;
import clearcontrol.stack.OffHeapPlanarStack;
import coremem.enums.NativeTypeEnum;

public class Test {
    public static void main(String... args) {
        ClearCLIJ clij = ClearCLIJ.getInstance();

        //byte[] test = new byte[512*512*512];
        OffHeapPlanarStack test = new OffHeapPlanarStack(true, 0, NativeTypeEnum.Byte, 1, new long[]{512,512,512});

        long timestamp = System.currentTimeMillis();
        ClearCLImage flip = clij.createCLImage(new long[]{512,512,512}, ImageChannelDataType.UnsignedInt8);
        System.out.println("The create in GPU took " + (System.currentTimeMillis() - timestamp) + " msec");


        timestamp = System.currentTimeMillis();
        flip.readFrom(test.getContiguousMemory(), true);
        System.out.println("The copy to GPU took " + (System.currentTimeMillis() - timestamp) + " msec");


    }
}
