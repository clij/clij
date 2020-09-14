package ij;

public class MacroHook {
    public static boolean wasAborted() {
        try {
            return Macro.abort;
        } catch (IllegalAccessError e) {
            return false;
        }
    }
}
