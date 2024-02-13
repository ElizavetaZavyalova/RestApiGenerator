package org.example.analize.request.delete.delete;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

public abstract class BaseDelete<R> extends PortRequestWithCondition<R> {
    protected BaseDelete(String request, PortRequestWithCondition<R> select, Endpoint parent) {
        super(request, select, parent);
    }
}
