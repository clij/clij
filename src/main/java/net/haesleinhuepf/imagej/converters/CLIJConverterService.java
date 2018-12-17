package net.haesleinhuepf.imagej.converters;

import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.imagej.ImageJService;
import org.scijava.plugin.AbstractPTService;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.PluginInfo;
import org.scijava.service.Service;

import java.util.HashMap;

/**
 * CLIJConverterService
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = Service.class)
public class CLIJConverterService extends AbstractPTService<CLIJConverterPlugin> implements ImageJService {

    private HashMap<Class, PluginInfo<CLIJConverterPlugin>> converterPluginsBySource = new HashMap<>();
    private HashMap<Class, PluginInfo<CLIJConverterPlugin>> converterPluginsByTarget = new HashMap<>();

    @Override
    public void initialize() {
        for (final PluginInfo<CLIJConverterPlugin> info : getPlugins()) {
            final CLIJConverterPlugin plugin = pluginService().createInstance(info);
            converterPluginsBySource.put(plugin.getSourceType(), info);
            converterPluginsByTarget.put(plugin.getTargetType(), info);
        }
    }

    @Override
    public Class<CLIJConverterPlugin> getPluginType() {
        return CLIJConverterPlugin.class;
    }



}
