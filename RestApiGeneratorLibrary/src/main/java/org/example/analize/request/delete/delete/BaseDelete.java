package org.example.analize.request.delete.delete;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

public abstract class BaseDelete<R,C> extends PortRequestWithCondition<R,C> {
    protected BaseDelete(String request, PortRequestWithCondition<R,C> select, Endpoint parent) {
        super(request, select, parent);
    }
}
