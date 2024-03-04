package org.example.analize.request.update.update;
import lombok.Getter;
import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.BaseFieldReal;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseUpdate<R,N> extends PortRequestWithCondition<R> {

    protected List<BaseFieldReal<R,N>> fields;
    protected BaseUpdate(String request, List<String> fields, PortRequestWithCondition<R> select, Endpoint parent) throws IllegalArgumentException {
        super(request, select, parent);
        this.fields=fields.stream().map(fieldName->makeField(fieldName,tableName,parent)).toList();
    }
    protected  abstract BaseFieldReal<R,N> makeField(String name, String table, Endpoint parent);
}
