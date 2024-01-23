package org.example.analize.request.delete;

import org.example.analize.request.BaseRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

public abstract class BaseDeleteRequest<R, C> extends BaseRequest<R, C> {
    PortRequestWithCondition<R, C> delete;

    protected BaseDeleteRequest(String url, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
        delete = makeDelete(address.getEndUrl(), address.getSelectCurrent(), parent);
    }

    protected abstract PortRequestWithCondition<R, C> makeDelete(String request, PortRequestWithCondition<R, C> select, Endpoint parent);
}
