package org.example.analize.address.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Interpretation;
import org.example.analize.Variables;
import org.example.analize.address.premetive.table.BaseTable;
import org.example.analize.ident.Ident;

@Slf4j
public abstract class BaseConditionParser<Condition, Table> extends Interpretation<Condition> {
    record PORT() {
        static final int OPERATION = 0;
        static final int NEXT = 1;
        static final int PORT_CONT = 2;
    }

    @AllArgsConstructor
    public enum ConditionOperand {
        AND("&"),//TODO regex valid field is may Be | or &
        OR("|"),
        LEFT_BRACKET("("),
        NO("");
        @Getter
        private String value = null;

        static ConditionOperand fromString(String value) {
            for (ConditionOperand conditionOperand : ConditionOperand.values()) {
                if (conditionOperand.value.equals(value)) {
                    return conditionOperand;
                }
            }
            return NO;
        }
    }

    Interpretation<Condition> comparison = null;
    Interpretation<Condition> conditionNext = null;
    ConditionOperand operand = ConditionOperand.NO;
    @Override
    public String requestInterpret(){
        if(conditionNext!=null) {
            return comparison.requestInterpret() + operand.getValue() + conditionNext.requestInterpret();
        }
        return comparison.requestInterpret();
    }

    BaseConditionParser(String request, Variables variables, BaseTable<Table> table) {
        //[(&|]
        Debug.debug(log, "request:", request);
        String[] input = request.split(Ident.REGEXP.REGEX_CONDITION_NOT_AFTER_SLASH, PORT.PORT_CONT);
        if (input.length == PORT.PORT_CONT) {
            operand = ConditionOperand.fromString(String.valueOf(request.charAt(input[PORT.OPERATION].length())));
            /* if (parseBrackets(input, variables, table)) {
                return;
            }*/
            Debug.debug(log, "operand is: ", operand.toString(), "(" + operand.value, ")");
            comparison = makeComparison(input[PORT.OPERATION], variables, table);
            Debug.debug(log, "port of comparison is ", input[PORT.OPERATION]);
            conditionNext = makeConditionNext(input[PORT.NEXT], variables, table);
            Debug.debug(log, "port of condition next is ", input[PORT.NEXT]);
            return;
        }
        comparison = makeComparison(request, variables, table);
        Debug.debug(log, "port of comparison is ", request);
    }

    @Deprecated
    boolean parseBrackets(String[] input, Variables variables, BaseTable<Table> table) {
        Debug.debug(log, "parseBrackets");
        if (operand == ConditionOperand.LEFT_BRACKET) {
            Debug.debug(log, "parseBrackets operand:", operand.value);
            if (input[PORT.OPERATION].isEmpty()) {
                Debug.debug(log, "parseBrackets port operation empty:");
                input = input[PORT.NEXT].split("[)]", PORT.PORT_CONT);
                comparison = makeConditionNext(input[PORT.OPERATION], variables, table);
                Debug.debug(log, "parseBrackets  comparison:", input[PORT.OPERATION]);
                if (!input[PORT.NEXT].isEmpty()) {
                    Debug.debug(log, "parseBrackets port next not empty:");
                    operand = ConditionOperand.fromString(String.valueOf(input[PORT.NEXT]
                            .charAt(input[PORT.OPERATION].length())));
                    Debug.debug(log, "parseBrackets operand:", operand.value);
                    if ((operand == ConditionOperand.NO) || operand == ConditionOperand.LEFT_BRACKET) {
                        if (operand == ConditionOperand.LEFT_BRACKET) {
                            Debug.debug(log, "parseBrackets operand:", operand.value);
                            conditionNext = makeConditionNext("(" + input[PORT.NEXT], variables, table);
                            Debug.debug(log, "parseBrackets port next:", "(", input[PORT.NEXT]);
                            operand = ConditionOperand.AND;
                            Debug.debug(log, "parseBrackets operand:", operand.value);
                            return true;
                        }
                        operand = ConditionOperand.AND;
                        Debug.debug(log, "parseBrackets operand:", operand.value);
                        conditionNext = makeConditionNext(input[PORT.NEXT], variables, table);
                        Debug.debug(log, "parseBrackets port next:", input[PORT.NEXT]);
                        return true;
                    }
                    conditionNext = makeConditionNext(input[PORT.NEXT], variables, table);
                }
                return true;
            }
            operand = ConditionOperand.AND;
            Debug.debug(log, "operand is: ", operand.toString(), "(", operand.value, ")");
            comparison = makeComparison(input[PORT.OPERATION], variables, table);
            Debug.debug(log, "port of comparison is ", input[PORT.OPERATION]);
            conditionNext = makeConditionNext("(" + input[PORT.NEXT], variables, table);
            Debug.debug(log, "port of condition next is ", input[PORT.NEXT]);
            return true;
        }
        return false;
    }

    abstract Interpretation<Condition> makeComparison(String request, Variables variables, BaseTable<Table> table);

    abstract Interpretation<Condition> makeConditionNext(String request, Variables variables, BaseTable<Table> table);

}
