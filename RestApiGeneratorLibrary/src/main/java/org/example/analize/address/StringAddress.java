package org.example.analize.address;

import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.StringSelect;
import org.example.read_json.rest_controller_json.Endpoint;

public class StringAddress extends BaseAddress<String,String>{
    public StringAddress(String url, Endpoint parent) {
        super(url, parent);
    }

    @Override
    PortRequestWithCondition<String,String> makeSelect(String request, PortRequestWithCondition<String,String> select, Endpoint parent) {
        return new StringSelect(request,select,parent);
    }

    @Override
    public String interpret() {
        if(selectCurrent!=null){
            return selectCurrent.interpret();
        }
        return "";
    }

    @Override
    public String requestInterpret() {
        if(selectCurrent!=null){
            return selectCurrent.requestInterpret();
        }
        return "";
    }

    @Override
    public String getParams() {
        return null;
    }
}
