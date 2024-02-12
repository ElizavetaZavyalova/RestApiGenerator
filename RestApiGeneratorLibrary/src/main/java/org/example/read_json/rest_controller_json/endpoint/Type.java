package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.AnnotationSpec;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.read_json.rest_controller_json.InterpretDb;
import org.example.read_json.rest_controller_json.MakeCast;

import java.util.*;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RequestMapping.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.TYPE;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.*;

import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.RequestType._GET;
import static org.example.read_json.rest_controller_json.endpoint.Type.RegExp.*;

@Getter
@ToString
public class Type {
    final RequestType requestType;
    List<String> params = new ArrayList<>();
    String operationSummary = "";
    String httpOk = "OK";
    String httpException = "NOT_FOUND";
    @Setter
    InterpretDb interpretDb;

    public record RegExp() {
        public static final String SPLIT_PARAMS = "[|]";
        public static final String SPLIT_TYPE = ":";
        public static final int TYPE_PORT = 0;
        public static final int ENTITY_PORT = 1;
        public static final int MAX_PORT_COUNT = 2;

    }

    public static Type makeType(Map<String, Object> map,Endpoint parent) throws IllegalArgumentException {
        Map<String, String> result = new HashMap<>();
        String[] typePorts = MakeCast.makeString(map.get(TYPE), TYPE).split(SPLIT_TYPE);
        if (typePorts.length > MAX_PORT_COUNT) {
            throw new IllegalArgumentException(map.get(TYPE) + "MUST BE LIKE: request:field1|field2...");
        } else if (typePorts.length == MAX_PORT_COUNT) {
            result.put(ENTITY, typePorts[ENTITY_PORT]);
        } else {
            result.put(ENTITY, "");
        }
        if (typePorts[TYPE_PORT].isEmpty()) {
            typePorts[TYPE_PORT] = _GET;
        }
        return new Type(typePorts[TYPE_PORT],result,parent);
    }

    public AnnotationSpec getMapping(String request) {
        AnnotationSpec.Builder mapping = null;
        switch (requestType) {
            case GET: {
                mapping = AnnotationSpec.builder(GET_MAPPING_ANNOTATION_CLASS);
                break;
            }
            case POST: {
                mapping = AnnotationSpec.builder(POST_MAPPING_ANNOTATION_CLASS);
                break;
            }
            case PUT: {
                mapping = AnnotationSpec.builder(PUT_MAPPING_ANNOTATION_CLASS);
                break;
            }
            case PATCH: {
                mapping = AnnotationSpec.builder(PATCH_MAPPING_ANNOTATION_CLASS);
                break;
            }
            case DELETE: {
                mapping = AnnotationSpec.builder(DELETE_MAPPING_ANNOTATION_CLASS);
                break;
            }
        }
        return mapping.addMember("value", "$S", request).build();
    }

    public AnnotationSpec getResponseStatus() {
        AnnotationSpec.Builder mapping = AnnotationSpec.builder(RESPONSE_STATUS_ANNOTATION_CLASS);
        return mapping.addMember("value", "$T." + httpOk, HTTP_STATUS_CLASS).build();
    }

    public AnnotationSpec getOperation() {
        AnnotationSpec.Builder mapping = AnnotationSpec.builder(OPERATION_ANNOTATION_CLASS);
        return mapping.addMember("summary", "$S", operationSummary).build();
    }


    public Type(String type, Map<String, String> info,Endpoint parent) throws IllegalArgumentException {
        requestType = RequestType.fromName(type);
        setDefaultStatus();
        setInfo(info);
        createParams(info,parent);
    }

    void createParams(Map<String, String> info,Endpoint parent) {
        String paramList = Optional.ofNullable(info.get(ENTITY)).orElse("");
        params = Arrays.stream(paramList.split(SPLIT_PARAMS)).filter(s -> !s.isEmpty())
                .map(parent::getEntity).flatMap(List::stream).toList();
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
            operationSummary = info.get(OPERATION);
        }
    }

}
