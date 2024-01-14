package org.example.read_json.rest_controller_json;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class Endpoints {
    Map<String, Endpoint> endpoint = new TreeMap<>();

    Endpoints(Map<String, Object> map) {
        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                endpoint.put(entry.getKey(), new Endpoint(MakeCast.makeMap(entry.getValue(), entry.getKey())));
            }
        } catch (IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            //TODO exception
        }
    }


}
