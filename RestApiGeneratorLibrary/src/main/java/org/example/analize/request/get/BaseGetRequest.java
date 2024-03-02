package org.example.analize.request.get;

import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.request.BaseRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public abstract class BaseGetRequest<R,M> extends BaseRequest<R,M> {
    PortRequestWithCondition<R> select;
    protected BaseGetRequest(String url, List<String> fields, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
        select=makeSelect(address.getEndUrl(),fields,address.getSelectCurrent(),parent);
    }
    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        select.addParams(params,filters);
    }
    protected abstract PortRequestWithCondition<R> makeSelect(String request, List<String> fields, PortRequestWithCondition<R> select, Endpoint parent);
}
