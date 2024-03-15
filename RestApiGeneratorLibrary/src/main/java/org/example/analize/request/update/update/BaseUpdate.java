package org.example.analize.request.update.update;

import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.BaseFieldInsertUpdate;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseUpdate<R,N> extends PortRequestWithCondition<R> {

    protected List<BaseFieldInsertUpdate<R,N>> fields;
    protected List<BaseField<R>> returnFields;
    protected BaseUpdate(String request, List<String> fields,List<String> returnFields, PortRequestWithCondition<R> select, Endpoint parent) throws IllegalArgumentException {
        super(request, select, parent);
        this.fields=fields.stream().map(fieldName->makeField(fieldName,tableName,parent)).toList();
        this.returnFields=returnFields.stream().map(fieldName->makeReturnField(fieldName,tableName,parent)).toList();
    }
    public boolean isReturnSomething(){
        return !returnFields.isEmpty();
    }

    public String getExampleFields(){
        return fields.stream().map(BaseFieldInsertUpdate::getExample).collect(Collectors.joining(", "));
    }
    protected  abstract BaseField<R> makeReturnField(String name, String table, Endpoint parent);
    protected  abstract BaseFieldInsertUpdate<R,N> makeField(String name, String table, Endpoint parent);
}
