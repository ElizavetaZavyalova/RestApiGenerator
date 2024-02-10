package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.AnnotationSpec;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.read_json.rest_controller_json.InterpretDb;

import java.util.*;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RequestMapping.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.*;

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

    }

    public AnnotationSpec getMapping(String request){
        AnnotationSpec.Builder mapping = null;
        switch (requestType) {
            case GET : {mapping = AnnotationSpec.builder(GET_MAPPING_ANNOTATION_CLASS); break;}
            case POST : {mapping = AnnotationSpec.builder(POST_MAPPING_ANNOTATION_CLASS);break;}
            case PUT : {mapping = AnnotationSpec.builder(PUT_MAPPING_ANNOTATION_CLASS);break;}
            case PATCH : {mapping = AnnotationSpec.builder(PATCH_MAPPING_ANNOTATION_CLASS);break;}
            case DELETE : {mapping = AnnotationSpec.builder(DELETE_MAPPING_ANNOTATION_CLASS);break;}
        }
        return mapping.addMember("value", "$S",request).build();
    }
    public AnnotationSpec getResponseStatus(){
        AnnotationSpec.Builder mapping = AnnotationSpec.builder(RESPONSE_STATUS_ANNOTATION_CLASS );
        return mapping.addMember("value", "$T."+httpOk,HTTP_STATUS_CLASS).build();
    }
    public AnnotationSpec getOperation(){
        AnnotationSpec.Builder mapping = AnnotationSpec.builder(OPERATION_ANNOTATION_CLASS);
        return mapping.addMember("summary", "$S",operationSummary).build();
    }


    public Type(String type, Map<String, String> info) throws IllegalArgumentException {
        setDefaultStatus();
        setInfo(info);
        requestType = RequestType.fromName(type);
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
            operationSummary = info.get(OPERATION);
        }
    }

}
