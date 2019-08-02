package net.haesleinhuepf.clij.jython;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.utilities.CLIJOps;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.roi.Regions;
import net.imglib2.view.Views;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.RandomAccess;

/**
 * ScriptingAutoCompleteProvider
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 07 2018
 */
public class ScriptingAutoCompleteProvider extends DefaultCompletionProvider
{
    private static ScriptingAutoCompleteProvider instance = null;

    public static ScriptingAutoCompleteProvider getInstance () {
        if (instance == null) {
            instance = new ScriptingAutoCompleteProvider();
        }
        return instance;
    }

    private ScriptingAutoCompleteProvider() {
        addClassToAutoCompletion(CLIJOps.class, "clij.op().");
        addClassToAutoCompletion(CLIJ.class, "clij.");
        addClassToAutoCompletion(ClearCLBuffer.class, "buffer.");

        addClassToAutoCompletion(ImagePlus.class, "imp.");
        addClassToAutoCompletion(ImageProcessor.class, "ip.");
        addClassToAutoCompletion(IJ.class, "IJ.");
        addClassToAutoCompletion(Roi.class, "roi.");

        addClassToAutoCompletion(ArrayImgs.class, "ArrayImgs.");
        addClassToAutoCompletion(Cursor.class, "cursor.");
        addClassToAutoCompletion(RandomAccess.class, "randomAccess.");
        addClassToAutoCompletion(RandomAccessibleInterval.class, "rai.");
        addClassToAutoCompletion(Img.class, "img.");
        addClassToAutoCompletion(Views.class, "Views.");
        addClassToAutoCompletion(Regions.class, "Regions.");
        addClassToAutoCompletion(ImageJFunctions.class, "ImageJFunctions.");
    }

    private void addClassToAutoCompletion(Class c, String prefix) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            //if (Modifier.isStatic(method.getModifiers()) || prefix.substring(0,0) == prefix.substring(0,0).toLowerCase()) {
                //System.out.println(method.toString());
                String description = "<b>" + method.getName() + "</b><br>";
                String headline = prefix + method.getName() + "(";
                String name = prefix + method.getName();

                for (Parameter parameter : method.getParameters()) {
                    //System.out.println(parameter);
                    if (!headline.endsWith("(")) {
                        headline = headline + ", ";
                    }
                    headline = headline + parameter.getName();
                    description = description + "<li>" + parameter.getType().getSimpleName() + " " + parameter.getName() + "</li>";
                }
                headline = headline + ");";

                description = description + "<br><br>" +
                        "Defined in <br>" +
                        c.getCanonicalName() +"<br>";

                description = description + "<br><br>" +
                        "returns " + method.getReturnType().getSimpleName();

                addCompletion(makeListEntry(this, headline, name, description));
            //}
        }
        if (c.getSuperclass() != null && c.getSuperclass() != Object.class) {
            addClassToAutoCompletion(c.getSuperclass(), prefix);
        }
    }

    protected boolean isValidChar(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_' || ch == '.' || ch == '"' || ch == '(' || ch == '2';
    }



    private BasicCompletion makeListEntry(
            final ScriptingAutoCompleteProvider provider, String headline,
            final String name, String description)
    {

        if (headline.trim().endsWith("-")) {
            headline = headline.trim();
            headline = headline.substring(0, headline.length() - 2);
        }

        return new BasicCompletion(provider, headline, null, description);
    }

    public static void main(String... args) {
        ScriptingAutoCompleteProvider.getInstance();
    }

}
