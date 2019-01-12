package net.haesleinhuepf.clij.codegenerator;

import net.haesleinhuepf.clij.kernels.Kernels;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * OpGenerator
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 01 2019
 */
public class OpGenerator {
    public static void main(String ... args) throws IOException {
        File inputFile = new File("src/main/java/net/haesleinhuepf/clij/kernels/Kernels.java");

        BufferedReader br = new BufferedReader(new FileReader(inputFile));

        StringBuilder builder = new StringBuilder();
        builder.append("package net.haesleinhuepf.clij.utilities;\n");
        builder.append("import net.haesleinhuepf.clij.CLIJ;\n");
        builder.append("import net.haesleinhuepf.clij.kernels.Kernels;\n");

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("import")) {
                builder.append(line);
                builder.append("\n");
            }
            if (line.startsWith("public class")) {
                break;
            }
        }

        builder.append("// this is generated code. See src/test/java/net/haesleinhuepf/clij/codegenerator for details\n");
        builder.append("public class CLIJOps {\n");
        builder.append("   private CLIJ clij;\n");
        builder.append("   public CLIJOps(CLIJ clij) {\n");
        builder.append("       this.clij = clij;\n");
        builder.append("   }\n");

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("public")) {
                String[] temp = line.split("\\(");

                String methodDefinition = temp[0].trim();
                String parameters = temp[1].trim();
                parameters = parameters.replace(")", "");
                parameters = parameters.replace("{", "");

                temp = methodDefinition.split(" ");
                String methodName = temp[temp.length - 1];
                String returnType = temp[temp.length - 2];

                builder.append("    public " + returnType + " " + methodName + "(");

                temp = parameters.split(",");
                StringBuilder parametersForCall = new StringBuilder();
                parametersForCall.append("clij");

                int count = 0;
                for (String parameter : temp) {
                    String[] temp2 = parameter.trim().split(" ");
                    if (count > 0) {
                        parametersForCall.append(", ");
                        parametersForCall.append(temp2[temp2.length - 1]);

                        if (count > 1) {
                            builder.append(", ");
                        }
                        builder.append(parameter);
                    }
                    count++;
                }
                builder.append(") {\n");
                builder.append("        return Kernels." + methodName + "(");
                builder.append(parametersForCall.toString());
                builder.append(");\n");
                builder.append("    }\n\n");
            }
        }
        builder.append("}\n");

        File outputTarget = new File("src/main/java/net/haesleinhuepf/clij/utilities/CLIJOps.java");

        FileWriter writer = new FileWriter(outputTarget);
        writer.write(builder.toString());
        writer.close();

    }
}
