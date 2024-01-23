package org.example.analize.premetive.fields;

import org.example.read_json.rest_controller_json.Endpoint;

public class StringField extends BaseField<String>{
    public StringField(String name, String tableName, Endpoint parent) {
        super(name, tableName, parent);
    }

    @Override
    public String interpret() {
        StringBuilder builder=new StringBuilder("DSL.field(" + toString(tableName + "." + realFieldName) + ")");
        if(!realFieldName.equals(name)){
            builder.append(".as(").append(toString(name)).append(")");
        }
        return builder.toString();
    }
    String toString(String string) {
        return "\"" + string + "\"";
    }

    @Override
    public String requestInterpret() {
        return null;
    }

    @Override
    public String getParams() {
        return   "DSL.field(" + toString(tableName + "." + realFieldName) + ")";
    }
}
