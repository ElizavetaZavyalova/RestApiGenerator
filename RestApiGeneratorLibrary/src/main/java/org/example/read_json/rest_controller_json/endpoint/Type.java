package org.example.read_json.rest_controller_json.endpoint;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.read_json.rest_controller_json.InterpretDb;

import java.util.*;

import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.*;
import static org.example.read_json.rest_controller_json.endpoint.Type.RegExp.*;

@Getter
@ToString
public class Type {
    final RequestType requestType;
    List<String> params = new ArrayList<>();
    String operation = "";
    String httpOk = "OK";
    String httpException = "NOT_FOUND";
    @Setter
    InterpretDb interpretDb;

    public record RegExp() {
        public static final String SPLIT_PARAMS = "[|]";

    }

    public Type(String type, Map<String, String> info) throws IllegalArgumentException {
        setInfo(info);
        requestType = RequestType.fromName(type);
        setDefaultStatus();
        createParams(info);
    }

    void createParams(Map<String, String> info) {
        String paramList = Optional.ofNullable(info.get(ENTITY)).orElse("");
        params = Arrays.stream(paramList.split(SPLIT_PARAMS)).filter(s -> !s.isEmpty()).toList();
        if (!requestType.equals(RequestType.DELETE)) {
            if (requestType.equals(RequestType.GET) && params.isEmpty()) {
                return;
            } else if (!requestType.equals(RequestType.GET) && params.isEmpty()) {
                throw new IllegalArgumentException(paramList + "MUST be params in request post patch put");
            }
        }
        if (requestType.equals(RequestType.DELETE) && !params.isEmpty()) {
            throw new IllegalArgumentException(paramList + "MUST be empty for delete");
        }
    }

    void setDefaultStatus() {
        //TODO default
        switch (requestType) {
            case GET -> {
                httpOk = "OK";
                httpException = "NOT_FOUND";
            }
            case POST -> {
                httpOk = "CREATED";
            }
            case PATCH -> {
                httpOk = "OK";

            }
            case DELETE -> {
                httpOk = "NO_CONTENT";
            }
            case PUT -> {
                httpOk = "OK";
            }
        }
    }

    void setInfo(Map<String, String> info) {
        if (info.containsKey(HTTP_OK)) {
            httpOk = info.get(HTTP_OK);
        }
        if (info.containsKey(HTTP_EXCEPTION)) {
            httpException = info.get(HTTP_EXCEPTION);
        }
        if (info.containsKey(OPERATION)) {
            operation = info.get(OPERATION);
        }
    }

}
