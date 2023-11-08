package org.example.analize.request.get;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.address.address.BaseAddress;
import org.example.analize.address.address.BuildAddress;
import org.example.analize.address.select.BaseSelectParser;
import org.example.analize.request.get.select.BuildSelectOfGet;

@Slf4j
public class BuildGetRequest extends BaseGetRequest<String,String,String,String,String>{
    protected BuildGetRequest(String request) {
        super(request);
        Debug.debug(log,"BuildGetRequest request:", request);
    }

    @Override
    public String interpret() {
        return Debug.debugResult(log,"interpret",getResult.interpret());
    }

    @Override
    protected BaseAddress<String, String, String, String> makeAddress(String[] request) {
        Debug.debug(log," makeAddress request:", request);
        return new BuildAddress(request,variables);
    }

    @Override
    protected BaseSelectParser<String, String, String, String> makeGetResult(String request) {
        Debug.debug(log,"makeGetResult request:", request);
        return new BuildSelectOfGet(request,variables,address.getAddress());
    }
}
