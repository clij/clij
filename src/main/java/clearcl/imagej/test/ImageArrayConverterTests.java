package clearcl.imagej.test;

import clearcl.ClearCLImage;
import clearcl.imagej.ClearCLIJ;
import clearcontrol.stack.StackInterface;
import ij.ImageJ;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * ImageArrayConverterTests
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 07 2018
 */
public class ImageArrayConverterTests {

    int[][][] intTestImage =
        {
            {
                    {0,1,2},
                    {3,4,5}
            },{
                    {6,7,8},
                    {9,10,11}
            },{
                    {12,13,14},
                    {15,16,17}
            },{
                    {18,19,20},
                    {21,22,23}
            }
        };
    ClearCLIJ clij = ClearCLIJ.getInstance();


    @Test
    public void testConvertIntStack() {

        StackInterface stack = clij.converter(intTestImage).getOffHeapPlanarStack();
        int[][][] backConvertedInt = clij.converter(stack).getInt3DArray();

        assertTrue(TestUtilities.compareArrays(intTestImage, backConvertedInt));
    }

    @Test
    public void testConvertIntDouble() {

        double[][][] array = clij.converter(intTestImage).getDouble3DArray();
        int[][][] backConvertedInt = clij.converter(array).getInt3DArray();

        assertTrue(TestUtilities.compareArrays(intTestImage, backConvertedInt));
    }

    @Test
    public void testConvertIntFloat() {

        float[][][] array = clij.converter(intTestImage).getFloat3DArray();
        int[][][] backConvertedInt = clij.converter(array).getInt3DArray();

        assertTrue(TestUtilities.compareArrays(intTestImage, backConvertedInt));
    }

    @Test
    public void testConvertIntUnsignedShort() {

        char[][][] array = clij.converter(intTestImage).getUnsignedShort3DArray();
        int[][][] backConvertedInt = clij.converter(array).getInt3DArray();

        assertTrue(TestUtilities.compareArrays(intTestImage, backConvertedInt));
    }

    @Test
    public void testConvertIntShort() {

        short[][][] array = clij.converter(intTestImage).getShort3DArray();
        int[][][] backConvertedInt = clij.converter(array).getInt3DArray();

        assertTrue(TestUtilities.compareArrays(intTestImage, backConvertedInt));
    }

    @Test
    public void testConvertIntByte() {

        byte[][][] array = clij.converter(intTestImage).getByte3DArray();
        int[][][] backConvertedInt = clij.converter(array).getInt3DArray();

        assertTrue(TestUtilities.compareArrays(intTestImage, backConvertedInt));
    }

    @Test
    public void testConvertIntShortByte() {

        short[][][] array1 = clij.converter(intTestImage).getShort3DArray();
        byte[][][] array2 = clij.converter(array1).getByte3DArray();
        short[][][] array3 = clij.converter(array2).getShort3DArray();
        int[][][] backConvertedInt = clij.converter(array3).getInt3DArray();

        assertTrue(TestUtilities.compareArrays(intTestImage, backConvertedInt));
    }

    @Test
    public void testConvertIntShortFloat() {

        short[][][] array1 = clij.converter(intTestImage).getShort3DArray();
        float[][][] array2 = clij.converter(array1).getFloat3DArray();
        short[][][] array3 = clij.converter(array2).getShort3DArray();
        int[][][] backConvertedInt = clij.converter(array3).getInt3DArray();

        assertTrue(TestUtilities.compareArrays(intTestImage, backConvertedInt));
    }

    @Test
    public void testConvertIntShortDouble() {

        short[][][] array1 = clij.converter(intTestImage).getShort3DArray();
        double[][][] array2 = clij.converter(array1).getDouble3DArray();
        short[][][] array3 = clij.converter(array2).getShort3DArray();
        int[][][] backConvertedInt = clij.converter(array3).getInt3DArray();

        assertTrue(TestUtilities.compareArrays(intTestImage, backConvertedInt));
    }

    @Test
    public void testConvertOffHeapPlanarStackToDoubleArray() {

        StackInterface stack = clij.converter(intTestImage).getOffHeapPlanarStack();

        double[][][] doubleArray = clij.converter(stack).getDouble3DArray();
        StackInterface stack2 = clij.converter(doubleArray).getOffHeapPlanarStack();

        int[][][] result = clij.converter(stack2).getInt3DArray();

        assertTrue(TestUtilities.compareArrays(intTestImage, result));
    }


}
