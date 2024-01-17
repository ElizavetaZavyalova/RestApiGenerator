package org.example.read_json.rest_controller_json;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class Endpoints {
    Map<String, Endpoint> endpoint = new TreeMap<>();
    @Getter
    RestJson parent;

    Endpoints(Map<String, Object> map,RestJson parent) {
        try {
            this.parent=parent;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                endpoint.put(entry.getKey(), new Endpoint(MakeCast.makeMap(entry.getValue(), entry.getKey()),this));
            }
        } catch (IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            //TODO exception
        }
    }


}
