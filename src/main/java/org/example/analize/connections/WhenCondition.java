package org.example.analize.connections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.analize.Variables;
import org.jooq.Condition;
import org.jooq.DSLContext;

public class WhenCondition implements ConditionInterpret {

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
    public WhenCondition(String request, Variables variables) {
        condition=ConditionOperand.ADD;
        String[] input=request.split(ConditionOperand.ADD.value,2);
        if(input.length==2){
            operation=new OperationCondition(input[PORT.OPERATION],variables);
            conditionNext=new WhenCondition(input[PORT.WHEN_NEXT],variables);
        }
        operation=new OperationCondition(request,variables);
    }
}
