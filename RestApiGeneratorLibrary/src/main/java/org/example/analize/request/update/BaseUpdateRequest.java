package org.example.analize.request.update;

import org.example.analize.request.BaseRequest;
import org.example.analize.request.update.update.BaseUpdate;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;

public abstract class BaseUpdateRequest<R,C> extends BaseRequest<R,C> {
    BaseUpdate<R,C> update;
    protected BaseUpdateRequest(String url, List<String> fields, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
        update=makeUpdate(address.getEndUrl(),fields,address.getSelectCurrent(),parent);
    }
    @Override
    public R interpret() {
        return update.interpret();
    }

    @Override
    public String requestInterpret() {
        return update.requestInterpret();
    }

    @Override
    public String getParams() {
        return null;
    }
    protected abstract BaseUpdate<R,C> makeUpdate(String request, List<String> fields, PortRequestWithCondition<R,C> select, Endpoint parent);
}