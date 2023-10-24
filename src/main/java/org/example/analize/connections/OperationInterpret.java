package org.example.analize.connections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.Variables;
import org.jooq.Condition;
import org.jooq.DSLContext;

import org.jooq.impl.DSL;

import java.util.Map;

@Slf4j
public class OperationInterpret implements ConditionInterpret {
    private static class PORT {
        static int FIELD = 0;
        static int VALUE = 2;
    }

    @AllArgsConstructor
    public enum Operation {
        NOT_EQUAL("!="),
        NOT_LIKE("!~"),
        GREATER_OR_EQUAL(">="),
        LESS_OR_EQUAL("<="),
        LESS_THEN("<"),
        GREATER_THEN(">"),
        LIKE("~"),
        EQ("=");

        @Getter
        private String value = null;
    }

    String field = null;
    String value = null;
    Operation operation = null;


    @Override
    public Condition makeCondition(DSLContext dsl) {
        switch (operation) {
            case EQ -> {
                return DSL.field(field).eq(value);
            }
            case LIKE -> {
                return DSL.field(field).like(value);
            }
            case NOT_LIKE -> {
                return DSL.field(field).notLike((value));
            }
            case LESS_THEN -> {
                return DSL.field(field).lessThan(value);
            }
            case NOT_EQUAL -> {
                return DSL.field(field).notEqual(value);
            }
            case LESS_OR_EQUAL -> {
                return DSL.field(field).lessOrEqual(value);
            }
            case GREATER_THEN -> {
                return DSL.field(field).greaterThan(value);
            }
            case GREATER_OR_EQUAL -> {
                return DSL.field(field).greaterOrEqual(value);
            }
        }
        //TODO no opiration
        throw new IllegalArgumentException("not correct Operation"+ operation);
    }

    OperationInterpret(String request, Variables variables) {
        for (Operation operation : Operation.values()) {
            String[] input = request.split(operation.getValue());
            if (input.length == 3) {
                this.field = variables.addVariableAndMakeItCorrectView(input[PORT.FIELD]);
                this.operation = operation;
                this.value = variables.addVariableAndMakeItCorrectView(input[PORT.VALUE]);
                return;
            }

        }
        log.debug("NOT_CORRECT_REQUEST:" + request);
        //TODO No operation
    }
}
