package org.example.analize.request.update.update;
import org.example.analize.premetive.fields.BaseFieldInsertUpdate;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public abstract class BaseUpdate<R,N> extends PortRequestWithCondition<R> {

    protected List<BaseFieldInsertUpdate<R,N>> fields;
    protected BaseUpdate(String request, List<String> fields, PortRequestWithCondition<R> select, Endpoint parent) throws IllegalArgumentException {
        super(request, select, parent);
        this.fields=fields.stream().map(fieldName->makeField(fieldName,tableName,parent)).toList();
    }
    protected  abstract BaseFieldInsertUpdate<R,N> makeField(String name, String table, Endpoint parent);
}
