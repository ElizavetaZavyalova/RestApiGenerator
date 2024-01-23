package org.example.analize.request.post;

import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.post.insert.BaseInsertRequest;
import org.example.analize.request.post.insert.StringInsertRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;

public class StringPostRequest extends BasePostRequest<String,String> {


    protected StringPostRequest(String url, List<String> params, Endpoint parent) throws IllegalArgumentException {
        super(url, params, parent);
    }

    @Override
    public String interpret() {
        return this.insert.interpret();
    }

    @Override
    public String requestInterpret() {
        return null;
    }

    @Override
    public String getParams() {
        return null;
    }

    @Override
    protected BaseAddress<String, String> make(String url, Endpoint parent) {
        return new StringAddress(url,parent);
    }

    @Override
    BaseInsertRequest<String, String> makeBaseInsertRequest(String request, List<String> fields, PortRequestWithCondition<String, String> select, Endpoint parent) {
        return new StringInsertRequest(request,fields,select,parent);
    }
}
