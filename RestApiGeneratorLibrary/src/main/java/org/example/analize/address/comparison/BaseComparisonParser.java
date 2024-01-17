package org.example.analize.address.comparison;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Interpretation;
import org.example.analize.Variables;
import org.example.analize.address.premetive.table.BaseTable;
import org.example.analize.ident.Ident;

@Slf4j
public abstract class BaseComparisonParser<Condition,Table,Field,Value> extends Interpretation<Condition> {
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
        public String getNotShieldedRegexpValue(){
            return Ident.REGEXP.REGEX_NOT_AFTER_SLASH+"("+value+")";
        }
    }
    Interpretation<Field> field = null;
    Interpretation<Value> value = null;
  Operation operation = null;
    record PORT() {
        static final int FIELD = 0;
        static final int VALUE = 1;
        static final int PORT_CONT=2;
    }
    @Override
    public String requestInterpret(){
         return field.requestInterpret()+operation.getValue()+value.requestInterpret();
    }
    BaseComparisonParser(String request, Variables variables,  BaseTable<Table> table) {
        Debug.debug(log,"BaseComparisonParser of:" ,request," tableName: ",table.getTable());
        Debug.debug(log,"request:" ,request);
        for (Operation operation :Operation.values()) {
            String[] input = request.split(operation.getNotShieldedRegexpValue());
            if (input.length == PORT.PORT_CONT) {
                this.operation = operation;
                Debug.debug(log,"operation is:",operation.value);
                this.value = addValue(input[PORT.VALUE],variables);
                Debug.debug(log,"value");
                this.field = (!input[PORT.FIELD].isEmpty()) ?
                        addField(input[PORT.FIELD],variables,table,false) :
                        addField(input[PORT.VALUE],variables,table,true);
                Debug.debug(log,"field",input[PORT.FIELD]);
                return;
            }
            //TODO exception input.length > PORT.PORT_CONT
        }
        this.operation = Operation.EQ;
        Debug.debug(log,"operation is:",operation.value);
        this.value =addValue(request,variables);
        Debug.debug(log,"value");
        this.field =  addField(request,variables,table,true);
        Debug.debug(log,"fieldVar");
    }
    abstract Interpretation<Value> addValue(String value, Variables variables);
    abstract Interpretation<Field> addField(String value, Variables variables,
                                            BaseTable<Table> table, boolean isFromVariable);
}