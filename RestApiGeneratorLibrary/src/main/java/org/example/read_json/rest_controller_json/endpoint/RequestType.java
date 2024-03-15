package org.example.read_json.rest_controller_json.endpoint;


import lombok.Getter;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.LoggerColor.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.RequestType.*;

public enum RequestType {
    GET(_GET,_GET_COLOR),
    POST(_POST,_POST_COLOR),
    PUT(_PUT,_PUT_COLOR),
    DELETE(_DELETE,_DELETE_COLOR),
    PATCH(_PATCH,_PATCH_COLOR);
    @Getter
    private static final String RESET=_RESET_COLOR;
    @Getter
    private final String type;
    @Getter
    private final String typeColor;

    RequestType(String type,String typeColor) {
        this.type = type;
        this.typeColor=typeColor;
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
            default -> throw new IllegalArgumentException("no request type:" + name);
        };
    }
}
