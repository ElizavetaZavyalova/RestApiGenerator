package org.example.analize.connections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.analize.Variables;
import org.jooq.Condition;
import org.jooq.DSLContext;

public class WhenExpressionInterpret implements ConditionInterpret {
    //TODO condition OR ()
    private static class PORT {
        static int OPERATION = 0;
        static int WHEN_NEXT = 1;
    }
    @AllArgsConstructor
    public enum ConditionOperand{
        ADD("&");
        @Getter
        private String value = null;

    }
    ConditionInterpret operation;
    ConditionInterpret conditionNext=null;
    ConditionOperand condition;

    @Override
    public Condition makeCondition(DSLContext dsl) {
        if(conditionNext==null) {
            return operation.makeCondition(dsl);
        }
        return operation.makeCondition(dsl).and(conditionNext.makeCondition(dsl));
    }
    WhenExpressionInterpret(String request, Variables variables) {
        condition=ConditionOperand.ADD;
        String input[]=request.split(ConditionOperand.ADD.value,2);
        if(input.length==2){
            operation=new OperationInterpret(input[PORT.OPERATION],variables);
            conditionNext=new WhenExpressionInterpret(input[PORT.WHEN_NEXT],variables);
        }
        operation=new OperationInterpret(request,variables);
    }
}
