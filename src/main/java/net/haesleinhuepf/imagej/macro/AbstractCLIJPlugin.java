package net.haesleinhuepf.imagej.macro;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.macro.MacroExtension;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.frame.Recorder;
import ij.process.ImageProcessor;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.imglib2.RandomAccessibleInterval;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * AbstractCLIJPlugin
 * <p>
 * Author: @haesleinhuepf
 * December 2018
 */
public abstract class AbstractCLIJPlugin implements PlugInFilter, CLIJMacroPlugin {
    protected ClearCLIJ clij;
    protected Object[] args;
    protected String name;
    public AbstractCLIJPlugin() {
        //System.out.println("init " + this);
        String name = this.getClass().getSimpleName();
        this.name = "CLIJ_" + name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
    }

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





    @Override
    public int setup(String arg, ImagePlus imp) {
        return PlugInFilter.DOES_ALL;
    }

    @Override
    public void run(ImageProcessor ip) {
        GenericDialogPlus gd = new GenericDialogPlus(name);

        ArrayList<String> deviceList = ClearCLIJ.getAvailableDeviceNames();
        String[] deviceArray = new String[deviceList.size()];
        deviceList.toArray(deviceArray);
        gd.addChoice("CL_Device", deviceArray, deviceArray[0]);

        String[] parameters = getParameterHelpText().split(",");
        if (parameters.length > 0 && parameters[0].length() > 0) {
            args = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                String[] parameterParts = parameters[i].trim().split(" ");
                String parameterType = parameterParts[0];
                String parameterName = parameterParts[1];
                if (parameterType.compareTo("Image") == 0) {
                    if (!parameterName.contains("destination")) {
                        gd.addImageChoice(parameterName, IJ.getImage().getTitle());
                    }
                } else if (parameterType.compareTo("String") == 0) {
                    gd.addStringField(parameterName, "");
                } else if (parameterType.compareTo("Boolean") == 0) {
                    gd.addCheckbox(parameterName, true);
                } else { // Number
                    gd.addNumericField(parameterName, 2, 2);
                }
            }
        }
        // gd.addNumericField("Radius (in pixels)", 2, 0);

        gd.showDialog();
        if (gd.wasCanceled()) {
            return;
        }

        String deviceName = gd.getNextChoice();

        clij = ClearCLIJ.getInstance(deviceName);

        recordIfNotRecorded("run", "\"CLIJ Macro Extensions\", \"cl_device=[" + deviceName + "]\"");

        ArrayList<ClearCLBuffer> allBuffers = new ArrayList<ClearCLBuffer>();

        HashMap<String, ClearCLBuffer> destinations = new HashMap<String, ClearCLBuffer>();

        String imageTitle = "";
        String calledParameters = "";

        if (parameters.length > 0 && parameters[0].length() > 0) {
            for (int i = 0; i < parameters.length; i++) {
                String[] parameterParts = parameters[i].trim().split(" ");
                String parameterType = parameterParts[0];
                String parameterName = parameterParts[1];
                if (parameterType.compareTo("Image") == 0) {
                    if (parameterName.contains("destination")) {
                        // Creation of output buffers needs to be done after all other parameters have been read.
                        String destinationName = name + "_" + parameterName + "_" + imageTitle;
                        calledParameters = calledParameters + "\"" + destinationName + "\"";
                    } else {
                        ImagePlus imp = gd.getNextImage();
                        if (imageTitle.length() == 0) {
                            imageTitle = imp.getTitle();
                        }
                        recordIfNotRecorded("// Ext.CLIJ_push", "\"" + imp.getTitle() + "\"");
                        args[i] = clij.convert(imp, ClearCLBuffer.class);
                        allBuffers.add((ClearCLBuffer) args[i]);
                        calledParameters = calledParameters + "\"" + imp.getTitle() + "\"";
                    }
                } else if (parameterType.compareTo("String") == 0) {
                    args[i] = gd.getNextString();
                    calledParameters = calledParameters + "\"" + args[i] + "\"";
                } else if (parameterType.compareTo("Boolean") == 0) {
                    boolean value = gd.getNextBoolean();
                    args[i] = value ? 1.0 : 0.0;
                    calledParameters = calledParameters + (value ? "true" : "false");
                } else { // Number
                    args[i] = gd.getNextNumber();
                    calledParameters = calledParameters + args[i];
                }
                if (calledParameters.length() > 0 && i < parameters.length - 1) {
                    calledParameters = calledParameters + ", ";
                }
            }
            for (int i = 0; i < parameters.length; i++) {
                String[] parameterParts = parameters[i].trim().split(" ");
                String parameterType = parameterParts[0];
                String parameterName = parameterParts[1];
                if (parameterType.compareTo("Image") == 0) {
                    if (parameterName.contains("destination")) {
                        if (allBuffers.size() > 0) {
                            ClearCLBuffer destination = createOutputBufferFromSource(allBuffers.get(0));
                            args[i] = destination;
                            allBuffers.add(destination);
                            String destinationName = name + "_" + parameterName + "_" + imageTitle;
                            destinations.put(destinationName, destination);
                        }
                    }
                }
            }
        }

        if (this instanceof CLIJOpenCLProcessor) {
            ((CLIJOpenCLProcessor)this).executeCL();
        } else if (this instanceof CLIJImageJProcessor) {
            ((CLIJImageJProcessor)this).executeIJ();
        }

        record("// Ext." + name, calledParameters);

        for (String destinationName : destinations.keySet()) {
            record("// Ext.CLIJ_pull", "\"" + destinationName + "\"");
            clij.show(destinations.get(destinationName), destinationName);
        }

        for (ClearCLBuffer buffer : allBuffers) {
            buffer.close();
        }
    }

    private void recordIfNotRecorded(String recordMethod, String recordParameters) {
        if (Recorder.getInstance() == null) {
            return;
        }
        if (Recorder.getInstance().getText().contains(recordMethod)) {
            return;
        }
        record(recordMethod, recordParameters);
    }

    private void record(String recordMethod, String recordParameters) {
        if (Recorder.getInstance() == null) {
            return;
        }
        Recorder.recordString(recordMethod + "(" + recordParameters + ");\n");
        Recorder.record = true;
    }
}
