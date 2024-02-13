package org.example.analize.request.get.select;

import org.example.analize.premetive.fields.BaseField;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public abstract class GetBaseSelect<R> extends PortRequestWithCondition<R> {
    protected List<BaseField<R>> fields;
    protected GetBaseSelect(String request, PortRequestWithCondition<R> select, List<String> fields, Endpoint parent) {
        super(request, select, parent);
        this.fields=fields.stream().map(fieldName->makeField(fieldName,tableName,parent)).toList();
    }
    protected  abstract BaseField<R> makeField(String name,String table,Endpoint parent);

}
