package org.example.read_json.rest_controller_json.filter;

import lombok.Getter;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.Map;

public class EndpointFilters extends Filters {
    @Getter
    Endpoint parent;

    public EndpointFilters(Map<String, String> filters, Endpoint parent) throws IllegalArgumentException {
        super(filters);
        this.parent = parent;
    }

    String makeFilterVoidName(String key) {
        return parent.getFuncName() + key.substring(0, 1).toUpperCase() + key.substring(1);
    }
}
