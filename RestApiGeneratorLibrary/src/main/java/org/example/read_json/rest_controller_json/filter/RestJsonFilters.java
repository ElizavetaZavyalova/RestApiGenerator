package org.example.read_json.rest_controller_json.filter;

import org.example.read_json.rest_controller_json.RestJson;

import java.util.Map;

public class RestJsonFilters extends Filters{
    RestJson parent;
    public RestJsonFilters(Map<String, String> filters, RestJson parent) throws IllegalArgumentException {
        super(filters);
        this.parent=parent;
    }
}
