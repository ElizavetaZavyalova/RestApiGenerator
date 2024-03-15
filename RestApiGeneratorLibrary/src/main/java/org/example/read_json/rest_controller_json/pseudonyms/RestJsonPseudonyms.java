package org.example.read_json.rest_controller_json.pseudonyms;

import lombok.Getter;
import org.example.read_json.rest_controller_json.RestJson;

import java.util.List;
import java.util.Map;

@Getter
public class RestJsonPseudonyms extends Pseudonyms {
    RestJson parent;

    public RestJsonPseudonyms(Map<String, Map<String, List<String>>> pseudonyms, RestJson parent) throws IllegalArgumentException {
        super(pseudonyms);
        this.parent = parent;
    }
}
