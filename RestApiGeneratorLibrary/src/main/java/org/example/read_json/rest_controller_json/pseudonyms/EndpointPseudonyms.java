package org.example.read_json.rest_controller_json.pseudonyms;

import lombok.Getter;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;
import java.util.Map;

@Getter
public class EndpointPseudonyms extends Pseudonyms {
    Endpoint parent;

    public EndpointPseudonyms(Map<String, Map<String, List<String>>> pseudonyms, Endpoint parent) throws IllegalArgumentException {
        super(pseudonyms);
        this.parent = parent;
    }
}
