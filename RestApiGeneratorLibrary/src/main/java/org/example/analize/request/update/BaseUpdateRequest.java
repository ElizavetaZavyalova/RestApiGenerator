package org.example.analize.request.update;

import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.update.update.BaseUpdate;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public abstract class BaseUpdateRequest<R,M> extends BaseRequest<R,M> {
    protected BaseUpdate<R> update;
    protected BaseUpdateRequest(String url, List<String> fields, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
        update=makeUpdate(address.getEndUrl(),fields,address.getSelectCurrent(),parent);
    }



    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        update.addParams(params,filters);
    }
    protected abstract BaseUpdate<R> makeUpdate(String request, List<String> fields, PortRequestWithCondition<R> select, Endpoint parent);
}
