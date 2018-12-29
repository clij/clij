package net.haesleinhuepf.imagej.utilities;

import ij.IJ;
import org.junit.runners.Parameterized;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.script.ScriptService;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

/**
 * BenchmarkExecutorPlugin
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = Command.class, menuPath = "Plugins>BenchmarkExecutor")
public class BenchmarkExecutorPlugin implements Command {

    @Parameter
    private ScriptService scriptService;

    //@Parameter
    String scriptLocation = "C:/structure/code/haesleinhuepf_clearclij/src/main/macro/benchmarking.ijm";

    @Override
    public void run() {
        try {
            scriptService.run(new File(scriptLocation), true, new Object[]{}).get();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("########################");
        System.out.println(IJ.getLog());
        System.out.println("########################");
        System.exit(0);
    }
}
