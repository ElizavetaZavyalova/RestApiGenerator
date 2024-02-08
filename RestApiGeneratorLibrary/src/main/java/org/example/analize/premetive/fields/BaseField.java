package org.example.analize.premetive.fields;

import lombok.Getter;
import org.example.analize.interpretation.Interpretation;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

public abstract class BaseField<R> implements Interpretation<R> {
    @Getter
    protected String name;
    protected String realFieldName;
    protected String tableName;
    protected BaseField(String name, String tableName, Endpoint parent){
        this.tableName=tableName;
        this.name=name;
        this.realFieldName=parent.getRealFieldName(name);
    }
}
