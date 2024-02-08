package org.example.analize.request;

import lombok.Getter;
import org.example.analize.address.BaseAddress;
import org.example.analize.interpretation.Interpretation;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

public abstract class BaseRequest<R, C> implements Interpretation<R> {
    protected BaseAddress<R, C> address = null;

    @Getter
    protected enum RequestType {
        GET("get"), POST("post"), PUT("put"), PATCH("patch"), DELETE("delete");
        private final String typeName;

        RequestType(String typeName) {
            this.typeName = typeName;
        }
    }

    protected BaseRequest(String url, Endpoint parent) throws IllegalArgumentException {
        address = make(url, parent);
        if (address.getEndUrl().isEmpty()) {
            throw new IllegalArgumentException("NO URL");
        }
    }

    protected abstract BaseAddress<R, C> make(String url, Endpoint parent);


}
