package net.haesleinhuepf.imagej.macro;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.ClearCLIJ;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.macro.ExtensionDescriptor;
import ij.macro.Functions;
import ij.macro.MacroExtension;
import org.apache.commons.lang3.math.NumberUtils;
import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * CLIJMacroExtensions
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 11 2018
 */
@Plugin(type = Command.class, menuPath = "Plugins>CLIJ>CLIJ Macro Extensions")
public class CLIJMacroExtensions implements Command, MacroExtension {

    final String TO_CLIJ = "CLIJ_push";
    final String FROM_CLIJ = "CLIJ_pull";
    final String RELEASE_BUFFER = "CLIJ_release";
    final String CLEAR_CLIJ = "CLIJ_clear";
    final String HELP = "CLIJ_help";



    ClearCLIJ clij;

    private class MethodInfo {
        MethodInfo(ExtensionDescriptor extensionDescriptor, Method method, String parameters, String name) {
            this.extensionDescriptor = extensionDescriptor;
            this.method = method;
            this.parameters = parameters;
            this.name = name;
        }
        final ExtensionDescriptor extensionDescriptor;
        final Method method;
        final String parameters;
        final String name;
    }

    //ArrayList<ExtensionDescriptor> list = new ArrayList<ExtensionDescriptor>();
    //HashMap<String, Method> map = new HashMap<String, Method>();

    HashMap<String, MethodInfo> methodMap = new HashMap<String, MethodInfo>();

    static HashMap<String, ClearCLBuffer> bufferMap = new HashMap<String, ClearCLBuffer>();


    @Override
    public void run() {
        ArrayList<String> deviceList = ClearCLIJ.getAvailableDeviceNames();
        String[] deviceArray = new String[deviceList.size()];
        deviceList.toArray(deviceArray);

        GenericDialog gd = new GenericDialog("CLIJ");
        gd.addChoice("CL_Device", deviceArray, deviceArray[0]);
        gd.showDialog();

        if (gd.wasCanceled()) {
            return;
        }
        clij = ClearCLIJ.getInstance(gd.getNextChoice());

        if (!IJ.macroRunning()) {
            IJ.error("Cannot install extensions from outside a macro.");
            return;
        }
        Functions.registerExtensions(this);
    }


