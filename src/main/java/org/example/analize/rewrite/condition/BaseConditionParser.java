package org.example.analize.rewrite.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.rewrite.Interpretation;

@Slf4j
public abstract class BaseConditionParser<Condition> implements Interpretation<Condition> {
    record PORT() {
        static final int OPERATION = 0;
        static final int NEXT = 1;
        static final int PORT_CONT = 2;
    }

    @AllArgsConstructor
    public enum ConditionOperand {
        AND("&"),
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

    BaseConditionParser(String request, BaseVariables variables, String tableName) {
        //[(&|]
        log.debug("request:" + request);
        String[] input = request.split("[(&|]", PORT.PORT_CONT);
        if (input.length == PORT.PORT_CONT) {
            operand = ConditionOperand.fromString(String.valueOf(request.charAt(input[PORT.OPERATION].length())));
            if (parseBrackets(input, variables, tableName)) {
                return;
            }
            log.debug("operand is: " + operand.toString() + "(" + operand.value + ")");
            comparison = makeComparison(input[PORT.OPERATION], variables, tableName);
            log.debug("port of comparison is " + input[BaseConditionParser.PORT.OPERATION]);
            conditionNext = makeConditionNext(input[BaseConditionParser.PORT.NEXT], variables, tableName);
            log.debug("port of condition next is " + input[BaseConditionParser.PORT.NEXT]);
            return;
        }
        comparison = makeComparison(request, variables, tableName);
        log.debug("port of comparison is " + request);
    }

    boolean parseBrackets(String[] input, BaseVariables variables, String tableName) {
        log.debug("parseBrackets");
        if (operand == ConditionOperand.LEFT_BRACKET) {
            log.debug("parseBrackets operand:" + operand.value);
            if (input[PORT.OPERATION].isEmpty()) {
                log.debug("parseBrackets port operation empty:");
                input = input[PORT.NEXT].split("[)]", PORT.PORT_CONT);
                comparison = makeConditionNext(input[PORT.OPERATION], variables, tableName);
                log.debug("parseBrackets  comparison:" + input[PORT.OPERATION]);
                if (!input[PORT.NEXT].isEmpty()) {
                    log.debug("parseBrackets port next not empty:");
                    operand = ConditionOperand.fromString(String.valueOf(input[PORT.NEXT]
                            .charAt(input[PORT.OPERATION].length())));
                    log.debug("parseBrackets operand:" + operand.value);
                    if ((operand == ConditionOperand.NO) || operand == ConditionOperand.LEFT_BRACKET) {
                        if (operand == ConditionOperand.LEFT_BRACKET) {
                            log.debug("parseBrackets operand:" + operand.value);
                            conditionNext = makeConditionNext("(" + input[PORT.NEXT], variables, tableName);
                            log.debug("parseBrackets port next:" + "(" + input[PORT.NEXT]);
                            operand = ConditionOperand.AND;
                            log.debug("parseBrackets operand:" + operand.value);
                            return true;
                        }
                        operand = ConditionOperand.AND;
                        log.debug("parseBrackets operand:" + operand.value);
                        conditionNext = makeConditionNext(input[PORT.NEXT], variables, tableName);
                        log.debug("parseBrackets port next:" + input[PORT.NEXT]);
                        return true;
                    }
                    conditionNext = makeConditionNext(input[PORT.NEXT], variables, tableName);
                }
                return true;
            }
            operand = ConditionOperand.AND;
            log.debug("operand is: " + operand.toString() + "(" + operand.value + ")");
            comparison = makeComparison(input[PORT.OPERATION], variables, tableName);
            log.debug("port of comparison is " + input[BaseConditionParser.PORT.OPERATION]);
            conditionNext = makeConditionNext("(" + input[BaseConditionParser.PORT.NEXT], variables, tableName);
            log.debug("port of condition next is " + input[BaseConditionParser.PORT.NEXT]);
            return true;
        }
        return false;
    }

    abstract Interpretation<Condition> makeComparison(String request, BaseVariables variables, String tableName);

    abstract Interpretation<Condition> makeConditionNext(String request, BaseVariables variables, String tableName);

}
