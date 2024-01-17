package org.example.read_json.rest_controller_json.filter;

import lombok.Getter;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.Map;

public class EndpointFilters extends Filters{
    @Getter
    Endpoint parent;
    public EndpointFilters(Map<String, String> filters, Endpoint parent) throws IllegalArgumentException {
        super(filters);
        this.parent=parent;
    }
}
