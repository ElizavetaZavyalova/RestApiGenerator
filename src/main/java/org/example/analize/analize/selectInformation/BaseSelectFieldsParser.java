package org.example.analize.analize.selectInformation;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.Interpretation;
import org.jooq.Select;
import org.jooq.impl.DSL;

@Slf4j
public abstract class BaseSelectFieldsParser<Select> implements Interpretation<Select> {

    @Getter
    protected String table;

    protected BaseSelectFieldsParser(String request, BaseVariables variables) {
        log.debug(request);
        String[] input = request.split(":");
        DSL.select()
        for (String port : input) {

            if (Type.isTable(port)) {
                table = addTable(Type.makeValue(port), variables);
            } else if (!add(port, variables)) {
                table = addTable(port, variables);
            }
        }
    }

    public abstract String getIdIn();

    protected record debug() {
        public static String debugString(String information, String result) {
            if (log.isDebugEnabled()) {
                log.debug(information + ": " + result);
            }
            return result;
        }
    }

    protected abstract boolean add(String port, BaseVariables variables);

    protected abstract String addField(String field, BaseVariables variables);

    protected abstract String addTable(String table, BaseVariables variables);

    protected record Type() {
        record debug() {
            static boolean debugResult(String information, String value, boolean result) {
                if (log.isDebugEnabled()) {
                    log.debug(information + ": " + value + ": " + result);
                }
                return result;
            }

            static String debugResult(String information, String value, String result) {
                if (log.isDebugEnabled()) {
                    log.debug(information + ": " + value + ": " + result);
                }
                return result;
            }
        }

        static boolean isField(String value) {
            return debug.debugResult("isField", value,
                    value.charAt(0) == 'F');
        }

        public static boolean isIdNext(String value) {
            return debug.debugResult("isIdNext", value,
                    value.charAt(0) == 'N');
        }

        public static boolean isId(String value) {
            return debug.debugResult("isId", value,
                    value.charAt(0) == 'I');
        }

        static boolean isLimit(String value) {
            return debug.debugResult("isLimit", value,
                    value.charAt(0) == 'L');
        }

        static boolean isTable(String value) {
            return debug.debugResult("isTable", value,
                    value.charAt(0) == 'T');
        }

        static boolean isGroupBy(String value) {
            return debug.debugResult("isGroupBy", value,
                    value.charAt(0) == 'G');
        }

        public static boolean isMax(String value) {
            return debug.debugResult("isMax", value,
                    value.equals("M"));
        }

        public static boolean isMin(String value) {
            return debug.debugResult("isMin", value,
                    value.equals("m"));
        }

        static String makeValue(String value) {
            return debug.debugResult("makeValue", value,
                    value.substring(1));
        }
    }
}
