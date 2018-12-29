package net.haesleinhuepf.clij.macro.documentation;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.utilities.CLIJUtilities;

public class HTMLDocumentationTemplate {
    private String description;
    private String availableForDimensions;
    private String sourceUrl;
    private String headline;
    private String parameterHelpText;

    public HTMLDocumentationTemplate(String description, String availableForDimensions, CLIJMacroPlugin source) {
        this.description = description;
        this.availableForDimensions = availableForDimensions;
        this.sourceUrl = DocumentationUtilities.rootSourceUrl + source.getClass().getName().replace(",", "/") + ".java";
        this.headline = CLIJUtilities.classToName(source.getClass());
        this.parameterHelpText = source.getParameterHelpText();
    }

    public String toString() {
        String output = "<a name=\"" + headline + "\"></a>" +
                "<b>" + headline + "</b><br/>" +
                description.replace("\n", "<br/>") + "<br/><br/>" +
                "Parameters: " + parameterHelpText + "<br/>" +
                "Available for: " + availableForDimensions + "<br/><br/>";

        return output;
    }
}
