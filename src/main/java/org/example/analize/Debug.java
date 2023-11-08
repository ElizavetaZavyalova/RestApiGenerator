package org.example.analize;

import java.util.Arrays;

import org.slf4j.Logger;

public record Debug() {
    public static String debugResult(Logger log, String funcName, String result) {
        if (log.isDebugEnabled()) {
            log.debug(funcName + " result:" + result);
        }
        return result;
    }

    public static void debug(Logger log, String... strings) {
        if (log.isDebugEnabled()) {
            StringBuilder builder = new StringBuilder();
            for (String string : strings) {
                builder.append(string);
            }
            log.debug(builder.toString());
        }

    }

    public static void debug(Logger log, String string, String[] array) {
        if (log.isDebugEnabled()) {
            log.debug(string, Arrays.toString(array));
        }

    }

    public static String debugResult(Logger log, String funcName, String variable, String result) {
        if (log.isDebugEnabled()) {
            log.debug(funcName + " variable:" + variable + result);
        }
        return result;
    }

    public static boolean debugResult(Logger log, String funcName, String variable, boolean result) {
        if (log.isDebugEnabled()) {
            log.debug(funcName + " variable:" + variable + result);
        }
        return result;
    }

    public static long debugResult(Logger log, String funcName, long result) {
        if (log.isDebugEnabled()) {
            log.debug(funcName + result);
        }
        return result;
    }
}
