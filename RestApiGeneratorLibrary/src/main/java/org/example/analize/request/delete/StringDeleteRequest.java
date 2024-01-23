package org.example.analize.request.delete;

import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;
import org.example.analize.request.delete.delete.StringDelete;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

public class StringDeleteRequest extends BaseDeleteRequest<String,String>{
    protected StringDeleteRequest(String url, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
    }

    @Override
    public String interpret() {
        return delete.interpret();
    }

    @Override
    public String requestInterpret() {
        return delete.requestInterpret();
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
    protected PortRequestWithCondition<String, String> makeDelete(String request, PortRequestWithCondition<String,String> select, Endpoint parent) {
        return new StringDelete(request,select,parent);
    }
}
