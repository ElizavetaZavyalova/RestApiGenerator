package org.example.analize.connections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.Variables;
import org.jooq.Condition;
import org.jooq.DSLContext;

import org.jooq.impl.DSL;

@Slf4j
public class OperationCondition implements ConditionInterpret {
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
        return DSL.field(field).eq(value);
        //TODO no opiration
        //throw new IllegalArgumentException("not correct Operation"+ operation);
    }

    OperationCondition(String request, Variables variables) {
        for (Operation operation : Operation.values()) {
            String[] input = request.split(operation.getValue());
            if (input.length == 3||input.length==2) {
                this.operation = operation;
                this.value = variables.addVariableValue(input[PORT.VALUE],isOnlyString());
                this.field =(input.length==3)?
                        (variables.addVariableField(input[PORT.FIELD])):
                        variables.makeString(variables.makeVariableFromString(value));
                return;
            }
        }
        this.operation = Operation.EQ;
        this.value = variables.addVariableValue(request,isOnlyString());
        this.field = variables.makeString(variables.makeVariableFromString(value));
    }
    boolean isOnlyString(){
        return operation==Operation.LIKE||operation==Operation.NOT_LIKE;
    }

}
