package org.example.analize.rewrite;

import org.slf4j.Logger;
public record Debug ()  {
    public static String debugResult(Logger log, String funcName, String result) {
        if (log.isDebugEnabled()) {
            log.debug("funcName: " +funcName+ result);
        }
        return result;
    }
    public static String debugResult(Logger log, String funcName,String variable, String result) {
        if (log.isDebugEnabled()) {
            log.debug("funcName: " +funcName+" variable:"+variable + result);
        }
        return result;
    }
    public static boolean debugResult(Logger log,String funcName,String variable, boolean result) {
        if (log.isDebugEnabled()) {
            log.debug("funcName: " +funcName+" variable:"+variable + result);
        }
        return result;
    }
    public static long debugResult(Logger log,String funcName, long result) {
        if (log.isDebugEnabled()) {
            log.debug("funcName: " +funcName+ result);
        }
        return result;
    }
}
