package org.example.read_json.rest_controller_json.endpoint;


import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.RequestType.*;

public enum RequestType {
    GET(_GET), POST(_POST), PUT(_PUT), DELETE(_DELETE), PATCH(_PATCH);
    private final String type;

    RequestType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

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
