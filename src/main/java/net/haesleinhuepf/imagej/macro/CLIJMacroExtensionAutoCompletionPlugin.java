package net.haesleinhuepf.imagej.macro;

import net.imagej.legacy.plugin.MacroExtensionAutoCompletionPlugin;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * CLIJMacroExtensionAutoCompletionPlugin
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = MacroExtensionAutoCompletionPlugin.class)
public class CLIJMacroExtensionAutoCompletionPlugin implements MacroExtensionAutoCompletionPlugin {

    @Override
    public List<BasicCompletion> getCompletions(CompletionProvider completionProvider) {
        CLIJMacroPluginService pluginService = CLIJHandler.getInstance().getPluginService();

        ArrayList<BasicCompletion> completions = new ArrayList<BasicCompletion>();
        for (String key : pluginService.getCLIJMethodNames()) {
            System.out.println("parsing " + key);
            String headline =
                    "Ext." + key + "(" +
                    pluginService.clijMacroPlugin(key).getParameterHelpText() +
                    ");";

            String description = headline;

            BasicCompletion basicCompletion = new BasicCompletion(completionProvider, headline, null, description);
            completions.add(basicCompletion);
        }

        return completions;
    }
}
