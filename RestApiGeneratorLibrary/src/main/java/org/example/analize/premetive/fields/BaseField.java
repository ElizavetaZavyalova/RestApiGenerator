package org.example.analize.premetive.fields;


import org.example.analize.premetive.BaseFieldParser;
import org.example.read_json.rest_controller_json.Endpoint;


public abstract class BaseField<R> extends BaseFieldParser<R> {
    String tableName;

    BaseField(String variable, String tableName, Endpoint parent) throws IllegalArgumentException {
        super(variable, parent);
        this.tableName = tableName;
    }

    @Override
    public String requestInterpret() {
        return "{" + this.fieldName + "}";
    }
}
