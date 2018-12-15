package net.haesleinhuepf.imagej.macro;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.macro.ExtensionDescriptor;
import ij.macro.MacroExtension;
import net.haesleinhuepf.imagej.ClearCLIJ;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * CLIJHandler
 * <p>
 * Author: @haesleinhuepf
 * December 2018
 */
public class CLIJHandler implements MacroExtension {
    static CLIJHandler instance = null;
    private CLIJMacroPluginService pluginService = null;

    public static CLIJHandler getInstance() {
        if (instance == null) {
            instance = new CLIJHandler();
        }
        return instance;
    }

    ClearCLIJ clij;
    HashMap<String, ClearCLBuffer> bufferMap = new HashMap<String, ClearCLBuffer>();

    final String TO_CLIJ = "CLIJ_push";
    final String FROM_CLIJ = "CLIJ_pull";
    final String RELEASE_BUFFER = "CLIJ_release";
    final String CLEAR_CLIJ = "CLIJ_clear";
    final String HELP = "CLIJ_help";


    public void setCLIJ(ClearCLIJ clij) {
        this.clij = clij;
    }

    public ClearCLIJ getCLIJ() {
        if (clij == null) {
            clij = ClearCLIJ.getInstance();
        }
        return clij;
    }

    public void setPluginService(CLIJMacroPluginService pluginService) {
        this.pluginService = pluginService;

    }

    @Override
    public String handleExtension(String name, Object[] args) {
        ArrayList<Integer> existingImageIndices = new ArrayList<Integer>();
        HashMap<Integer, String> missingImageIndices = new HashMap<Integer, String>();
        CLIJMacroPlugin plugin = null;
        //System.out.println("Handle Ext " + name);
        try {
            if (name.equals(TO_CLIJ)) {
                pushToGPU((String) args[0]);
                return null;
            } else if (name.equals(FROM_CLIJ)) {
                pullFromGPU((String) args[0]);
                return null;
            } else if (name.equals(RELEASE_BUFFER)) {
                releaseBuffer((String) args[0]);
                return null;
            } else if (name.equals(CLEAR_CLIJ)) {
                clearGPU();
                return null;
            } else if (name.equals(HELP)) {
                help(args);
                return null;
            } else if (pluginService != null) {
                plugin = pluginService.clijMacroPlugin(name);
            }

            System.out.println("plugins " + CLIJHandler.getInstance().pluginService.getCLIJMethodNames().size());

            if (plugin == null) {
                //System.out.println("Method not found: " + name);
                return "Error: Plugin not found!";
            }

            //System.out.println("Check method: " + name);
            String[] pluginParameters = plugin.getParameterHelpText().split(",");
            Object[] parsedArguments = new Object[args.length + 1];
            parsedArguments[0] = clij;
            for (int i = 0; i < args.length; i++) {
                //System.out.println("Parsing args: " + args[i] + " " + args[i].getClass());
                if (args[i] instanceof Double) {
                    //System.out.println("numeric");
                    parsedArguments[i + 1] = args[i];
                } else {
                    if (pluginParameters[i].trim().startsWith("Image")) {
                        //System.out.println("not numeric");
                        ClearCLBuffer bufferImage = bufferMap.get(args[i]);
                        if (bufferImage == null) {
                            //IJ.log("Warning: Image \"" + args[i] + "\" doesn't exist in GPU memory. Try this:");
                            //IJ.log("Ext.CLIJ_push(\"" + args[i] + "\");");
                            missingImageIndices.put(i + 1, (String) args[i]);
                        } else {
                            existingImageIndices.add(i + 1);
                        }
                        parsedArguments[i + 1] = bufferImage;
                    } else {
                        parsedArguments[i + 1] = args[i];
                    }
                }
                //System.out.println("Parsed args: " + parsedArguments[i + 1]);
            }

            // create missing images by making images as given images
            if (existingImageIndices.size() > 0) {
                for (int i : missingImageIndices.keySet()) {
                    String nameInCache = missingImageIndices.get(i);
                    if (bufferMap.keySet().contains(nameInCache)) {
                        parsedArguments[i] = bufferMap.get(nameInCache);
                    } else {
                        parsedArguments[i] = clij.createCLBuffer((ClearCLBuffer) parsedArguments[existingImageIndices.get(0)]);
                        bufferMap.put(nameInCache, (ClearCLBuffer) parsedArguments[i]);
                    }
                }
            }

            System.out.println("Invoking plugin " + name);
            plugin.setClij(clij);
            Object[] arguments = new Object[parsedArguments.length - 1];
            System.arraycopy(parsedArguments, 1, arguments, 0, arguments.length);
            plugin.setArgs(arguments);
            if (plugin instanceof CLIJOpenCLProcessor) {
                ((CLIJOpenCLProcessor) plugin).executeCL();
            } else {
                System.out.println("Couldn't execute CLIJ plugin!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void help(Object[] args) {
        //IJ.log("Help");
        String searchString = "";
        if (args.length > 0) {
            searchString = (String) (args[0]);
        }
        ArrayList<String> helpList = new ArrayList<String>();

        for (String name : pluginService.getCLIJMethodNames()) {
            if (searchString.length() == 0 || name.contains(searchString)) {

                helpList.add(name + "(" + pluginService.clijMacroPlugin(name).getParameterHelpText() + ")");
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

    private void clearGPU() {
        ArrayList<String> keysToRelease = new ArrayList<String>();
        for (String key : bufferMap.keySet()) {
            keysToRelease.add(key);
        }
        for (String key : keysToRelease) {
            releaseBuffer(key);
        }
        bufferMap.clear();
    }

    private void pullFromGPU(String arg) {
        ClearCLBuffer buffer = bufferMap.get(arg);
        clij.show(buffer, arg);
    }

    private void pushToGPU(String arg) {
        ImagePlus imp = WindowManager.getImage(arg);
        bufferMap.put(arg, clij.converter(imp).getClearCLBuffer());
    }

    @Override
    public ExtensionDescriptor[] getExtensionFunctions() {

        int numberOfPredefinedExtensions = 5;

        int numberOfPlugins = (pluginService != null)?pluginService.getCLIJMethodNames().size():0;

        ExtensionDescriptor[] extensions = new ExtensionDescriptor[numberOfPredefinedExtensions + numberOfPlugins];

        extensions[0] = new ExtensionDescriptor(TO_CLIJ, new int[]{MacroExtension.ARG_STRING}, this);
        extensions[1] = new ExtensionDescriptor(FROM_CLIJ, new int[]{MacroExtension.ARG_STRING}, this);
        extensions[2] = new ExtensionDescriptor(RELEASE_BUFFER, new int[]{MacroExtension.ARG_STRING}, this);
        extensions[3] = new ExtensionDescriptor(CLEAR_CLIJ, new int[]{}, this);
        extensions[4] = new ExtensionDescriptor(HELP, new int[]{MacroExtension.ARG_STRING}, this);

        int i = numberOfPredefinedExtensions;
        if (pluginService != null) {
            for (String name : pluginService.getCLIJMethodNames()) {
                extensions[i] = pluginService.getPluginExtensionDescriptor(name);
                i++;
            }
        }

        return extensions;
    }
}
