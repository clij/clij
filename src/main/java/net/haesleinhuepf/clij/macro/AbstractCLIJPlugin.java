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
import java.awt.*;
import java.awt.event.KeyEvent;
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
    protected static Object[] default_values = null;

    protected String name;
    public AbstractCLIJPlugin() {
        this.name = CLIJUtilities.classToName(this.getClass());
        if (this.getClass().getPackage().toString().contains(".clijx.")) {
            this.name = this.name.replace("CLIJ_", "CLIJx_");
        } else if (this.getClass().getPackage().toString().contains(".clij2.")) {
            this.name = this.name.replace("CLIJ_", "CLIJ2_");
        }
    }

    public void setClij(CLIJ clij) {
        this.clij = clij;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
    protected void releaseBuffers(Object[] args) {
        for (int i = 0; i < args.length; i ++) {
            Object item = args[i];
            if (item instanceof ClearCLBuffer && args[i] != this.args[i]) {
                ((ClearCLBuffer) item).close();
            }
        }
    }

    @Deprecated
    protected boolean containsCLBufferArguments() {
        for (Object item : args) {
            if (item instanceof ClearCLBuffer) {
                return true;
            }
        }
        return false;
    }

    public static Float asFloat(Object number) {
        if (number instanceof Float ) {
            return (Float)number;
        } else {
            return Float.parseFloat(new String("" + number));
        }
    }


    public static Boolean asBoolean(Object object) {
        //System.out.println("asBoolean " + object);
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

    public static Integer asInteger(Object number) {
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

    private boolean myWasOKed = false;
    private boolean myWasCancelled = false;
    private class MyGenericDialogPlus extends GenericDialogPlus {
        public MyGenericDialogPlus(String title) {
            super(title);
        }

        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            IJ.setKeyDown(keyCode);

            if (keyCode == 10 && this.textArea1 == null) {
                myWasOKed = true;
                myWasCancelled = false;
                this.dispose();
            } else if (keyCode == 27) {
                myWasOKed = false;
                myWasCancelled = true;
                this.dispose();
                IJ.resetEscape();
            } else if (keyCode == 87 && (e.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0) {
                myWasOKed = false;
                myWasCancelled = true;
                this.dispose();
            }

        }

    }

    @Override
    public void run(ImageProcessor ip) {
        default_values = getDefaultValues();
        GenericDialogPlus gd = new MyGenericDialogPlus(name);

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
                boolean byRef = false;
                if (parameterType.compareTo("ByRef") == 0) {
                    parameterType = parameterParts[1];
                    parameterName = parameterParts[2];
                    byRef = true;
                }
                if (parameterType.compareTo("Image") == 0) {
                    if (!(parameterName.contains("destination") || byRef)) {
                        gd.addImageChoice(parameterName, IJ.getImage().getTitle());
                    }
                } else if (parameterType.compareTo("String") == 0) {
                    if (default_values != null) {
                        gd.addStringField(parameterName, (String)default_values[i], 2);
                    } else {
                        gd.addStringField(parameterName, "");
                    }
                } else if (parameterType.compareTo("Boolean") == 0) {
                    if (default_values != null) {
                        gd.addCheckbox(parameterName, Boolean.valueOf("" + default_values[i]));
                    } else {
                        gd.addCheckbox(parameterName, true);
                    }
                } else { // Number
                    if (default_values != null) {
                        gd.addNumericField(parameterName, Double.valueOf("" + default_values[i]), 2);
                    } else {
                        gd.addNumericField(parameterName, 2, 2);
                    }
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
        gd.addHelp("http://clij.github.io/");
        gd.setHelpLabel("CLIJ online");

        myWasCancelled = false;
        myWasOKed = false;
        gd.showDialog();
        if (gd.wasCanceled() || myWasCancelled || (!myWasOKed && !gd.wasOKed())) {
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

        if (this.getClass().getPackage().toString().contains(".clij.macro.")) {
            recordIfNotRecorded("run", "\"CLIJ Macro Extensions\", \"cl_device=[" + deviceName + "]\"");
        } else {
            recordIfNotRecorded("run", "\"CLIJ2 Macro Extensions\", \"cl_device=[" + deviceName + "]\"");
        }

        String className = this.getClass().getSimpleName();
        className = className.replace("2D", "");
        className = className.replace("3D", "");
        className = className.replace("Box", "");
        className = className.replace("Sphere", "");
        className = className.replace("Diamond", "");
        className = makeNiceName(className, " ");
        className = className.substring(1);
        record(("\n// " + className).replace("  ", " "));
        System.out.println(this.getClass().getPackage().toString());
        if (this.getClass().getPackage().toString().contains(".clij.macro.modules")) {
            record(("// (DEPRECATED: This method is deprecated. Use CLIJ2 instead. See the documentation for details: https://clij.github.io/clij2-docs/)").replace("  ", " "));
        }


        ArrayList<ClearCLBuffer> allBuffers = new ArrayList<ClearCLBuffer>();

        HashMap<String, ClearCLBuffer> destinations = new HashMap<String, ClearCLBuffer>();


        String allParametersAsString = "";
        boolean allParametersAsString_locked = false;
        //String firstImageTitle = "";
        String calledParameters = "";

        if (parameters.length > 0 && parameters[0].length() > 0) {
            for (int i = 0; i < parameters.length; i++) {



                String[] parameterParts = parameters[i].trim().split(" ");
                String parameterType = parameterParts[0];
                String parameterName = parameterParts[1];
                boolean byRef = false;
                if (parameterType.compareTo("ByRef") == 0) {
                    parameterType = parameterParts[1];
                    parameterName = parameterParts[2];
                    byRef = true;
                }

                if (!allParametersAsString_locked) {
                    allParametersAsString = "";
                    if (parameters.length > 0 && parameters[0].length() > 0) {
                        for (int j = 0; j < parameters.length; j++) {
                            String temp = args[j] != null ? args[j].toString() : "";
                            allParametersAsString = allParametersAsString + "#" + parameters[j].trim().replace(" ", "%") + temp;
                        }
                    }
                }

                String parameterNiceName = makeNiceName(parameterName, "_");
                if (parameterType.compareTo("Image") == 0) {
                    if (parameterName.contains("destination") || byRef) {
                        allParametersAsString_locked = true;
                        // Creation of output buffers needs to be done after all other parameters have been read.
                        String destinationName = getImageVariableName(className.replace(" ", "_") + (name + "_" + parameterName + "_" + allParametersAsString).hashCode());
                        calledParameters = calledParameters + destinationName;
                    } else {
                        ImagePlus imp = gd.getNextImage();
                        //if (firstImageTitle.length() == 0) {
                        //    firstImageTitle = imp.getTitle();
                        //}

                        String variable = getImageVariableName(imp.getTitle());

                        if (this.getClass().getPackage().toString().contains(".clij.macro.")) {
                            recordIfNotRecorded("Ext.CLIJ_push", variable);
                        } else {
                            recordIfNotRecorded("Ext.CLIJ2_push", variable);
                        }
                        args[i] = CLIJHandler.getInstance().pushToGPU(imp.getTitle());
                                //clij.convert(imp, ClearCLBuffer.class);
                        allBuffers.add((ClearCLBuffer) args[i]);
                        calledParameters = calledParameters + variable;
                    }
                } else if (parameterType.compareTo("String") == 0) {
                    args[i] = gd.getNextString();
                    if (default_values != null) {
                        default_values[i] = (String)args[i];
                    }
                    record(parameterNiceName + " = \"" + args[i] + "\";");
                    calledParameters = calledParameters + parameterNiceName;
                } else if (parameterType.compareTo("Boolean") == 0) {
                    boolean value = gd.getNextBoolean();
                    args[i] = value ? 1.0 : 0.0;
                    if (default_values != null) {
                        default_values[i] = Boolean.valueOf((double)args[i] != 0);
                    }
                    record(parameterNiceName + " = " + (value ? "true" : "false") + ";");
                    calledParameters = calledParameters + parameterNiceName;
                } else { // Number
                    args[i] = gd.getNextNumber();
                    if (default_values != null) {
                        default_values[i] = Double.valueOf((double)args[i]);
                    }
                    record(parameterNiceName + " = " + args[i] + ";");
                    calledParameters = calledParameters + parameterNiceName;
                }
                if (calledParameters.length() > 0 && i < parameters.length - 1) {
                    calledParameters = calledParameters + ", ";
                }
            }
            for (int i = 0; i < parameters.length; i++) {
                String[] parameterParts = parameters[i].trim().split(" ");
                String parameterType = parameterParts[0];
                String parameterName = parameterParts[1];
                boolean byRef = false;
                if (parameterType.compareTo("ByRef") == 0) {
                    parameterType = parameterParts[1];
                    parameterName = parameterParts[2];
                    byRef = true;
                }
                String parameterNiceName = makeNiceName(parameterName, "_");
                if (parameterType.compareTo("Image") == 0) {
                    if (parameterName.contains("destination") || byRef) {
                        ClearCLBuffer template = null;
                        if (allBuffers.size() > 0) {
                            template = allBuffers.get(0);
                        }

                        allParametersAsString_locked = true;
                        String destinationName = className.replace(" ", "_") + (name + "_" + parameterName + "_" + allParametersAsString).hashCode(); // name + "_" + parameterName + "_" + firstImageTitle;
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
            String variable = getImageVariableName(destinationName);
            if (this.getClass().getPackage().toString().contains(".clij.macro.")) {
                record("Ext.CLIJ_pull", variable);
            } else {
                record("Ext.CLIJ2_pull", variable);
            }
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

    public Object[] getDefaultValues() {
        return null;
    }

    private String makeNiceName(String className, String spacer) {
        StringBuilder niceName = new StringBuilder();
        for (int i = 0; i < className.length(); i++){
            if (className.substring(i,i+1).compareTo("_") == 0) {
                niceName.append(spacer);
            } else {
                if (className.substring(i, i + 1).compareTo(className.substring(i, i + 1).toLowerCase()) != 0) {
                    niceName.append(spacer);
                }
                niceName.append(className.substring(i, i + 1).toLowerCase());
            }
        }
        return niceName.toString();
    }

    private static int imageCounter = 0;
    protected String getImageVariableName(String destinationName) {
        if (Recorder.getInstance() == null) {
            return "";
        }
        String text = Recorder.getInstance().getText();
        String searchString = " = \"" + destinationName + "\";";
        if (text.contains(searchString)) {
            String[] temp = text.split(searchString);
            temp = temp[0].split("\n");
            return temp[temp.length - 1];
        } else {
            imageCounter++;
            String imageName = "image" + imageCounter;
            record(imageName + searchString);
            return imageName;
        }

    }

    private void recordIfNotRecorded(String recordMethod, String recordParameters) {
        if (Recorder.getInstance() == null) {
            return;
        }
        String text = Recorder.getInstance().getText();
        if ( (
                text.contains(recordMethod.replace("CLIJ", "CLIJ2")) ||
                text.contains(recordMethod.replace("CLIJ2", "CLIJ"))) &&
            text.contains(recordParameters)) {
            return;
        }
        record(recordMethod, recordParameters);
    }

    private void recordIfNotRecorded(String textToRecord) {
        if (Recorder.getInstance() == null) {
            return;
        }
        String text = Recorder.getInstance().getText();
        if (text.contains(textToRecord)) {
            return;
        }
        record(textToRecord);
    }

    private void record(String recordMethod, String recordParameters) {
        if (Recorder.getInstance() == null) {
            return;
        }
        Recorder.recordString(recordMethod + "(" + recordParameters + ");\n");
        Recorder.record = true;
    }

    private void record(String text) {
        if (Recorder.getInstance() == null) {
            return;
        }
        Recorder.recordString(text + "\n");
        Recorder.record = true;
    }

    public String getName() {
        return name;
    }
}
