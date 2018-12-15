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
public abstract class AbstractCLIJPlugin {
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
                result[i] = clij.converter((RandomAccessibleInterval)item).getImagePlus();
            } else if (item instanceof ImagePlus) {
                result[i] = clij.converter((ImagePlus)item).getImagePlus();
            } else if(item instanceof ClearCLImage) {
                result[i] = clij.converter((ClearCLImage)item).getImagePlus();
            } else if(item instanceof ClearCLBuffer) {
                result[i] = clij.converter((ClearCLBuffer)item).getImagePlus();
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
                result[i] = clij.converter((RandomAccessibleInterval)item).getClearCLBuffer();
            } else if (item instanceof ImagePlus) {
                result[i] = clij.converter((ImagePlus)item).getClearCLBuffer();
            } else if(item instanceof ClearCLImage) {
                result[i] = clij.converter((ClearCLImage)item).getClearCLBuffer();
            } else if(item instanceof ClearCLBuffer) {
                result[i] = clij.converter((ClearCLBuffer)item).getClearCLBuffer();
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
                result[i] = clij.converter((RandomAccessibleInterval)item).getClearCLImage();
            } else if (item instanceof ImagePlus) {
                result[i] = clij.converter((ImagePlus)item).getClearCLImage();
            } else if(item instanceof ClearCLImage) {
                result[i] = clij.converter((ClearCLImage)item).getClearCLImage();
            } else if(item instanceof ClearCLBuffer) {
                result[i] = clij.converter((ClearCLBuffer)item).getClearCLImage();
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
                result[i] = clij.converter((RandomAccessibleInterval)item).getRandomAccessibleInterval();
            } else if (item instanceof ImagePlus) {
                result[i] = clij.converter((ImagePlus)item).getRandomAccessibleInterval();
            } else if(item instanceof ClearCLImage) {
                result[i] = clij.converter((ClearCLImage)item).getRandomAccessibleInterval();
            } else if(item instanceof ClearCLBuffer) {
                result[i] = clij.converter((ClearCLBuffer)item).getRandomAccessibleInterval();
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
        if (object instanceof Boolean ) {
            return (Boolean)object;
        } else {
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

}
