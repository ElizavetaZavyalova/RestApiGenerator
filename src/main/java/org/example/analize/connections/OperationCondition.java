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
   record PORT() {
        static int FIELD = 0;
        static int VALUE = 1;
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
    }
    String makeField(){
        return "DSL.field("+field+")";
    }

    @Override
    public String makeCondition() {
        String string;
        switch (operation) {
            case LIKE -> {
                string=  makeField()+".like("+value+")";

            }
            case NOT_LIKE -> {
                string= makeField()+".notLike("+value+")";
            }
            case LESS_THEN -> {
                string= makeField()+".lessThan("+value+")";

            }
            case NOT_EQUAL -> {
                string= makeField()+".notEqual("+value+")";

            }
            case LESS_OR_EQUAL -> {
                string= makeField()+".lessOrEqual("+value+")";

            }
            case GREATER_THEN -> {
                string=makeField()+".greaterThan("+value+")";

            }
            case GREATER_OR_EQUAL -> {
                string= makeField()+".greaterOrEqual("+value+")";

            }
        }
        string= makeField()+".eq("+value+")";
        log.debug("interprit:"+string);
        return string;
    }

    OperationCondition(String request, Variables variables) {
        log.debug("request:"+request);
        for (Operation operation : Operation.values()) {
            String[] input = request.split(operation.getValue());
            if (input.length == 2) {
                this.operation = operation;
                this.value = variables.addVariableValue(input[PORT.VALUE],isOnlyString());
                this.field =(!input[PORT.FIELD].isEmpty())?
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
