package net.haesleinhuepf.clij.macro.documentation;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.utilities.CLIJUtilities;

public class MarkDownDocumentationTemplate {
    private String name;
    private String description;
    private String availableForDimensions;
    private String sourceUrl;
    private String headline;
    private String parameterHelpText;

    public MarkDownDocumentationTemplate(String description, String availableForDimensions, CLIJMacroPlugin source) {
        this.name = source.getName();
        this.description = description;
        this.availableForDimensions = availableForDimensions;
        this.sourceUrl = DocumentationUtilities.rootSourceUrl + source.getClass().getName().replace(".", "/") + ".java";
        this.headline = CLIJUtilities.classToName(source.getClass());
        this.parameterHelpText = source.getParameterHelpText();
    }

    public String toString() {
        String output = "## " + headline + "\n\n" +
                description + "\n\n" +
                "**Parameters**: " + parameterHelpText + "\n\n" +
                "**Available for**: " + availableForDimensions + "\n\n" +
                "**Macro example**: \n" +
                "```\n" +
                generateExampleCode() +
                "```\n" +
                "[Link to source](" + sourceUrl + ")\n\n";

        return output;
    }

    private String generateExampleCode() {
        StringBuilder pushCode = new StringBuilder();
        StringBuilder pullCode = new StringBuilder();
        StringBuilder macroCode = new StringBuilder();
        macroCode.append("Ext." + name + "(");

        int count = 0;
        for (String parameter : parameterHelpText.split(",")) {
            if (parameter.length() > 0) {
                String[] parameterParts = parameter.trim().split(" ");
                if (count > 0) {
                    macroCode.append(", ");
                }
                macroCode.append(parameterParts[1]);

                if (parameter.contains("Image")) {
                    if (parameter.contains("destination")) {
                        pullCode.append("Ext.CLIJ_pull(" + parameterParts[1] + ");\n");
                    } else {
                        pushCode.append("Ext.CLIJ_push(" + parameterParts[1] + ");\n");
                    }
                }
                count++;
            }
        }
        macroCode.append(");\n");

        if (name.compareTo("release") == 0 ||
                name.compareTo("push") == 0 ||
                name.compareTo("pull") == 0 ||
                name.compareTo("clear") == 0 ||
                name.compareTo("help") == 0) {
            pushCode = new StringBuilder();
            pullCode = new StringBuilder();
        }


        String result = "run(\"CLIJ Macro Extensions\", \"cl_device=\");\n" +
                pushCode.toString() +
                macroCode.toString() +
                pullCode.toString();

        return result;
    }
}
