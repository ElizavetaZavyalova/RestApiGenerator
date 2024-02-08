package org.example.read_json.rest_controller_json.endpoint;



import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.RequestType.*;

public enum RequestType {
    GET, POST, PUT, DELETE, PATCH;

    public static RequestType fromName(String name) {
        return switch (name) {
            case _GET -> GET;
            case _POST -> POST;
            case _PUT -> PUT;
            case _PATCH -> PATCH;
            case _DELETE -> DELETE;
            default -> throw new IllegalArgumentException("NO REQUEST TYPE:" + name);
        };
    }
}
