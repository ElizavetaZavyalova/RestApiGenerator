package org.example.analize.request;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.analize.address.BaseAddress;

import org.example.analize.interpretation.InterpretationParams;
import org.example.analize.interpretation.InterpretationRequestDBBody;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;


@NoArgsConstructor(access=AccessLevel.PROTECTED)
public abstract class BaseRequest<R,M,P> implements InterpretationRequestDBBody<M,P>, InterpretationParams {
    protected BaseAddress<R> address = null;

    protected BaseRequest(String url, Endpoint parent) throws IllegalArgumentException {
      init(url,parent);
    }
    protected void init(String url, Endpoint parent)throws IllegalArgumentException{
        address = make(url, parent);
        if (address.getEndUrl().isEmpty()) {
            throw new IllegalArgumentException("NO URL");
        }
    }
    public String getExampleParams(){
        return "{}";
    }
    public String getExampleEntity(){
        return "{}";
    }
    protected abstract BaseAddress<R> make(String url, Endpoint parent);


}
