package org.example.analize.rewrite;

import org.slf4j.Logger;
public record Debug() {
    public static String debugString(Logger log, String funcName, String result) {
        if (log.isDebugEnabled()) {
            log.debug("funcName: " +funcName+ result);
        }
        return result;
    }
    public static boolean debugBoolean(Logger log,String funcName, boolean result) {
        if (log.isDebugEnabled()) {
            log.debug("funcName: " +funcName+ result);
        }
        return result;
    }
    public static long debugLong(Logger log,String funcName, long result) {
        if (log.isDebugEnabled()) {
            log.debug("funcName: " +funcName+ result);
        }
        return result;
    }
}
