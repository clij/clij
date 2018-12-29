package net.haesleinhuepf.clij.macro.documentation;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.utilities.CLIJUtilities;

public class MarkDownDocumentationTemplate {
    private String description;
    private String availableForDimensions;
    private String sourceUrl;
    private String headline;
    private String parameterHelpText;

    public MarkDownDocumentationTemplate(String description, String availableForDimensions, CLIJMacroPlugin source) {
        this.description = description;
        this.availableForDimensions = availableForDimensions;
        this.sourceUrl = DocumentationUtilities.rootSourceUrl + source.getClass().getName().replace(",", "/") + ".java";
        this.headline = CLIJUtilities.classToName(source.getClass());
        this.parameterHelpText = source.getParameterHelpText();
    }

    public String toString() {
        String output = "## " + headline + "\n\n" +
                description + "\n\n" +
                "Parameters: " + parameterHelpText + "\n" +
                "Available for: " + availableForDimensions + "\n" +
                "[Source](" + sourceUrl + ")\n\n";

        return output;
    }
}
