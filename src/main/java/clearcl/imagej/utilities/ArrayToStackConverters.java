package clearcl.imagej.utilities;

import clearcontrol.core.log.LoggingFeature;
import clearcontrol.stack.OffHeapPlanarStack;
import clearcontrol.stack.StackInterface;
import coremem.ContiguousMemoryInterface;
import coremem.enums.NativeTypeEnum;

/**
 * ArrayToStackConverters contains static helper methods for array to stack conversion. See
 * ImageTypeConverter for actual access methods.
 *
 * Author: @haesleinhuepf
 * Author: @debayansaha102
 * 07 2018
 */
class ArrayToStackConverters {
    public static boolean copyArrayToStack(byte[] array, StackInterface stack) {
        stack.getContiguousMemory().copyFrom(array);
        return true;
    }
    public static boolean copyArrayToStack(char[] array, StackInterface stack) {
        stack.getContiguousMemory().copyFrom(array);
        return true;
    }
    public static boolean copyArrayToStack(int[] array, StackInterface stack) {
        stack.getContiguousMemory().copyFrom(array);
        return true;
    }
    public static boolean copyArrayToStack(short[] array, StackInterface stack) {
        stack.getContiguousMemory().copyFrom(array);
        return true;
    }
    public static boolean copyArrayToStack(long[] array, StackInterface stack) {
        stack.getContiguousMemory().copyFrom(array);
        return true;
    }
    public static boolean copyArrayToStack(float[] array, StackInterface stack) {
        stack.getContiguousMemory().copyFrom(array);
        return true;
    }
    public static boolean copyArrayToStack(double[] array, StackInterface stack) {
        stack.getContiguousMemory().copyFrom(array);
        return true;
    }

    public static boolean copyArrayToStack(byte[][][] array, StackInterface stack) {
        return copyArrayToStackInternal(boxArray(array), stack);
    }
    public static boolean copyArrayToStack(char[][][] array, StackInterface stack) {
        // As Character does not extend Number, we need to convert this Array to Integer
        return copyArrayToStackInternal(intArray(array), stack);
    }
    public static boolean copyArrayToStack(short[][][] array, StackInterface stack) {
        return copyArrayToStackInternal(boxArray(array), stack);
    }
    public static boolean copyArrayToStack(int[][][] array, StackInterface stack) {
        return copyArrayToStackInternal(boxArray(array), stack);
    }
    public static boolean copyArrayToStack(long[][][] array, StackInterface stack) {
        return copyArrayToStackInternal(boxArray(array), stack);
    }
    public static boolean copyArrayToStack(float[][][] array, StackInterface stack) {
        return copyArrayToStackInternal(boxArray(array), stack);
    }
    public static boolean copyArrayToStack(double[][][] array, StackInterface stack) {
        return copyArrayToStackInternal(boxArray(array), stack);
    }

