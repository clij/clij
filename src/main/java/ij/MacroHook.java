package ij;

public class MacroHook {
    public static boolean wasAborted() {
        return Macro.abort;
    }
}
