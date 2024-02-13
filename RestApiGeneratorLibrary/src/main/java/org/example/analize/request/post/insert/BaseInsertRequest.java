package org.example.analize.request.post.insert;

import org.example.analize.premetive.fields.BaseField;
import org.example.analize.select.port_request.PortRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public abstract class BaseInsertRequest<R> extends PortRequest<R> {
    protected List<BaseField<R>> fields;

    protected BaseInsertRequest(String request, List<String> fields, PortRequestWithCondition<R> select, Endpoint parent) {
        super.initTableName(request, select, parent);
        this.fields = fields.stream().map(fieldName -> makeField(fieldName, tableName, parent)).toList();
    }

    protected abstract BaseField<R> makeField(String name, String table, Endpoint parent);
}
