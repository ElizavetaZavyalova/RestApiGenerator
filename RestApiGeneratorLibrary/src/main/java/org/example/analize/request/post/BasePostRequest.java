package org.example.analize.request.post;

import org.example.analize.request.BaseRequest;
import org.example.analize.request.post.insert.BaseInsertRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;

public abstract class BasePostRequest<R,C> extends BaseRequest<R,C> {
    protected BaseInsertRequest<R,C> insert;
    protected BasePostRequest(String url, List<String> params, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
        insert=makeBaseInsertRequest(address.getEndUrl(),params,address.getSelectCurrent(),parent);
    }
    abstract BaseInsertRequest<R,C> makeBaseInsertRequest(String request, List<String> fields, PortRequestWithCondition<R, C> select, Endpoint parent);

}
