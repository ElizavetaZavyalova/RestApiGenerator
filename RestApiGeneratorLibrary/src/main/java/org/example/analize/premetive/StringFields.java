package org.example.analize.premetive;

import org.example.read_json.rest_controller_json.Endpoint;
import org.jooq.Condition;
import org.jooq.impl.DSL;

public class StringFields extends BaseField<String>{
    StringFields(String variable, String tableName, Endpoint parent) throws IllegalArgumentException {
        super(variable, tableName, parent);
    }

    @Override
    public String interpret() {
        //DSL.field(DSL.table(toString(tableName)).field(toString(realFieldName))).equal(fieldName);
        return null;

    }
    String toString(String string){
        return "\""+string+"\"";
    }

    @Override
    public String requestInterpret() {
        return null;
    }

    @Override
    public String getParams() {
        return "{"+this.fieldName+"}";
    }
}
