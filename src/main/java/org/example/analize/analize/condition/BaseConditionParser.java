package org.example.analize.analize.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.Interpretation;
import org.example.analize.remove.connections.OperationCondition;
@Slf4j
public abstract class BaseConditionParser<Condition> implements Interpretation<Condition>{
    record PORT() {
        static final int OPERATION = 0;
        static final int NEXT = 1;
        static final int PORT_CONT=2;
    }

    @AllArgsConstructor
    public enum ConditionOperand{
        ADD("&"),
        OR("|"),
        NO("");
        @Getter
        private String value = null;
        static ConditionOperand fromString(String value){
            for (ConditionOperand conditionOperand : ConditionOperand.values()) {
                if(conditionOperand.value.equals(value)){
                    return conditionOperand;
                }
            }
            throw new IllegalArgumentException("value of ConditionOperand is "+value);
        }
    }
    Interpretation<Condition> comparison=null;
    Interpretation<Condition> conditionNext=null;
    ConditionOperand operand=ConditionOperand.NO;

    BaseConditionParser(String request, BaseVariables variables){
        //[&|]
        log.debug("request:"+request);
        String[] input=request.split("[&|]",PORT.PORT_CONT);
        if(input.length==PORT.PORT_CONT){
            operand=ConditionOperand.fromString(String.valueOf(request.charAt(input[PORT.OPERATION].length())));
            log.debug("operand is: "+operand.toString()+"("+operand.value+")");
            comparison=makeComparison(input[PORT.OPERATION],variables);
            log.debug("port of comparison is "+input[PORT.OPERATION]);
            conditionNext= makeConditionNext(input[PORT.NEXT],variables);
            log.debug("port of condition next is "+input[PORT.NEXT]);
            return;
        }
        comparison=makeComparison(request,variables);
        log.debug("port of comparison is "+request);
    }
    abstract Interpretation<Condition> makeComparison(String request,BaseVariables variables);
    abstract Interpretation<Condition> makeConditionNext(String request,BaseVariables variables);
    record debug(){
        static String debugString(String information,String result){
            if(log.isDebugEnabled()){
                log.debug(information+": "+result);
            }
            return result;
        }
    }

}
