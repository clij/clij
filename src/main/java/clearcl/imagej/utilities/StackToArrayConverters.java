package clearcl.imagej.utilities;

import clearcontrol.stack.StackInterface;
import coremem.ContiguousMemoryInterface;
import coremem.enums.NativeTypeEnum;

/**
 * StackToArrayConverters
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * Author: @debayansaha102
 * 07 2018
 */
public class StackToArrayConverters {

    public static boolean copyStackToArray(StackInterface stack, byte[][][] array) {
        Byte[][][] boxedArray = new Byte[array.length][array[0].length][array[0][0].length];
        if (!copyStackToArrayInternal(stack, boxedArray, new Byte((byte)0))) {
            return false;
        }
        unboxArray(boxedArray, array);
        return true;
    }

    public static boolean copyStackToArray(StackInterface stack, char[][][] array) {
        Integer[][][] boxedArray = new Integer[array.length][array[0].length][array[0][0].length];
        if (!copyStackToArrayInternal(stack, boxedArray, new Character((char)0))) {
            return false;
        }
        unboxArray(boxedArray, array);
        return true;
    }

    public static boolean copyStackToArray(StackInterface stack, short[][][] array) {
        Short[][][] boxedArray = new Short[array.length][array[0].length][array[0][0].length];
        if (!copyStackToArrayInternal(stack, boxedArray, new Short((short)0))) {
            return false;
        }
        unboxArray(boxedArray, array);
        return true;
    }

    public static boolean copyStackToArray(StackInterface stack, int[][][] array) {
        Integer[][][] boxedArray = new Integer[array.length][array[0].length][array[0][0].length];
        if (!copyStackToArrayInternal(stack, boxedArray, new Integer(0))) {
            return false;
        }
        unboxArray(boxedArray, array);
        return true;
    }

    public static boolean copyStackToArray(StackInterface stack, long[][][] array) {
        Long[][][] boxedArray = new Long[array.length][array[0].length][array[0][0].length];
        if (!copyStackToArrayInternal(stack, boxedArray, new Long(0))) {
            return false;
        }
        unboxArray(boxedArray, array);
        return true;
    }

    public static boolean copyStackToArray(StackInterface stack, float[][][] array) {
        Float[][][] boxedArray = new Float[array.length][array[0].length][array[0][0].length];
        if (!copyStackToArrayInternal(stack, boxedArray, new Float(0))) {
            return false;
        }
        unboxArray(boxedArray, array);
        return true;
    }

    public static boolean copyStackToArray(StackInterface stack, double[][][] array) {
        Double[][][] boxedArray = new Double[array.length][array[0].length][array[0][0].length];
        if (!copyStackToArrayInternal(stack, boxedArray, new Double(0))) {
            return false;
        }
        unboxArray(boxedArray, array);
        return true;
    }

