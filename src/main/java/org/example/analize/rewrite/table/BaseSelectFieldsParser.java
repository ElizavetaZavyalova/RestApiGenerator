package org.example.analize.rewrite.table;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.premetive.field.BaseField;
import org.example.analize.rewrite.premetive.table.BaseTable;

@Slf4j
public abstract class BaseSelectFieldsParser<Select,Table,Field> implements Interpretation<Select> {

    @Getter
    protected BaseTable<Table> table;
    protected BaseSelectFieldsParser(String request, Variables variables) {
        log.debug(request);
        String[] input = request.split(":");
        for (String port : input) {
            if (Type.isTable(port)) {
                table = addTable(Type.makeValue(port), variables);
            } else if (!add(port, variables)) {
                table = addTable(port, variables);
            }
        }
    }

    public abstract  BaseField<Field> getIdIn();



    protected abstract boolean add(String port, Variables variables);

    protected abstract BaseField<Field> addField(String field, Variables variables);

    protected abstract BaseTable<Table> addTable(String table, Variables variables);

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
            return Debug.debugResult(log,"isIdNext", value,
                    value.charAt(0) == 'N');
        }

        public static boolean isId(String value) {
            return Debug.debugResult(log,"isId", value,
                    value.charAt(0) == 'I');
        }

        static boolean isLimit(String value) {
            return Debug.debugResult(log,"isLimit", value,
                    value.charAt(0) == 'L');
        }

        static boolean isTable(String value) {
            return Debug.debugResult(log,"isTable", value,
                    value.charAt(0) == 'T');
        }

        static boolean isGroupBy(String value) {
            return Debug.debugResult(log,"isGroupBy", value,
                    value.charAt(0) == 'G');
        }

        public static boolean isMax(String value) {
            return Debug.debugResult(log,"isMax", value,
                    value.equals("M"));
        }

        public static boolean isMin(String value) {
            return Debug.debugResult(log,"isMin", value,
                    value.equals("m"));
        }

        static String makeValue(String value) {
            return Debug.debugResult(log,"makeValue", value,
                    value.substring(1));
        }
    }
}
