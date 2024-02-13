package org.example.analize.request.delete;

import org.example.analize.premetive.info.VarInfo;
import org.example.analize.request.BaseRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public abstract class BaseDeleteRequest<R> extends BaseRequest<R> {
    PortRequestWithCondition<R> delete;

    protected BaseDeleteRequest(String url, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
        delete = makeDelete(address.getEndUrl(), address.getSelectCurrent(), parent);
    }
    @Override
    public void addParams(List<VarInfo> params) {
        delete.addParams(params);
    }

    protected abstract PortRequestWithCondition<R> makeDelete(String request, PortRequestWithCondition<R> select, Endpoint parent);
}