    private static Float[][][] boxArray(float[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        Float[][][] boxedArray = new Float[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    boxedArray[x][y][z] = array[x][y][z];
                }
            }
        }
        return boxedArray;
    }


    private static Double[][][] boxArray(double[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        Double[][][] boxedArray = new Double[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    boxedArray[x][y][z] = array[x][y][z];
                }
            }
        }
        return boxedArray;
    }


    private static Long[][][] boxArray(long[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        Long[][][] boxedArray = new Long[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    boxedArray[x][y][z] = array[x][y][z];
                }
            }
        }
        return boxedArray;
    }

    private static Integer[][][] boxArray(int[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        Integer[][][] boxedArray = new Integer[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    boxedArray[x][y][z] = array[x][y][z];
                }
            }
        }
        return boxedArray;
    }


    private static Character[][][] boxArray(char[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        Character[][][] boxedArray = new Character[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    boxedArray[x][y][z] = array[x][y][z];
                }
            }
        }
        return boxedArray;
    }

    private static Short[][][] boxArray(short[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        Short[][][] boxedArray = new Short[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    boxedArray[x][y][z] = array[x][y][z];
                }
            }
        }
        return boxedArray;
    }


    private static Byte[][][] boxArray(byte[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        Byte[][][] boxedArray = new Byte[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    boxedArray[x][y][z] = array[x][y][z];
                }
            }
        }
        return boxedArray;
    }

    private static Integer[][][] intArray(char[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        Integer[][][] boxedArray = new Integer[width][height][depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    boxedArray[x][y][z] = new Integer(array[x][y][z]);
                }
            }
        }
        return boxedArray;
    }



    private static boolean copyArrayToStackInternal(Number[][][] array, StackInterface stack) {
        int depth = array.length;
        int height = array[0].length;
        int width = array[0][0].length;

        if (stack.getDataType() == NativeTypeEnum.Byte ||
                stack.getDataType() == NativeTypeEnum.UnsignedByte) {
            byte[] img1Darray = new byte[depth * width * height];

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        img1Darray[getPos(x, stack.getWidth(), y, stack.getHeight(), z, stack.getDepth())] = (byte) array[z][y][x].doubleValue();
                    }
                }
            }
            copyArrayToStack(img1Darray, stack);
        }
        else if (stack.getDataType() == NativeTypeEnum.UnsignedShort) {
            char[] img1Darray = new char[depth * width * height];

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        img1Darray[getPos(x, stack.getWidth(), y, stack.getHeight(), z, stack.getDepth())] = (char) array[z][y][x].doubleValue();
                    }
                }
            }
            copyArrayToStack(img1Darray, stack);
        }
        else if (stack.getDataType() == NativeTypeEnum.Short) {
            short[] img1Darray = new short[depth * width * height];

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        img1Darray[getPos(x, stack.getWidth(), y, stack.getHeight(), z, stack.getDepth())] = (short) array[z][y][x].doubleValue();
                    }
                }
            }
            copyArrayToStack(img1Darray, stack);
        }
        else if (stack.getDataType() == NativeTypeEnum.Int || stack.getDataType() == NativeTypeEnum.UnsignedInt) {
            int[] img1Darray = new int[depth * width * height];

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        img1Darray[getPos(x, stack.getWidth(), y, stack.getHeight(), z, stack.getDepth())] = (int) array[z][y][x].doubleValue();
                    }
                }
            }
            copyArrayToStack(img1Darray, stack);
        }
        else if (stack.getDataType() == NativeTypeEnum.Long || stack.getDataType() == NativeTypeEnum.UnsignedLong) {
            long[] img1Darray = new long[depth * width * height];

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        img1Darray[getPos(x, stack.getWidth(), y, stack.getHeight(), z, stack.getDepth())] = (long) array[z][y][x].doubleValue();
                    }
                }
            }
            copyArrayToStack(img1Darray, stack);
        }
        else if (stack.getDataType() == NativeTypeEnum.Float) {
            float[] img1Darray = new float[depth * width * height];

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        img1Darray[getPos(x, stack.getWidth(), y, stack.getHeight(), z, stack.getDepth())] = (float) array[z][y][x].doubleValue();
                    }
                }
            }
            copyArrayToStack(img1Darray, stack);
        }
        else if (stack.getDataType() == NativeTypeEnum.Double) {
            double[] img1Darray = new double[depth * width * height];

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        img1Darray[getPos(x, stack.getWidth(), y, stack.getHeight(), z, stack.getDepth())] = array[z][y][x].doubleValue();
                    }
                }
            }
            copyArrayToStack(img1Darray, stack);
        } else {
            System.out.println("ArrayToStackConverters: Cannot convert array, unknown type!");
            return false;
        }

        return true;
    }

    static int getPos(int x, long pWidth, int y, long pHeight, int z, long pDepth){
        return z * ((int)pHeight *(int)pWidth) + y * (int) pWidth + x;
    }
}
