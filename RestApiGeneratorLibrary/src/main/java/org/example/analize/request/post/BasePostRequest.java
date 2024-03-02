package org.example.analize.request.post;

import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.post.insert.BaseInsertRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public abstract class BasePostRequest<R,M> extends BaseRequest<R,M> {
    protected BaseInsertRequest<R> insert;

    protected BasePostRequest(String url, List<String> params, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
        insert = makeBaseInsertRequest(address.getEndUrl(), params, address.getSelectCurrent(), parent);
    }

    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        this.insert.addParams(params,filters);
    }

    abstract BaseInsertRequest<R> makeBaseInsertRequest(String request, List<String> fields, PortRequestWithCondition<R> select, Endpoint parent);

}
