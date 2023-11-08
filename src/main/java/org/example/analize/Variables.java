package org.example.analize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.ident.Ident;

@Slf4j
public class Variables {
    public void add(String variable, make.TYPE type) {
        Debug.debug(log, "add variable:", variable, " type:", type.value);
        //TODO parse Var:valid... {name, type,valid}
    }

    @Slf4j
    public record make() {
        @AllArgsConstructor
        public enum TYPE {
            STRING("String"),
            LONG("long");
            @Getter
            String value = null;

        }

        public static boolean isVariable(String variable) {
            return Debug.debugResult(log, "isVariable", variable,
                    (variable.charAt(0) == '{') && (variable.charAt(variable.length() - 1) == '}'));
        }

        public static boolean isConstant(String variable) {
            return Debug.debugResult(log, "isConstant", variable, !isVariable(variable));
        }

        public static boolean isLong(String variable) {
            if (isVariable(variable)) {
                return Debug.debugResult(log, "isLong", variable,
                        variable.charAt(1) == '@');
            }
            return Debug.debugResult(log, "isLong", variable,
                    variable.charAt(0) == '@');
        }

        public static String makeVariable(String variable) {
            if (isLong(variable)) {
                return Debug.debugResult(log, "makeVariable",
                        variable.substring(2, variable.length() - 1));
            }
            return Debug.debugResult(log, "makeVariable",
                    variable.substring(1, variable.length() - 1));
        }

        public static String makeField(String variable, String tableName) {
            return Debug.debugResult(log, "makeField",
                    makeVariable(tableName) + "." + makeVariable(variable));

        }

        public static String makeVariableFromString(String variable) {
            if ((variable.charAt(0) == '\"') && (variable.charAt(variable.length() - 1) == '\"')) {
                return Debug.debugResult(log, "makeVariableFromString",
                        variable.substring(1, variable.length() - 1));
            }
            return Debug.debugResult(log, "makeVariable", variable);
        }

        public static boolean isString(String variable) {
            return Debug.debugResult(log, "isString", variable,
                    !isLong(variable));
        }


        static public String makeWriteVariable(String variable) {
            Debug.debug(log, "makeWriteVariable from: ", variable);
            if (isVariable(variable)) {
                if (isLong(variable)) {
                    return Debug.debugResult(log, "makeWriteVariable result:",
                            variable.substring(2, variable.length() - 1).split(":", 2)[0]);
                }
                return Debug.debugResult(log, "makeWriteVariable result:",
                        variable.substring(1, variable.length() - 1).split(":", 2)[0]);
            }
            if (isLong(variable)) {
                return Debug.debugResult(log, "makeWriteVariable result:",
                        variable.substring(1));
            }
            return Debug.debugResult(log, "makeWriteVariable result:",
                    makeOnlyString(variable));
        }

        public static String makeOnlyString(String variable) {
            Debug.debug(log, "makeOnlyString from ", variable);
            if (isVariable(variable)) {
                variable = variable.substring(1, variable.length() - 1);
                if (isLong(variable)) {
                    variable = variable.substring(1);
                }
            }
            return Debug.debugResult(log, "makeOnlyString result:",
                    "\"" + variable + "\"");
        }

        public static String deleteNotUseSlash(String value) {
            return String.join("", value.split(Ident.REGEXP.REGEXP_TO_SPLIT));
        }

        static public TYPE makeType(String variable) {
            TYPE type = isLong(variable) ? Variables.make.TYPE.LONG : Variables.make.TYPE.STRING;
            Debug.debugResult(log, "makeType" + variable, type.getValue());
            return type;
        }
    }
}
