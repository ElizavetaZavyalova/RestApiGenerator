package org.example.analize.request.get;

import org.example.analize.request.BaseRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;

public abstract class BaseGetRequest<R,C> extends BaseRequest<R,C> {
    PortRequestWithCondition<R,C> select;
    protected BaseGetRequest(String url, List<String> fields, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
        select=makeSelect(address.getEndUrl(),fields,address.getSelectCurrent(),parent);
    }
    protected abstract PortRequestWithCondition<R,C> makeSelect(String request, List<String> fields, PortRequestWithCondition<R,C> select, Endpoint parent);
}