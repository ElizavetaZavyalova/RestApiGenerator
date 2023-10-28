package org.example.analize.analize.compison;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.Interpretation;
import org.example.analize.remove.Variables;
import org.example.analize.remove.connections.OperationCondition;
@Slf4j
public abstract class BaseComparisonParser<Condition > implements Interpretation<Condition>{
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
    record PORT() {
        static final int FIELD = 0;
        static final int VALUE = 1;
        static final int PORT_CONT=2;
    }
    BaseComparisonParser(String request, BaseVariables variables,String tableName) {
        log.debug("BaseComparisonParser of:" + request);
        log.debug("request:" + request);
        for (Operation operation : Operation.values()) {
            String[] input = request.split(operation.getValue());
            if (input.length == PORT.PORT_CONT) {
                this.operation = operation;
                log.debug("operation is:"+operation.value);
                this.value = addValue(input[PORT.VALUE],variables);
                log.debug("value is:"+operation.value);
                this.field = (!input[PORT.FIELD].isEmpty()) ?
                        addField(input[PORT.FIELD],variables,tableName,false) :
                        addField(input[PORT.VALUE],variables,tableName,true);
                log.debug("field is:"+operation.value);
                return;
            }
            //TODO exception input.length > PORT.PORT_CONT

        }
        this.operation = Operation.EQ;
        log.debug("operation is:"+operation.value);
        this.value =addValue(request,variables);
        log.debug("value is:"+operation.value);
        this.field =  addField(request,variables,tableName,true);
        log.debug("field is:"+operation.value);
    }
    record debug(){
        static String debugString(String information,String result){
            if(log.isDebugEnabled()){
                log.debug(information+": "+result);
            }
            return result;
        }
    }
    abstract String addValue(String value, BaseVariables baseVariables);
    abstract String addField(String value, BaseVariables baseVariables,String tableName,boolean isFromVariable);
}
