package org.example.analize.request.get;

import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;
import org.example.analize.request.get.select.StringGetSelect;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;
import java.util.List;

public class StringGetRequest extends BaseGetRequest<String,String>{
    protected StringGetRequest(String url, List<String> fields, Endpoint parent) throws IllegalArgumentException {
        super(url, fields, parent);
    }

    @Override
    public String interpret() {
        return select.interpret();
    }

    @Override
    public String requestInterpret() {
        return select.requestInterpret();
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
    protected PortRequestWithCondition<String, String> makeSelect(String request, List<String> fields, PortRequestWithCondition<String,String> select, Endpoint parent) {
        return new StringGetSelect(request,select,fields,parent);
    }
}
