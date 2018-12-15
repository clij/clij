package net.haesleinhuepf.imagej.macro;


import ij.macro.ExtensionDescriptor;
import ij.macro.MacroExtension;
import net.imagej.ImageJService;
import org.scijava.plugin.AbstractPTService;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginInfo;
import org.scijava.service.Service;

import java.util.HashMap;
import java.util.Set;

@Plugin(type = Service.class)
public class CLIJMacroPluginService  extends AbstractPTService<CLIJMacroPlugin> implements ImageJService {

    private HashMap<String, PluginInfo<CLIJMacroPlugin>> animals = new HashMap<>();

    @Override
    public void initialize() {
        for (final PluginInfo<CLIJMacroPlugin> info : getPlugins()) {
            String name = info.getName();
            if (name == null || name.isEmpty()) {
                name = info.getClassName();
            }
            animals.put(name, info);
        }
        CLIJHandler.getInstance().setPluginService(this);
    }


    public Set<String> getCLIJMethodNames() {
        return animals.keySet();
    }

    public CLIJMacroPlugin clijMacroPlugin(final String name) {
        final PluginInfo<CLIJMacroPlugin> info = animals.get(name);

        if (info == null) {
            throw new IllegalArgumentException("No animal of that name");
        }

        final CLIJMacroPlugin animal = pluginService().createInstance(info);

        return animal;
    }

    public ExtensionDescriptor getPluginExtensionDescriptor(String name){
        final PluginInfo<CLIJMacroPlugin> info = animals.get(name);

        if (info == null) {
            throw new IllegalArgumentException("No animal of that name");
        }

        final CLIJMacroPlugin animal = pluginService().createInstance(info);
        String[] parameters = animal.getParameterHelpText().split(",");
        int[] parameterTypes = new int[parameters.length];
        int i = 0;
        for (String parameter : parameters) {
            if (parameter.trim().startsWith("Image")) {
                parameterTypes[i] = MacroExtension.ARG_STRING;
            } else if (parameter.trim().startsWith("String")) {
                parameterTypes[i] = MacroExtension.ARG_STRING;
            } else {
                parameterTypes[i] = MacroExtension.ARG_NUMBER;
            }
            i++;
        }
        return new ExtensionDescriptor(name, parameterTypes, CLIJHandler.getInstance());
    }

    @Override
    public Class<CLIJMacroPlugin> getPluginType() {
        return CLIJMacroPlugin.class;
    }
}
