package net.haesleinhuepf.clij.macro;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import ij.IJ;
import ij.ImagePlus;
import ij.Macro;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.macro.ExtensionDescriptor;
import ij.macro.MacroExtension;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.kernels.Kernels;

import java.util.ArrayList;
import java.util.Arrays;
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

    // CLIJ clij;
    HashMap<String, ClearCLBuffer> bufferMap = new HashMap<String, ClearCLBuffer>();

    //public void setCLIJ(CLIJ clij) {
    //    this.clij = clij;
    //}

    //public CLIJ getCLIJ() {
        //if (clij == null) {
        //    clij = CLIJ.getInstance();
        //}
        //return clij;
    //}

    public void setPluginService(CLIJMacroPluginService pluginService) {
        this.pluginService = pluginService;
    }

    public CLIJMacroPluginService getPluginService() {
        return pluginService;
    }

    @Override
    public String handleExtension(String name, Object[] args) {

        ElapsedTime.measureForceOutput("Whole extension handling", () -> {
            ArrayList<Integer> existingImageIndices = new ArrayList<Integer>();
            HashMap<Integer, String> missingImageIndices = new HashMap<Integer, String>();
            HashMap<Integer, String> missingImageIndicesDescriptions = new HashMap<Integer, String>();

            CLIJMacroPlugin plugin = null;
            //System.out.println("Handle Ext " + name);
            try {
                plugin = pluginService.getCLIJMacroPlugin(name);

                //System.out.println("plugins " + CLIJHandler.getInstance().pluginService.getCLIJMethodNames().size());

                if (plugin == null) {
                    // this should never happen, because Macro extensions do a similar check before calling this method
                    System.out.println("Method not found: " + name);
                    return;
                }

                //System.out.println("Check method: " + name);
                String[] pluginParameters = plugin.getParameterHelpText().split(",");
                Object[] parsedArguments = new Object[0];
                if (args != null) {
                    parsedArguments = new Object[args.length];
                    for (int i = 0; i < args.length; i++) {
                        //System.out.println("Parsing args: " + args[i] + " " + args[i].getClass());
                        if (args[i] instanceof Double) {
                            //System.out.println("numeric");
                            parsedArguments[i] = args[i];
                        } else {
                            if (pluginParameters[i].trim().startsWith("Image")) {
                                //System.out.println("not numeric");
                                ClearCLBuffer bufferImage = bufferMap.get(args[i]);
                                if (bufferImage == null) {
                                    //IJ.log("Warning: Image \"" + args[i] + "\" doesn't exist in GPU memory. Try this:");
                                    //IJ.log("Ext.CLIJ_push(\"" + args[i] + "\");");
                                    missingImageIndices.put(i, (String) args[i]);
                                    missingImageIndicesDescriptions.put(i, pluginParameters[i]);
                                    parsedArguments[i] = (String) args[i];
                                } else {
                                    existingImageIndices.add(i);
                                    parsedArguments[i] = bufferImage;
                                }
                            } else {
                                parsedArguments[i] = args[i];
                            }
                        }
                        //System.out.println("Parsed args: " + parsedArguments[i + 1]);
                    }
                }
                // create missing images by making images as given images
            /*if (existingImageIndices.size() > 0) {
                for (int i : missingImageIndices.keySet()) {
                    String nameInCache = missingImageIndices.get(i);
                    if (bufferMap.keySet().contains(nameInCache)) {
                        parsedArguments[i] = bufferMap.get(nameInCache);
                    } else {
                        parsedArguments[i] = clij.createCLBuffer((ClearCLBuffer) parsedArguments[existingImageIndices.get(0)]);
                        bufferMap.put(nameInCache, (ClearCLBuffer) parsedArguments[i]);
                    }
                }
            }*/

                if (CLIJ.debug) {
                    System.out.println("Invoking plugin " + name + " " + Arrays.toString(args));
                }
                plugin.setClij(CLIJ.getInstance());

                // fill missing images
                //if (existingImageIndices.size() > 0) {
                for (int i : missingImageIndices.keySet()) {
                    String nameInCache = missingImageIndices.get(i);
                    String parameterDescription = missingImageIndicesDescriptions.get(i);
                    if (parameterDescription.toLowerCase().contains("destination")) { // only generate destination images
                        if (bufferMap.keySet().contains(nameInCache)) {
                            parsedArguments[i] = bufferMap.get(nameInCache);
                        } else {
                            // copy first to hand over all parameters as they came
                            plugin.setArgs(parsedArguments);
                            ClearCLBuffer template = null;
                            if (existingImageIndices.size() > 0) {
                                template = (ClearCLBuffer) parsedArguments[existingImageIndices.get(0)];
                            }
                            parsedArguments[i] = CLIJHandler.getInstance().getFromCacheOrCreateByPlugin(nameInCache, plugin, template);
                        }
                    }
                }
                //}

                // hand over complete parameters again
                plugin.setArgs(parsedArguments);

                // check if all requested images are set.
                boolean allImagesSet = true;
                int i = 0;
                for(String parameter : pluginParameters) {
                    if (parameter.startsWith("Image")) {
                        if (!(parsedArguments[i] instanceof ClearCLBuffer)) {
                            String parameterName = parameter.split(" ")[1];
                            GenericDialog gd = new GenericDialog(plugin.getName() + " Error");
                            gd.addMessage("Error when calling " + plugin.getName() + ": " +
                                    "The image parameter " + parameterName+ "('" + parsedArguments[i] + "') doesn't exist in GPUs memory. Consider calling\n\n" +
                                    "Ext.CLIJ_push(\"" + parsedArguments[i] + "\");");
                            gd.showDialog();

                            if (CLIJ.debug) {
                                System.out.println("Couldn't execute CLIJ plugin: Image '" + parameterName+ "' not found in GPU memory!");
                            }
                            Macro.abort();
                            allImagesSet = false;
                            break;
                        }
                    }
                    i++;
                }

                if (allImagesSet) {
                    if (plugin instanceof CLIJOpenCLProcessor) {
                        ((CLIJOpenCLProcessor) plugin).executeCL();
                    } else {

                        if (CLIJ.debug) {
                            System.out.println("Couldn't execute CLIJ plugin!");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    @Deprecated
    void putInCache(String nameInCache, ClearCLBuffer buffer) {
        System.out.println("Putting " + nameInCache);
        bufferMap.put(nameInCache, buffer);
    }

    ClearCLBuffer getFromCacheOrCreateByPlugin(String nameInCache, CLIJMacroPlugin plugin, ClearCLBuffer template) {
        if (bufferMap.containsKey(nameInCache)) {
            return bufferMap.get(nameInCache);
        } else {
            ClearCLBuffer buffer = plugin.createOutputBufferFromSource(template);
            bufferMap.put(nameInCache, buffer);
            return buffer;
        }
    }

    public void releaseBufferInGPU(String arg) {
        System.out.println("Releasing buffer " + arg);
        if (CLIJ.debug) {
            System.out.println("Releasing " + arg);
        }
        ClearCLBuffer buffer = bufferMap.get(arg);
        if (bufferAsImageMap.containsKey(buffer)) {
            System.out.println("Releasing image " + arg);
            ClearCLImage image = bufferAsImageMap.get(buffer);
            image.close();
            bufferAsImageMap.remove(buffer);
        }


        buffer.close();
        bufferMap.remove(arg);
    }

    public void clearGPU() {
        System.out.println("Clearing ");
        ArrayList<String> keysToRelease = new ArrayList<String>();
        for (String key : bufferMap.keySet()) {
            keysToRelease.add(key);
        }
        for (String key : keysToRelease) {
            releaseBufferInGPU(key);
        }
        bufferMap.clear();
    }

    public void pullFromGPU(String arg) {
        ClearCLBuffer buffer = bufferMap.get(arg);
        CLIJ.getInstance().show(buffer, arg);
    }

    public ClearCLBuffer pushToGPU(String arg) {
        ImagePlus imp = WindowManager.getImage(arg);
        imp.changes = false;

        ClearCLBuffer temp = CLIJ.getInstance().convert(imp, ClearCLBuffer.class);
        if (bufferMap.containsKey(arg)) {
            ClearCLBuffer preExistingBuffer = bufferMap.get(arg);

            if (
                    temp.getWidth() == preExistingBuffer.getWidth() &&
                            temp.getHeight() == preExistingBuffer.getHeight() &&
                            temp.getDepth() == preExistingBuffer.getDepth() &&
                            temp.getNativeType() == preExistingBuffer.getNativeType()
            ) {
                System.out.println("Overwriting image in cache.");
                Kernels.copy(CLIJ.getInstance(), temp, preExistingBuffer);
                temp.close();
            } else {
                System.out.println("Dropping image in cache.");
                releaseBufferInGPU(arg);
            }
        }
        if (!bufferMap.containsKey(arg)) {
            bufferMap.put(arg, temp);
            return temp;
        } else {
            return bufferMap.get(arg);
        }
    }

    @Override
    public ExtensionDescriptor[] getExtensionFunctions() {
        int numberOfPlugins = (pluginService != null)?pluginService.getCLIJMethodNames().size():0;

        ExtensionDescriptor[] extensions = new ExtensionDescriptor[numberOfPlugins];

        int i = 0;
        if (pluginService != null) {
            for (String name : pluginService.getCLIJMethodNames()) {
                extensions[i] = pluginService.getPluginExtensionDescriptor(name);
                i++;
            }
        }

        return extensions;
    }

    public String reportGPUMemory() {
        StringBuilder stringBuilder = new StringBuilder();
        long bytesSum = 0;
        boolean foundBufferAsImage = false;
        stringBuilder.append("GPU contains " + (bufferMap.keySet().size() + bufferAsImageMap.size() )+ " images.\n");
        for (String key : bufferMap.keySet()) {
            ClearCLBuffer buffer = bufferMap.get(key);
            stringBuilder.append("- " + key + "[" + buffer.getPeerPointer() + "] " + humanReadableBytes(buffer.getSizeInBytes()) + "\n");
            if (bufferAsImageMap.containsKey(buffer)) {
                ClearCLImage image = bufferAsImageMap.get(buffer);
                stringBuilder.append("- " + key + "[" + image.getPeerPointer() + "]* " + humanReadableBytes(image.getSizeInBytes()) + "\n");
                bytesSum = bytesSum + image.getSizeInBytes();
                foundBufferAsImage = true;
            }
            bytesSum = bytesSum + buffer.getSizeInBytes();
        }
        stringBuilder.append("= " + humanReadableBytes(bytesSum) +"\n");
        if (foundBufferAsImage) {
            stringBuilder.append("* some images are stored twice for technical reasons.\n");
        }

        return stringBuilder.toString();
    }

    private String humanReadableBytes(long bytesSum) {
        if (bytesSum > 1024) {
            bytesSum = bytesSum / 1024;
            if (bytesSum > 1024) {
                bytesSum = bytesSum / 1024;
                if (bytesSum > 1024) {
                    bytesSum = bytesSum / 1024;
                    return (bytesSum + " Gb");
                } else {
                    return (bytesSum + " Mb");
                }
            } else {
                return (bytesSum + " kb");
            }
        }
        return bytesSum + " b";
    }

    HashMap<ClearCLBuffer, ClearCLImage> bufferAsImageMap = new HashMap<ClearCLBuffer, ClearCLImage>();
    //public void cacheImageWithBuffer(ClearCLBuffer buffer, ClearCLImage image) {
    //
    //}
    public ClearCLImage getChachedImageByBuffer(ClearCLBuffer buffer) {
        if (bufferAsImageMap.containsKey(buffer)) {
            ClearCLImage image = bufferAsImageMap.get(buffer);
            System.out.println("Found the buffer, return its image");
            Kernels.copy(CLIJ.getInstance(), buffer, image);
            return image;
        }
        ClearCLImage image = CLIJ.getInstance().convert(buffer, ClearCLImage.class);
        bufferAsImageMap.put(buffer, image);
        return image;
    }
}
