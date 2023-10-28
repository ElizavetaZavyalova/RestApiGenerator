package org.example.analize.analize;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.remove.Variables;

@Slf4j
public class BaseVariables {
    public void addVariable(String variable, make.TYPE type) {

    }

    @Slf4j
    public record make() {
        @AllArgsConstructor
        public enum TYPE {
            STRING("String"),
            LONG("long");
            String value = null;

        }

        public static boolean isVariable(String variable) {
            return debug.debugResult("isVariable", variable,
                    (variable.charAt(0) == '{') && (variable.charAt(variable.length() - 1) == '}'));
        }

        public static boolean isConstant(String variable) {
            return debug.debugResult("isConstant", variable, !isVariable(variable));
        }

        public static boolean isLong(String variable) {
            if (isVariable(variable)) {
                return debug.debugResult("isLong", variable,
                        variable.charAt(1) == '@');
            }
            return debug.debugResult("isLong", variable,
                    variable.charAt(0) == '@');
        }

        public static String makeVariable(String variable) {
            if (isLong(variable)) {
                return debug.debugResult("makeVariable",
                        variable.substring(2, variable.length() - 1));
            }
            return debug.debugResult("makeVariable",
                    variable.substring(1, variable.length() - 1));
        }
        public static String makeField(String variable,String tableName) {
            return debug.debugResult("makeField",
                    makeVariable(tableName)+"."+ makeVariable(variable));

        }

        public static String makeVariableFromString(String variable) {
            if ((variable.charAt(0) == '\"') && (variable.charAt(variable.length() - 1) == '\"')) {
                return debug.debugResult("makeVariableFromString",
                        variable.substring(1, variable.length() - 1));
            }
            return debug.debugResult("makeVariable", variable);
        }

        public static boolean isString(String variable) {
            return debug.debugResult("isString", variable,
                    !isLong(variable));
        }

        record debug() {
            public static String debugResult(String information, String result) {
                if (log.isDebugEnabled()) {
                    log.debug(information + ": " + result);
                }
                return result;
            }

            public static boolean debugResult(String information, String variable, boolean result) {
                if (log.isDebugEnabled()) {
                    log.debug(information + ": " + variable + ": " + result);
                }
                return result;
            }

            public static TYPE debugType(String information, String variable, TYPE result) {
                if (log.isDebugEnabled()) {
                    log.debug(information + ": " + variable + ": " + result);
                }
                return result;
            }
        }

        static public String makeWriteVariable(String variable) {
            log.debug("makeWriteVariable from: " + variable);
            if (isVariable(variable)) {
                if (isLong(variable)) {
                    return debug.debugResult("makeWriteVariable result:",
                            variable.substring(2, variable.length() - 1));
                }
                return debug.debugResult("makeWriteVariable result:",
                        variable.substring(1, variable.length() - 1));
            }
            if (isLong(variable)) {
                return debug.debugResult("makeWriteVariable result:",
                        variable.substring(2, variable.length() - 1));
            }
            return debug.debugResult("makeWriteVariable result:",
                    makeOnlyString(variable));
        }

        static public String makeOnlyString(String variable) {
            log.debug("makeOnlyString from " + variable);
            if (isVariable(variable)) {
                variable = variable.substring(1, variable.length() - 1);
                if (isLong(variable)) {
                    variable = variable.substring(1);
                }
            }
            return debug.debugResult("makeWOnlyString result:",
                    "\"" + variable + "\"");
        }

        static public TYPE makeType(String variable) {
            return debug.debugType("makeType ", variable,
                    isLong(variable) ? TYPE.LONG : TYPE.STRING);
        }
    }
}
