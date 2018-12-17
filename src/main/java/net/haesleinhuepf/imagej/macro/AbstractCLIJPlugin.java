package net.haesleinhuepf.imagej.macro;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImagePlus;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.imglib2.RandomAccessibleInterval;

/**
 * AbstractCLIJPlugin
 * <p>
 * Author: @haesleinhuepf
 * December 2018
 */
public abstract class AbstractCLIJPlugin implements CLIJMacroPlugin{
    protected ClearCLIJ clij;
    protected Object[] args;
    public AbstractCLIJPlugin() { }

    public void setClij(ClearCLIJ clij) {
        this.clij = clij;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    protected Object[] imageJArgs() {
        Object[] result = new Object[args.length];
        int i = 0;
        for (Object item : args) {
            if (item instanceof RandomAccessibleInterval) {
                result[i] = clij.convert((RandomAccessibleInterval)item, ImagePlus.class);
            } else if (item instanceof ImagePlus) {
                result[i] = (ImagePlus)item;
            } else if(item instanceof ClearCLImage) {
                result[i] = clij.convert((ClearCLImage)item, ImagePlus.class);
            } else if(item instanceof ClearCLBuffer) {
                result[i] = clij.convert((ClearCLBuffer)item, ImagePlus.class);
            } else {
                result[i] = item;
            }
            i++;
        }
        return result;
    }

    protected Object[] openCLBufferArgs() {
        Object[] result = new Object[args.length];
        int i = 0;
        for (Object item : args) {
            if (item instanceof RandomAccessibleInterval) {
                result[i] = clij.convert((RandomAccessibleInterval)item, ClearCLBuffer.class);
            } else if (item instanceof ImagePlus) {
                result[i] = clij.convert((ImagePlus)item, ClearCLBuffer.class);
            } else if(item instanceof ClearCLImage) {
                result[i] = clij.convert((ClearCLImage)item, ClearCLBuffer.class);
            } else if(item instanceof ClearCLBuffer) {
                result[i] = (ClearCLBuffer)item;
            } else {
                result[i] = item;
            }
            i++;
        }
        return result;
    }

    protected Object[] openCLImageArgs() {
        Object[] result = new Object[args.length];
        int i = 0;
        for (Object item : args) {
            if (item instanceof RandomAccessibleInterval) {
                result[i] = clij.convert((RandomAccessibleInterval)item, ClearCLImage.class);
            } else if (item instanceof ImagePlus) {
                result[i] = clij.convert((ImagePlus)item, ClearCLImage.class);
            } else if(item instanceof ClearCLImage) {
                result[i] = (ClearCLImage)item;
            } else if(item instanceof ClearCLBuffer) {
                result[i] = clij.convert((ClearCLBuffer)item, ClearCLImage.class);
            } else {
                result[i] = item;
            }
            i++;
        }
        return result;
    }

    protected Object[] imageJ2Args() {
        Object[] result = new Object[args.length];
        int i = 0;
        for (Object item : args) {
            if (item instanceof RandomAccessibleInterval) {
                result[i] = (RandomAccessibleInterval)item;
            } else if (item instanceof ImagePlus) {
                result[i] = clij.convert((ImagePlus)item, RandomAccessibleInterval.class);
            } else if(item instanceof ClearCLImage) {
                result[i] = clij.convert((ClearCLImage)item, RandomAccessibleInterval.class);
            } else if(item instanceof ClearCLBuffer) {
                result[i] = clij.convert((ClearCLBuffer)item, RandomAccessibleInterval.class);
            } else {
                result[i] = item;
            }
            i++;
        }
        return result;
    }

    protected boolean containsCLImageArguments() {
        for (Object item : args) {
            if (item instanceof ClearCLImage) {
                return true;
            }
        }
        return false;
    }

    protected void releaseImages(Object[] args) {
        for (int i = 0; i < args.length; i ++) {
            Object item = args[i];
            if (item instanceof ClearCLImage && args[i] != this.args[i]) {
                ((ClearCLImage) item).close();
            }
        }
    }


    protected void releaseBuffers(Object[] args) {
        for (int i = 0; i < args.length; i ++) {
            Object item = args[i];
            if (item instanceof ClearCLBuffer && args[i] != this.args[i]) {
                ((ClearCLBuffer) item).close();
            }
        }
    }


    protected boolean containsCLBufferArguments() {
        for (Object item : args) {
            if (item instanceof ClearCLBuffer) {
                return true;
            }
        }
        return false;
    }

    protected Float asFloat(Object number) {
        if (number instanceof Float ) {
            return (Float)number;
        } else {
            return Float.parseFloat(new String("" + number));
        }
    }


    protected Boolean asBoolean(Object object) {
        System.out.println("asBoolean " + object);
        if (object instanceof Boolean ) {
            return (Boolean) object;
        } else if (object instanceof Double) {
            System.out.println("double " + new String("" + object));
            return ((Double)object) != 0;
        } else {
            System.out.println("convert " + new String("" + object));
            System.out.println("convert " + Boolean.parseBoolean(new String("" + object)));
            return Boolean.parseBoolean(new String("" + object));
        }
    }

    protected Integer asInteger(Object number) {
        if (number instanceof Integer ) {
            return (Integer)number;
        } else {
            return new Integer((int)Double.parseDouble(new String("" + number)));
        }
    }

    protected static int radiusToKernelSize(int radius) {
        int kernelSize = radius * 2 + 1;
        return kernelSize;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input)
    {
        return clij.createCLBuffer(input);
    }

}