    private static void unboxArray(Byte[][][] boxedArray, byte[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    array[x][y][z] = boxedArray[x][y][z];
                }
            }
        }
    }

    private static void unboxArray(Integer[][][] boxedArray, char[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    array[x][y][z] = (char) boxedArray[x][y][z].doubleValue();
                }
            }
        }
    }

    private static void unboxArray(Short[][][] boxedArray, short[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    array[x][y][z] = boxedArray[x][y][z];
                }
            }
        }
    }

    private static void unboxArray(Integer[][][] boxedArray, int[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    array[x][y][z] = boxedArray[x][y][z];
                }
            }
        }
    }

    private static void unboxArray(Long[][][] boxedArray, long[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    array[x][y][z] = boxedArray[x][y][z];
                }
            }
        }
    }

    private static void unboxArray(Float[][][] boxedArray, float[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    array[x][y][z] = boxedArray[x][y][z];
                }
            }
        }
    }

    private static void unboxArray(Double[][][] boxedArray, double[][][] array) {
        int depth = array[0][0].length;
        int height = array[0].length;
        int width = array.length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    array[x][y][z] = boxedArray[x][y][z];
                }
            }
        }
    }

    private static boolean copyStackToArrayInternal(StackInterface stack, Number[][][] boxedArray, Object type) {
        /*if (stack.getDataType() == NativeTypeEnum.Double && (!(boxedArray[0][0][0] instanceof Double))) {
            throw new IllegalArgumentException("Incompatible types!");
        } else if (stack.getDataType() == NativeTypeEnum.Float && (!(boxedArray[0][0][0] instanceof Float))) {
            throw new IllegalArgumentException("Incompatible types!");
        } else if (stack.getDataType() == NativeTypeEnum.Int && (!(boxedArray[0][0][0] instanceof Integer))) {
            throw new IllegalArgumentException("Incompatible types!");
        } else if (stack.getDataType() == NativeTypeEnum.UnsignedInt && (!(boxedArray[0][0][0] instanceof Integer))) {
            throw new IllegalArgumentException("Incompatible types!");
        } else if (stack.getDataType() == NativeTypeEnum.Long && (!(boxedArray[0][0][0] instanceof Long))) {
            throw new IllegalArgumentException("Incompatible types!");
        } else if (stack.getDataType() == NativeTypeEnum.UnsignedLong && (!(boxedArray[0][0][0] instanceof Long))) {
            throw new IllegalArgumentException("Incompatible types!");
        } else if (stack.getDataType() == NativeTypeEnum.Byte && (!(boxedArray[0][0][0] instanceof Byte))) {
            throw new IllegalArgumentException("Incompatible types!");
        } else if (stack.getDataType() == NativeTypeEnum.Short && (!(boxedArray[0][0][0] instanceof Short))) {
            throw new IllegalArgumentException("Incompatible types!");
        } else if (stack.getDataType() == NativeTypeEnum.UnsignedShort && (!(boxedArray[0][0][0] instanceof Integer))) {
            throw new IllegalArgumentException("Incompatible types!");
        }*/

        int depth = (int) stack.getDepth();
        int height = (int) stack.getHeight();
        int width = (int) stack.getWidth();

        if (type instanceof Byte) {
            byte[] temp = getByteArrayFromStack(stack);

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        boxedArray[z][y][x] = temp[ArrayToStackConverters.getPos(x, width, y, height, z, depth)];
                    }
                }
            }
        } else if (type instanceof Character) {
            char[] temp = getUnsignedShortArrayFromStack(stack);

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        boxedArray[z][y][x] = (int) temp[ArrayToStackConverters.getPos(x, width, y, height, z, depth)];
                    }
                }
            }
        } else if (type instanceof Short) {
            short[] temp = getShortArrayFromStack(stack);

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        boxedArray[z][y][x] = temp[ArrayToStackConverters.getPos(x, width, y, height, z, depth)];
                    }
                }
            }
        } else if (type instanceof Integer) {
            int[] temp = getIntArrayFromStack(stack);

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        boxedArray[z][y][x] = new Integer(temp[ArrayToStackConverters.getPos(x, width, y, height, z, depth)]);
                    }
                }
            }
        } else if (type instanceof Long) {
            long[] temp = getLongArrayFromStack(stack);

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        boxedArray[z][y][x] = temp[ArrayToStackConverters.getPos(x, width, y, height, z, depth)];
                    }
                }
            }
        } else if (type instanceof Float) {
            float[] temp = getFloatArrayFromStack(stack);

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        boxedArray[z][y][x] = temp[ArrayToStackConverters.getPos(x, width, y, height, z, depth)];
                    }
                }
            }
        } else if (type instanceof Double) {
            double[] temp = getDoubleArrayFromStack(stack);

            for (int z = 0; z < depth; z++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        boxedArray[z][y][x] = temp[ArrayToStackConverters.getPos(x, width, y, height, z, depth)];
                    }
                }
            }
        } else {
            System.out.println("StackToArrayConverters: Cannot convert stack, unknown type!");
            return false;
        }

        return true;
    }

    private static byte[] getByteArrayFromStack(StackInterface stack) {
        final ContiguousMemoryInterface contiguousMemory =
                stack.getContiguousMemory();

        int depth = (int) stack.getDepth();
        int height = (int) stack.getHeight();
        int width = (int) stack.getWidth();

        byte[] result = null;

        if (stack.getDataType() == NativeTypeEnum.Byte) {
            byte[] temp = new byte[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToByteArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Short) {
            short[] temp = new short[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToByteArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.UnsignedShort) {
            char[] temp = new char[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToByteArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Int || stack.getDataType() == NativeTypeEnum.UnsignedInt) {
            int[] temp = new int[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToByteArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Long || stack.getDataType() == NativeTypeEnum.UnsignedLong) {
            long[] temp = new long[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToByteArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Float) {
            float[] temp = new float[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToByteArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Double) {
            double[] temp = new double[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToByteArray(temp);
        } else {
            throw new IllegalArgumentException("wrong array type");
        }

        return result;
    }

    private static short[] getShortArrayFromStack(StackInterface stack) {
        final ContiguousMemoryInterface contiguousMemory =
                stack.getContiguousMemory();

        int depth = (int) stack.getDepth();
        int height = (int) stack.getHeight();
        int width = (int) stack.getWidth();

        short[] result = null;

        if (stack.getDataType() == NativeTypeEnum.Byte) {
            byte[] temp = new byte[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToShortArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Short) {
            short[] temp = new short[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToShortArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.UnsignedShort) {
            char[] temp = new char[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToShortArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Int || stack.getDataType() == NativeTypeEnum.UnsignedInt) {
            int[] temp = new int[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToShortArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Long || stack.getDataType() == NativeTypeEnum.UnsignedLong) {
            long[] temp = new long[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToShortArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Float) {
            float[] temp = new float[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToShortArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Double) {
            double[] temp = new double[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToShortArray(temp);
        } else {
            throw new IllegalArgumentException("wrong array type");
        }

        return result;
    }

    private static char[] getUnsignedShortArrayFromStack(StackInterface stack) {
        final ContiguousMemoryInterface contiguousMemory =
                stack.getContiguousMemory();

        int depth = (int) stack.getDepth();
        int height = (int) stack.getHeight();
        int width = (int) stack.getWidth();

        char[] result = null;

        if (stack.getDataType() == NativeTypeEnum.Byte) {
            byte[] temp = new byte[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToCharArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Short) {
            short[] temp = new short[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToCharArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.UnsignedShort) {
            char[] temp = new char[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToCharArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Int || stack.getDataType() == NativeTypeEnum.UnsignedInt) {
            int[] temp = new int[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToCharArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Long || stack.getDataType() == NativeTypeEnum.UnsignedLong) {
            long[] temp = new long[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToCharArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Float) {
            float[] temp = new float[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToCharArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Double) {
            double[] temp = new double[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToCharArray(temp);
        } else {
            throw new IllegalArgumentException("wrong array type");
        }

        return result;
    }

    private static int[] getIntArrayFromStack(StackInterface stack) {
        final ContiguousMemoryInterface contiguousMemory =
                stack.getContiguousMemory();

        int depth = (int) stack.getDepth();
        int height = (int) stack.getHeight();
        int width = (int) stack.getWidth();

        int[] result = null;

        if (stack.getDataType() == NativeTypeEnum.Byte) {
            byte[] temp = new byte[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToIntArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Short) {
            short[] temp = new short[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToIntArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.UnsignedShort) {
            char[] temp = new char[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToIntArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Int || stack.getDataType() == NativeTypeEnum.UnsignedInt) {
            int[] temp = new int[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToIntArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Long || stack.getDataType() == NativeTypeEnum.UnsignedLong) {
            long[] temp = new long[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToIntArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Float) {
            float[] temp = new float[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToIntArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Double) {
            double[] temp = new double[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToIntArray(temp);
        } else {
            throw new IllegalArgumentException("wrong array type");
        }

        return result;
    }
    private static long[] getLongArrayFromStack(StackInterface stack) {
        final ContiguousMemoryInterface contiguousMemory =
                stack.getContiguousMemory();

        int depth = (int) stack.getDepth();
        int height = (int) stack.getHeight();
        int width = (int) stack.getWidth();

        long[] result = null;

        if (stack.getDataType() == NativeTypeEnum.Byte) {
            byte[] temp = new byte[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToLongArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Short) {
            short[] temp = new short[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToLongArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.UnsignedShort) {
            char[] temp = new char[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToLongArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Int || stack.getDataType() == NativeTypeEnum.UnsignedInt) {
            int[] temp = new int[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToLongArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Long || stack.getDataType() == NativeTypeEnum.UnsignedLong) {
            long[] temp = new long[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToLongArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Float) {
            float[] temp = new float[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToLongArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Double) {
            double[] temp = new double[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToLongArray(temp);
        } else {
            throw new IllegalArgumentException("wrong array type");
        }

        return result;
    }
    private static float[] getFloatArrayFromStack(StackInterface stack) {
        final ContiguousMemoryInterface contiguousMemory =
                stack.getContiguousMemory();

        int depth = (int) stack.getDepth();
        int height = (int) stack.getHeight();
        int width = (int) stack.getWidth();

        float[] result = null;

        if (stack.getDataType() == NativeTypeEnum.Byte) {
            byte[] temp = new byte[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToFloatArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Short) {
            short[] temp = new short[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToFloatArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.UnsignedShort) {
            char[] temp = new char[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToFloatArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Int || stack.getDataType() == NativeTypeEnum.UnsignedInt) {
            int[] temp = new int[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToFloatArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Long || stack.getDataType() == NativeTypeEnum.UnsignedLong) {
            long[] temp = new long[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToFloatArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Float) {
            float[] temp = new float[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToFloatArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Double) {
            double[] temp = new double[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToFloatArray(temp);
        } else {
            throw new IllegalArgumentException("wrong array type");
        }

        return result;
    }
    private static double[] getDoubleArrayFromStack(StackInterface stack) {
        final ContiguousMemoryInterface contiguousMemory =
                stack.getContiguousMemory();

        int depth = (int) stack.getDepth();
        int height = (int) stack.getHeight();
        int width = (int) stack.getWidth();

        double[] result = null;

        if (stack.getDataType() == NativeTypeEnum.Byte) {
            byte[] temp = new byte[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToDoubleArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Short) {
            short[] temp = new short[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToDoubleArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.UnsignedShort) {
            char[] temp = new char[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToDoubleArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Int || stack.getDataType() == NativeTypeEnum.UnsignedInt) {
            int[] temp = new int[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToDoubleArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Long || stack.getDataType() == NativeTypeEnum.UnsignedLong) {
            long[] temp = new long[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToDoubleArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Float) {
            float[] temp = new float[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToDoubleArray(temp);
        } else if (stack.getDataType() == NativeTypeEnum.Double) {
            double[] temp = new double[width * height * depth];
            contiguousMemory.copyTo(temp);
            result = ArrayConverters.convertToDoubleArray(temp);
        } else {
            throw new IllegalArgumentException("wrong array type");
        }

        return result;
    }



}
