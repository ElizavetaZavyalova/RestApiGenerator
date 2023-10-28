package org.example.analize.rewrite.comparison;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.Interpretation;

@Slf4j
public abstract class BaseComparisonParser<Condition,Field,Value> implements Interpretation<Condition> {
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
    Interpretation<Field> field = null;
    Interpretation<Value> value = null;
  Operation operation = null;
    record PORT() {
        static final int FIELD = 0;
        static final int VALUE = 1;
        static final int PORT_CONT=2;
    }
    BaseComparisonParser(String request, BaseVariables variables, String tableName) {
        log.debug("BaseComparisonParser of:" + request+" tableName: "+tableName);
        log.debug("request:" + request);
        for (Operation operation :Operation.values()) {
            String[] input = request.split(operation.getValue());
            if (input.length == BaseComparisonParser.PORT.PORT_CONT) {
                this.operation = operation;
                log.debug("operation is:"+operation.value);
                this.value = addValue(input[PORT.VALUE],variables);
                log.debug("value is:"+value);
                this.field = (!input[BaseComparisonParser.PORT.FIELD].isEmpty()) ?
                        addField(input[BaseComparisonParser.PORT.FIELD],variables,tableName,false) :
                        addField(input[BaseComparisonParser.PORT.VALUE],variables,tableName,true);
                log.debug("field is:"+field);
                return;
            }
            //TODO exception input.length > PORT.PORT_CONT

        }
        this.operation = Operation.EQ;
        log.debug("operation is:"+operation.value);
        this.value =addValue(request,variables);
        log.debug("value is:"+value);
        this.field =  addField(request,variables,tableName,true);
        log.debug("field is:"+field);
    }
    abstract Interpretation<Value> addValue(String value, BaseVariables baseVariables);
    abstract Interpretation<Field> addField(String value, BaseVariables baseVariables,
                                            String tableName,boolean isFromVariable);
}