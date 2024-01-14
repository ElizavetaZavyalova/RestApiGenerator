package org.example.read_json.rest_controller_json;

public enum RequestType {
    GET,POST,PUT,DELETE,PATCH;
    static RequestType fromName(String name){
        return switch (name) {
            case "GET" -> GET;
            case "POST" -> POST;
            case "PUT" -> PUT;
            case "PATCH" -> PATCH;
            case "DELETE" -> DELETE;
            default -> throw new IllegalArgumentException("NO REQUEST TYPE:" + name);
        };
    }
}
