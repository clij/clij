package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_affineTransform2D")
public class AffineTransform2D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        net.imglib2.realtransform.AffineTransform2D at = new net.imglib2.realtransform.AffineTransform2D();
        Object[] args = openCLBufferArgs();
        String[] transformCommands = ((String)(args[2])).trim().toLowerCase().split(" ");

        for(String transformCommand : transformCommands) {
            String[] commandParts = transformCommand.split("=");
            //System.out.print("Command: " + commandParts[0]);
            if (commandParts[0].compareTo("center") == 0) {
                ClearCLBuffer input = (ClearCLBuffer) args[0];
                at.translate(-input.getWidth() / 2, -input.getHeight() / 2);
            } else if (commandParts[0].compareTo("-center") == 0) {
                ClearCLBuffer input = (ClearCLBuffer) args[0];
                at.translate(input.getWidth() / 2, input.getHeight() / 2);
            } else if (commandParts[0].compareTo("scale") == 0) {
                at.scale(1.0 / Double.parseDouble(commandParts[1]));
            } else if (commandParts[0].compareTo("scalex") == 0) {
                net.imglib2.realtransform.AffineTransform2D scaleTransform = new net.imglib2.realtransform.AffineTransform2D();
                scaleTransform.set(1.0 / Double.parseDouble(commandParts[1]),0,0);
                scaleTransform.set(1.0 , 1, 1);
                at.concatenate(scaleTransform);
            } else if (commandParts[0].compareTo("scaley") == 0) {
                net.imglib2.realtransform.AffineTransform2D scaleTransform = new net.imglib2.realtransform.AffineTransform2D();
                scaleTransform.set(1.0,0,0);
                scaleTransform.set(1.0  / Double.parseDouble(commandParts[1]) , 1, 1);
                at.concatenate(scaleTransform);
            } else if (commandParts[0].compareTo("rotate") == 0 || commandParts[0].compareTo("rotate") == 0) {
                float angle = (float)(-asFloat(commandParts[1]) / 180.0f * Math.PI);
                at.rotate(angle);
            } else if (commandParts[0].compareTo("translatex") == 0) {
                at.translate(Double.parseDouble(commandParts[1]), 0, 0);
            } else if (commandParts[0].compareTo("translatey") == 0) {
                at.translate(0,Double.parseDouble(commandParts[1]), 0);
            } else if (commandParts[0].compareTo("shearxy") == 0) {
                double shear = Double.parseDouble(commandParts[1]);
                net.imglib2.realtransform.AffineTransform2D shearTransform = new net.imglib2.realtransform.AffineTransform2D();
                shearTransform.set(1.0, 0, 0 );
                shearTransform.set(1.0, 1, 1 );
                shearTransform.set(shear, 0, 1);
                //shearTransform.set(shear, 0, 2);
                at.concatenate(shearTransform);
            } else {
                System.out.print("Unknown transform: " + commandParts[0]);
            }
        }

        if (!clij.hasImageSupport()) {
            ClearCLBuffer input = ((ClearCLBuffer) args[0]);
            ClearCLBuffer output = ((ClearCLBuffer) args[1]);

            return Kernels.affineTransform2D(clij, input, output, net.haesleinhuepf.clij.utilities.AffineTransform.matrixToFloatArray2D(at));
        } else {
            ClearCLImage input = CLIJHandler.getInstance().getChachedImageByBuffer((ClearCLBuffer) args[0]);
            ClearCLImage output = CLIJHandler.getInstance().getChachedImageByBuffer((ClearCLBuffer) args[1]);

            boolean result = Kernels.affineTransform2D(clij, input, output, net.haesleinhuepf.clij.utilities.AffineTransform.matrixToFloatArray2D(at));

            Kernels.copy(clij, output, (ClearCLBuffer) args[1]);

            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, String transform";
    }

    @Override
    public String getDescription() {
        return "Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.\n\n" +
                "Supported transforms:" +
                "\n* center: translate the coordinate origin to the center of the image" +
                "\n* -center: translate the coordinate origin back to the initial origin" +
                "\n* rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees" +
                "\n* scale=[factor]: isotropic scaling according to given zoom factor" +
                "\n* scaleX=[factor]: scaling along X-axis according to given zoom factor" +
                "\n* scaleY=[factor]: scaling along Y-axis according to given zoom factor" +
                "\n* shearXY=[factor]: shearing along X-axis in XY plane according to given factor" +
                "\n* translateX=[distance]: translate along X-axis by distance given in pixels" +
                "\n* translateY=[distance]: translate along X-axis by distance given in pixels" +
                "\n\nExample transform:" +
                "\ntransform = \"center scale=2 rotate=45 -center\";";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }
}
