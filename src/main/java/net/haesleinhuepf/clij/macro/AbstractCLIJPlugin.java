package net.haesleinhuepf.clij.macro;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.frame.Recorder;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.documentation.HTMLDocumentationTemplate;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.utilities.CLIJUtilities;
import net.imglib2.RandomAccessibleInterval;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * AbstractCLIJPlugin
 * <p>
 * Author: @haesleinhuepf
 * December 2018
 */
public abstract class AbstractCLIJPlugin implements PlugInFilter, CLIJMacroPlugin {
    protected CLIJ clij;
    protected Object[] args;
    protected String name;
    public AbstractCLIJPlugin() {
        this.name = CLIJUtilities.classToName(this.getClass());
        if (!this.getClass().getPackage().toString().contains(".clij.")) {
            this.name = this.name.replace("CLIJ_", "CLIJx_");
        }
    }

    public void setClij(CLIJ clij) {
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
                ClearCLBuffer buffer = (ClearCLBuffer)item;
                ClearCLImage image = CLIJHandler.getInstance().getChachedImageByBuffer(buffer);
                result[i] = image;
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

    @Deprecated // no longer needed as images are cached, release and managed with their corresponding buffers
    protected void releaseImages(Object[] args) {
        if (true) return;
        for (int i = 0; i < args.length; i ++) {
            Object item = args[i];
            if (item instanceof ClearCLImage && args[i] != this.args[i]) {
                //if (CLIJ.debug) {
                System.out.println("Releasing " + item);
                //}
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

        ArrayList<String> deviceList = CLIJ.getAvailableDeviceNames();
        if (clij == null) {
            clij = CLIJ.getInstance();
        }
        String[] deviceArray = new String[deviceList.size()];
        deviceList.toArray(deviceArray);
        gd.addChoice("CL_Device", deviceArray, clij.getClearCLContext().getDevice().getName());

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
        if (this instanceof OffersDocumentation) {
            gd.addComponent(new JLabel("<html><body>" +
                    new HTMLDocumentationTemplate(((OffersDocumentation) this).getDescription(), ((OffersDocumentation) this).getAvailableForDimensions(), this).toString() +
                    "</body></html>"
            ));
        }

        gd.showDialog();
        if (gd.wasCanceled()) {
            return;
        }

        // find out if a macro is running
        String threadName = Thread.currentThread().getName();
        boolean isMacro = threadName.startsWith("Run$_");

        if (!isMacro) {
            // Before we start: Empty the cache:
            CLIJHandler.getInstance().clearGPU();
        }
        String deviceName = gd.getNextChoice();

        clij = CLIJ.getInstance(deviceName);
        //CLIJHandler.getInstance().setCLIJ(clij);

        recordIfNotRecorded("run", "\"CLIJ Macro Extensions\", \"cl_device=[" + deviceName + "]\"");

        ArrayList<ClearCLBuffer> allBuffers = new ArrayList<ClearCLBuffer>();

        HashMap<String, ClearCLBuffer> destinations = new HashMap<String, ClearCLBuffer>();

        String firstImageTitle = "";
        String calledParameters = "";

        if (parameters.length > 0 && parameters[0].length() > 0) {
            for (int i = 0; i < parameters.length; i++) {
                String[] parameterParts = parameters[i].trim().split(" ");
                String parameterType = parameterParts[0];
                String parameterName = parameterParts[1];
                if (parameterType.compareTo("Image") == 0) {
                    if (parameterName.contains("destination")) {
                        // Creation of output buffers needs to be done after all other parameters have been read.
                        String destinationName = name + "_" + parameterName + "_" + firstImageTitle;
                        calledParameters = calledParameters + "\"" + destinationName + "\"";
                    } else {
                        ImagePlus imp = gd.getNextImage();
                        if (firstImageTitle.length() == 0) {
                            firstImageTitle = imp.getTitle();
                        }
                        recordIfNotRecorded("Ext.CLIJ_push", "\"" + imp.getTitle() + "\"");
                        args[i] = CLIJHandler.getInstance().pushToGPU(imp.getTitle());
                                //clij.convert(imp, ClearCLBuffer.class);
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
                        ClearCLBuffer template = null;
                        if (allBuffers.size() > 0) {
                            template = allBuffers.get(0);
                        }

                        String destinationName = name + "_" + parameterName + "_" + firstImageTitle;
                        ClearCLBuffer destination = CLIJHandler.getInstance().getFromCacheOrCreateByPlugin(destinationName, this, template);
                        args[i] = destination;
                        allBuffers.add(destination);
                        destinations.put(destinationName, destination);
                    }
                }
            }
        }

        if (this instanceof CLIJOpenCLProcessor) {
            ((CLIJOpenCLProcessor)this).executeCL();
        } else if (this instanceof CLIJImageJProcessor) {
            ((CLIJImageJProcessor)this).executeIJ();
        }

        record("Ext." + name, calledParameters);

        for (String destinationName : destinations.keySet()) {
            record("Ext.CLIJ_pull", "\"" + destinationName + "\"");
        }

        Recorder.setCommand(null);
        boolean recordBefore = Recorder.record;
        Recorder.record = false;
        for (String destinationName : destinations.keySet()) {
            clij.show(destinations.get(destinationName), destinationName);
        }
        Recorder.record = recordBefore;

        allBuffers.clear();
        if (!isMacro) {
            CLIJHandler.getInstance().clearGPU();
        }
    }

    private void recordIfNotRecorded(String recordMethod, String recordParameters) {
        if (Recorder.getInstance() == null) {
            return;
        }
        String text = Recorder.getInstance().getText();
        if (text.contains(recordMethod) && text.contains(recordParameters)) {
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

    public String getName() {
        return name;
    }
}
