package org.example.analize.ident;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;

@Slf4j
public record Ident() {
    record TYPE() {
        static char FIELD = 'F';
        static char ID_NEXT = 'P';
        static char ID = 'I';
        // static char LONG='@';
        static char TABLE = 'T';
        static String MAX = "M";
        static String MIN = "m";
    }

    public record REGEXP() {
        public static final String REGEX_QUESTION_NOT_AFTER_SLASH = "(?<!\\\\)\\?";//"[?](?![^{]*})";
        public static final String REGEX_NOT_AFTER_SLASH = "(?<!\\\\)";
        public static final String REGEX_CONDITION_NOT_AFTER_SLASH = "(?<!\\\\)[|&]";
        public static final String REGEX_BACK_SLASH_NOT_AFTER_SLASH = "(?<!\\\\)[/]";
        public static final String REGEX_COLON_NOT_AFTER_SLASH = "(?<!\\\\):";
        public static final String VALUES_USE_REGEXP = "(<=)|(=>)|(<)|(>)|(=)|(!=)|(~)|(!~)" +
                "|[(]|[)]|[{]|[}]|(:)|[?]|@";
        public static final String REGEXP_TO_SPLIT = "\\\\(?=" + VALUES_USE_REGEXP + ")";
    }

    public static boolean isField(String value) {
        return Debug.debugResult(log, "isField", value,
                value.charAt(0) == TYPE.FIELD);
    }

    public static boolean isIdNext(String value) {
        return Debug.debugResult(log, "isIdNext", value,
                value.charAt(0) == TYPE.ID_NEXT);
    }

    public static boolean isId(String value) {
        return Debug.debugResult(log, "isId", value,
                value.charAt(0) == TYPE.ID);
    }

    static boolean isNoNameMaybeTable(String value) {
        return !isField(value) && !isMin(value) && !isMax(value) && !isId(value) && !isIdNext(value);
    }

    static boolean isNoName(String value) {
        return Debug.debugResult(log, "isNoName", value,
                isNoNameMaybeTable(value) && !isTable(value));
    }

    public static boolean isTable(String value) {
        return Debug.debugResult(log, "isTable", value,
                value.charAt(0) == TYPE.TABLE || isNoNameMaybeTable(value));
    }

    public static boolean isMax(String value) {
        return Debug.debugResult(log, "isMax", value,
                value.equals(TYPE.MAX));
    }

    public static boolean isMin(String value) {
        return Debug.debugResult(log, "isMin", value,
                value.equals(TYPE.MIN));
    }
}
