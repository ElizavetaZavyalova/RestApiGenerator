package org.example.read_json.rest_controller_json;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


import static org.example.read_json.rest_controller_json.Endpoint.KeyWords.*;
import static org.example.read_json.rest_controller_json.Endpoint.KeyWords.Regexp.REQUEST_TYPE_SEPARATOR;

@Slf4j
public class Endpoint {
    String request;
    Set<RequestType> requestTypes = new HashSet<>();
    Boolean perms = false;
    String swagger;
    Endpoint(Map<String, Object> enpointMap) {
        try {
            request = MakeCast.makeString(enpointMap, REQUEST, true);
            perms = MakeCast.makeBoolean(enpointMap, PERMS, false);
            requestTypes = parseRequestTypes(MakeCast.makeString(enpointMap, TYPES, true));
            swagger = MakeCast.makeString(enpointMap, SWAGGER, true);
        } catch (IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            //TODO stop compile
        }
    }

    Set<RequestType> parseRequestTypes(String types) throws IllegalArgumentException {
        return Arrays.stream(types.split(REQUEST_TYPE_SEPARATOR))
                .map(RequestType::fromName).collect(Collectors.toSet());
    }

    record KeyWords() {
        static String REQUEST = "request";
        static String PERMS = "perms";
        static String TYPES = "types";

        record Regexp() {
            static String REQUEST_TYPE_SEPARATOR = "\\|";
        }
        static String SWAGGER = "swagger";

    }

}
