package net.haesleinhuepf.clij.macro.documentation;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.utilities.CLIJUtilities;


public class HTMLDocumentationTemplate {
    private String description;
    private String availableForDimensions;
    private String sourceUrl;
    private String headline;
    private String parameterHelpText;
    private String docsUrl;
    private String clijUrl;

    public HTMLDocumentationTemplate(String description, String availableForDimensions, CLIJMacroPlugin source) {
        this.description = description;
        this.availableForDimensions = availableForDimensions;
        this.sourceUrl = DocumentationUtilities.rootSourceUrl + source.getClass().getName().replace(".", "/") + ".java";
        this.headline = CLIJUtilities.classToName(source.getClass());
        this.parameterHelpText = source.getParameterHelpText();
        this.docsUrl = DocumentationUtilities.docsSourceUrl + "reference#" + headline;
        this.clijUrl = DocumentationUtilities.clijRootUrl;
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean includeLinks) {
        String output = "";

        if (includeLinks) {
            output = output +
                    "<b><a href=\"" + docsUrl + "\">" + headline + "</a></b><br/><br/>";
        }
        output = output +
                description.replace("\n", "<br/>") + "<br/><br/>" +
                "Parameters: " + parameterHelpText + "<br/>" +
                "Available for: " + availableForDimensions + "<br/><br/>";

        if (includeLinks) {
            output = output +
                    "<a href=\"" + docsUrl + "\">Documentation</a><br/>" +
                    "<a href=\"" + sourceUrl + "\">Source</a><br/>" +
                    "<a href=\"" + clijUrl + "\">clij on the web</a><br/>";
        }
        return output;
    }
}
