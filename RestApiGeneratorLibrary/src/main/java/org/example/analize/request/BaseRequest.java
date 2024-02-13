package org.example.analize.request;

import lombok.Getter;
import org.example.analize.address.BaseAddress;
import org.example.analize.interpretation.Interpretation;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.RequestType.*;

public abstract class BaseRequest<R> implements Interpretation<R> {
    protected BaseAddress<R> address = null;

    @Getter
    protected enum RequestType {
        GET(_GET), POST(_POST), PUT(_PUT), PATCH(_PATCH), DELETE(_DELETE);
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

    protected abstract BaseAddress<R> make(String url, Endpoint parent);


}
