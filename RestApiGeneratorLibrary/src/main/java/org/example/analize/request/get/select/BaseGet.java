package org.example.analize.request.get.select;


import org.example.analize.premetive.fields.BaseField;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseGet<R,M> extends PortRequestWithCondition<R> {
    protected final List<BaseField<R>> fields;
    protected BaseGet(String request, PortRequestWithCondition<R> select, List<String> fields, Endpoint parent) {
        super(request, select, parent);
        this.fields=fields.stream().map(fieldName->makeField(fieldName,tableName,parent)).toList();
    }
    public String getExampleFields(){
        return fields.stream().map(BaseField::getName).map(f->"\""+f+"\"").collect(Collectors.joining(", "));
    }
    public abstract M createFieldsPort(M method,boolean isFieldsExist);
    protected boolean isFieldsExist(){
        return !fields.isEmpty();
    }
    protected  abstract BaseField<R> makeField(String name,String table,Endpoint parent);

}
