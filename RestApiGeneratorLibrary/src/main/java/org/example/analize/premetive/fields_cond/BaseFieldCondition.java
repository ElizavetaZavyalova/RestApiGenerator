package org.example.analize.premetive.fields_cond;


import org.example.analize.premetive.BaseFieldParser;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;


public abstract class BaseFieldCondition<R> extends BaseFieldParser<R> {
    String tableName;

    BaseFieldCondition(String variable, String tableName, Endpoint parent) throws IllegalArgumentException {
        super(variable, parent);
        this.tableName = tableName;
    }
}
