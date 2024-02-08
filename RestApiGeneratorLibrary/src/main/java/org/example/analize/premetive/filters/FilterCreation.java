package org.example.analize.premetive.filters;

import org.example.read_json.rest_controller_json.endpoint.Endpoint;

@FunctionalInterface
public interface FilterCreation<C> {
    public void makeFilter(Endpoint parent,  C def, String table);
}
