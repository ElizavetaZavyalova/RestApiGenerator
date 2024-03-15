package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.analize.request.BaseRequest;

import org.example.read_json.rest_controller_json.MakeCast;

import java.util.*;
import java.util.stream.Collectors;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RequestMapping.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.SwaggerConfig.OPERATION_ANNOTATION_CLASS;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.TYPE;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.*;

import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.Ports.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.RequestType._GET;
import static org.example.read_json.rest_controller_json.endpoint.RequestType.*;
import static org.example.read_json.rest_controller_json.endpoint.Type.RegExp.*;


@Getter
@ToString
public class Type {
    final RequestType requestType;
    List<String> paramsBody = new ArrayList<>();
    List<String> returnParams=new ArrayList<>();
    String operationSummary = "";
    String operationTags = "";
    String httpOk = "OK";
    boolean ports=false;
    boolean sort=false;
    boolean fields=false;
    @Setter
    BaseRequest<CodeBlock, MethodSpec.Builder, TypeName> interpretDb;
    public boolean isParamsBodyExist(){
        return !paramsBody.isEmpty()&&!requestType.equals(GET);
    }
    public boolean isGetParamsExist(){
        return fields;
    }
    public String getExampleParams(){
        return interpretDb.getExampleParams();
    }
    public String getExampleEntity(){
        return  interpretDb.getExampleEntity();
    }
    public boolean isHasTags(){
        return !operationTags.isEmpty();
    }

    public TypeName returnParam() {
        return interpretDb.returnParam();
    }

    public record RegExp() {
        public static final String SPLIT_PARAMS = "[|]";
        public static final String SPLIT_TYPE = ":";
        public static final String SPLIT_RETURN = "->";
        public static final int TYPE_PORT = 0;
        public static final int ENTITY_PORT = 1;
        public static final int MAX_PORT_COUNT = 2;

    }
    public Type(String type, Map<String, String> info,Endpoint parent) throws IllegalArgumentException {
        try {
            requestType = RequestType.fromName(type);
            setReturn(info,parent);
            setDefaultStatus();
            setInfo(info);
            createParams(info, parent);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("In: " + type + " " + ex.getMessage());
        }
    }


    public static Type makeType(Map<String, Object> map,Endpoint parent) throws IllegalArgumentException {
        Map<String, String> result = new HashMap<>();
        if(map.containsKey(SUMMARY)){
            result.put(SUMMARY,MakeCast.makeString(map.get(SUMMARY), SUMMARY));
        }
        if(map.containsKey(TAGS)){
            result.put(TAGS,MakeCast.makeString(map.get(TAGS),TAGS));
        }
        if(map.containsKey(HTTP_OK)){
            result.put(HTTP_OK,MakeCast.makeString(map.get(HTTP_OK),HTTP_OK));
        }
        String[] type=MakeCast.makeString(map.get(TYPE), TYPE).split(SPLIT_RETURN);
        String request=type[TYPE_PORT];
        if (type.length > MAX_PORT_COUNT) {
            throw new IllegalArgumentException(map.get(TYPE) + "must be like: request:field1|field2...");
        } else if (type.length == MAX_PORT_COUNT) {
            result.put(RETURN, type[ENTITY_PORT]);
        } else {
            result.put(RETURN, "");
        }
        String[] typePorts = request.split(SPLIT_TYPE);
        if (typePorts.length > MAX_PORT_COUNT) {
            throw new IllegalArgumentException(map.get(TYPE) + "must be like: request:field1|field2...");
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
        AnnotationSpec.Builder mapping;
        switch (requestType) {
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
            default /*GET*/: {
                mapping = AnnotationSpec.builder(GET_MAPPING_ANNOTATION_CLASS);
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
         mapping.addMember("summary", "$S", operationSummary);
         String tags= Arrays.stream(operationTags.split(SPLIT_PARAMS))
                 .filter(s->!s.isEmpty()).map(p->"\""+p+"\"").collect(Collectors.joining(", "));
         if(isHasTags()){
             mapping.addMember( "tags" , "{"+tags+"}");
         }
         return mapping.build();
    }
    void setGetReturn(Map<String, String> info) {
           int countReturn=0;
            String[] result = info.get(RETURN).split(SPLIT_PARAMS);
            for(var res:result){
                if(res.isEmpty()){
                    countReturn++;
                }
                else if(res.equals(SORT)&&!isSort()){
                    sort = true;
                    countReturn++;
                }
                else if(res.equals(FIELDS)&&!isFields()){
                    fields = true;
                    countReturn++;
                }
                else if(res.equals(LIMIT)&&!isPorts()){
                    ports = true;
                    countReturn++;
                }
            }
            if(countReturn!= result.length) {
                throw new IllegalArgumentException(RETURN + " must be like limit|sort|fields ");
            }
    }

    void setReturn(Map<String, String> info,Endpoint parent) throws IllegalArgumentException{
        if (info.containsKey(RETURN)) {
            if (requestType.equals(GET)) {
                setGetReturn(info);
                return;
            }
            createReturnParams(info, parent);
        }
    }


    void createParams(Map<String, String> info,Endpoint parent) {
        String paramList = Optional.ofNullable(info.get(ENTITY)).orElse("");
        paramsBody = Arrays.stream(paramList.split(SPLIT_PARAMS))
                .filter(s -> !s.isEmpty()).map(parent::getEntity)
                .flatMap(List::stream).toList();
        if (!requestType.equals(RequestType.DELETE)) {
            if (requestType.equals(GET)&& !isHaveParams()) {
                return;
            } else if (paramsBody.isEmpty()) {
                throw new IllegalArgumentException(paramList + "must be paramsBody in request post patch put");
            }
        }
        if (requestType.equals(RequestType.DELETE) && isHaveParams()) {
            throw new IllegalArgumentException(paramList + "must be empty for delete");
        }
    }

    void createReturnParams(Map<String, String> info,Endpoint parent) {
        String paramList = Optional.ofNullable(info.get(RETURN)).orElse("");
        returnParams = Arrays.stream(paramList.split(SPLIT_PARAMS)).filter(s -> !s.isEmpty())
                .map(parent::getEntity).flatMap(List::stream).toList();
       if((!returnParams.isEmpty())&&(requestType.equals(DELETE)||requestType.equals(GET))){
           throw new IllegalArgumentException(RETURN + ":must be like: return:field1|field2...");
        }
    }
    private boolean isHaveParams(){
        return !paramsBody.isEmpty();
    }

    void setDefaultStatus() {
        switch (requestType) {
            case GET, PATCH, PUT -> httpOk = "OK";
            case POST -> httpOk = "CREATED";
            case DELETE ->httpOk = "NO_CONTENT";
        }
    }

    void setInfo(Map<String, String> info) {
        if (info.containsKey(HTTP_OK)) {
            httpOk = info.get(HTTP_OK);
        }
        if (info.containsKey(SUMMARY)) {
            operationSummary = info.get(SUMMARY);
        }
        if (info.containsKey(TAGS)) {
            operationTags = info.get(TAGS);
        }
    }

}
