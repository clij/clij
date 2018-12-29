package net.haesleinhuepf.clij.macro;

import net.haesleinhuepf.clij.macro.documentation.HTMLDocumentationTemplate;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
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

            CLIJMacroPlugin plugin = pluginService.getCLIJMacroPlugin(key);
            String headline =
                    "Ext." + key + "(" +
                    plugin.getParameterHelpText() +
                    ");";

            String description = headline;

            if (plugin instanceof OffersDocumentation) {
                description = "<html><body>" +
                        new HTMLDocumentationTemplate(((OffersDocumentation) plugin).getDescription(), ((OffersDocumentation) plugin).getAvailableForDimensions(), plugin).toString() +
                        "</body></html>";

            }

            BasicCompletion basicCompletion = new BasicCompletion(completionProvider, headline, null, description);
            completions.add(basicCompletion);
        }

        return completions;
    }
}