    @Override
    public String handleExtension(String name, Object[] args) {
        //System.out.println("Handle Ext " + name);
        try {
            if (name.equals(TO_CLIJ)) {
                toCLIJ((String) args[0]);
                return null;
            } else if (name.equals(FROM_CLIJ)) {
                fromCLIJ((String) args[0]);
                return null;
            } else if (name.equals(RELEASE_BUFFER)) {
                releaseBuffer((String) args[0]);
                return null;
            } else if (name.equals(CLEAR_CLIJ)) {
                clearCLIJ();
                return null;
            } else if (name.equals(HELP)) {
                help(args);
                return null;
            }

            //System.out.println("methods " + methodMap.size());
            Method method = methodMap.get(name + (args.length + 1)).method;
            if (method == null) {
                //System.out.println("Method not found: " + name);
                return "Error: Method not found!";
            }

            //System.out.println("Check method: " + name);
            Object[] parsedArguments = new Object[args.length + 1];
            parsedArguments[0] = clij;
            for (int i = 0; i < args.length; i++) {
                //System.out.println("Parsing args: " + args[i] + " " + args[i].getClass());
                if (args[i] instanceof Double) {
                    //System.out.println("numeric");
                    Class type = method.getParameters()[i + 1].getType();
                    //System.out.println("type " + type);
                    if (type == Double.class) {
                        parsedArguments[i + 1] = args[i];
                    } else if (type == Float.class) {
                        parsedArguments[i + 1] = ((Double) args[i]).floatValue();
                    } else if (type == Integer.class) {
                        parsedArguments[i + 1] = ((Double) args[i]).intValue();
                    } else if (type == Boolean.class) {
                        parsedArguments[i + 1] = ((Double) args[i]) > 0;
                    } else {
                        //System.out.println("Unknown type: " + type);
                    }
                } else {
                    //System.out.println("not numeric");
                    ClearCLBuffer bufferImage = bufferMap.get(args[i]);
                    if (bufferImage == null) {
                        IJ.log("Error: Image \"" + args[i] + "\" doesn't exist in GPU memory. Try this:");
                        IJ.log("Ext.CLIJ_push(\"" + args[i] + "\");");
                    }
                    parsedArguments[i + 1] = bufferImage;
                }
                //System.out.println("Parsed args: " + parsedArguments[i + 1]);
            }


            System.out.println("Invoke method: " + name);
            //for (int i = 0; i < parsedArguments.length; i++) {
                //if (parsedArguments[i] != null) {
                    //System.out.println("" + parsedArguments[i] + " " + parsedArguments[i].getClass());
                //}
            //}

            try {
                method.invoke(null, parsedArguments);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return "IllegalArgumentException";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return "IllegalAccessException";
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return "InvocationTargetException";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception";
            }

            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isNumeric(Object text) {
        if (text instanceof Double) {
            return true;
        }
        return NumberUtils.isNumber((String)text);
    }

    private void help(Object[] args) {
        //IJ.log("Help");
        String searchString = "";
        if (args.length > 0) {
            searchString = (String)(args[0]);
        }
        ArrayList<String> helpList = new ArrayList<String>();
        for (String key : methodMap.keySet()) {
            //System.out.println(key);
            if (searchString.length() == 0 || key.contains(searchString)) {

                helpList.add(methodMap.get(key).name + "(" + methodMap.get(key).parameters + ")");
                //IJ.log(key + "(" + methodMap.get(key).parameters + ")");
            }
        }

        IJ.log("Found " + helpList.size() + " method(s) containing the pattern \"" + searchString + "\":");
        Collections.sort(helpList);
        for (String entry : helpList) {
            IJ.log("Ext." + entry + ";");
        }
        //IJ.log("Helped");
    }

    private void releaseBuffer(String arg) {
        System.out.println("Releasing " + arg);
        ClearCLBuffer buffer = bufferMap.get(arg);
        buffer.close();
        bufferMap.remove(arg);
    }

    private void clearCLIJ() {
        ArrayList<String> keysToRelease = new ArrayList<String>();
        for (String key : bufferMap.keySet()) {
            keysToRelease.add(key);
        }
        for (String key : keysToRelease) {
            releaseBuffer(key);
        }
        bufferMap.clear();
    }

    private void fromCLIJ(String arg) {
        ClearCLBuffer buffer = bufferMap.get(arg);
        clij.show(buffer, arg);
    }

    private void toCLIJ(String arg) {
        ImagePlus imp = WindowManager.getImage(arg);
        bufferMap.put(arg, clij.converter(imp).getClearCLBuffer());
    }

    @Override
    public ExtensionDescriptor[] getExtensionFunctions() {

        parseClass(CLIJMacroAPI.class);

        int numberOfPredefinedExtensions = 5;

        ExtensionDescriptor[] extensions = new ExtensionDescriptor[methodMap.size() + numberOfPredefinedExtensions];

        extensions[0] = new ExtensionDescriptor(TO_CLIJ, new int[]{MacroExtension.ARG_STRING}, this);
        extensions[1] = new ExtensionDescriptor(FROM_CLIJ, new int[]{MacroExtension.ARG_STRING}, this);
        extensions[2] = new ExtensionDescriptor(RELEASE_BUFFER, new int[]{MacroExtension.ARG_STRING}, this);
        extensions[3] = new ExtensionDescriptor(CLEAR_CLIJ, new int[]{}, this);
        extensions[4] = new ExtensionDescriptor(HELP, new int[]{MacroExtension.ARG_STRING}, this);

        int i = numberOfPredefinedExtensions;
        for (String key: methodMap.keySet()) {
            extensions[i] = methodMap.get(key).extensionDescriptor;
            ////System.out.println("Add method: " + key + methodMap.get(key).parameters);
            i++;
        }

        return extensions;
    }

    private void parseClass(Class c) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.getName().startsWith("parameter")) {
                ArrayList<Integer> typeList = new ArrayList<Integer>();
                String parameters = "";
                boolean makeAvailable = true;

                int j = 0;
                String parameter_doc = "";
                try {
                    Field parameter_doc_field = c.getField("parameter_doc_" + method.getName());
                    parameter_doc = (String) ((Field) parameter_doc_field).get(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                for (Parameter parameter : method.getParameters()) {
                    if (parameter.getType() != ClearCLIJ.class) {
                        if (parameter.getType() == ClearCLImage.class) {
                            makeAvailable = false;
                        }

                        String type = "";
                        if (parameter.getType() == ClearCLBuffer.class) {
                            type = "image";
                            typeList.add(MacroExtension.ARG_STRING);
                        } else if (parameter.getType() == Float.class || parameter.getType() == Integer.class || parameter.getType() == Double.class) {
                            type = "number";
                            typeList.add(MacroExtension.ARG_NUMBER);
                        } else if (parameter.getType() == Boolean.class) {
                            type = "boolean";
                            typeList.add(MacroExtension.ARG_NUMBER);
                        } else {
                            type = "var";
                            typeList.add(MacroExtension.ARG_STRING);
                        }

                        if (parameters.length() == 0) {
                            parameters = type;
                        } else {
                            parameters = parameters + "," + type;
                        }
                        parameters = parameters + " " + parameter.getName();
                    }
                    j++;
                }
                if (parameter_doc.length() > 0) {
                    parameters = parameter_doc;
                }

                int[] types = new int[typeList.size()];
                for (int i = 0; i < typeList.size(); i++) {
                    types[i] = typeList.get(i);
                }
                String shortName = "CLIJ_" + method.getName();
                String key = shortName + (types.length + 1);
                if (makeAvailable && !methodMap.containsKey(key)) {
                    MethodInfo methodInfo = new MethodInfo(
                            new ExtensionDescriptor(shortName, types, this),
                            method,
                            parameters,
                            shortName
                    );
                    //.add(methodInfo.extensionDescriptor);
                    methodMap.put(key, methodInfo);
                }
            }
        }
    }


    public static void main(String... args) {
        new ImageJ();
        CLIJMacroExtensions ext = new CLIJMacroExtensions();
        ext.clij = ClearCLIJ.getInstance("gfx902");
        ext.getExtensionFunctions();

        IJ.open("C:/structure/code/haesleinhuepf_clearclij/src/main/resources/flybrain.tif");
        ext.toCLIJ("flybrain.tif");
        IJ.getImage().setTitle("out");
        ext.toCLIJ("out");

        Object[] arguments = new Object[] {
            "flybrain.tif",
                "out",
                new Double(3),
                new Double(3),
                new Double(1)
        };
        ext.handleExtension("CLIJ_mean3d", arguments);
        //ext.handleExtension("CLIJ_erode", arguments);

        ext.fromCLIJ("out");

        ext.handleExtension("CLIJ_clear", new Object[]{});
    }


    static int radiusToKernelSize(int radius) {
        int kernelSize = radius * 2 + 1;
        return kernelSize;
    }
}
