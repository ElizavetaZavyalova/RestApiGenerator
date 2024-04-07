package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.MakeCast;
import org.example.read_json.rest_controller_json.RequestFactory;


import javax.lang.model.element.Modifier;

import java.util.*;
import java.util.stream.Collectors;


import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.SwaggerConfig.PARAMETER_ANNOTATION_CLASS;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.SwaggerConfig.SCHEMA_ANNOTATION_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_BODY;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_NAME;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.Ports.*;

import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.RequestType.*;
import static org.example.read_json.rest_controller_json.endpoint.RequestInformation.KeyWords.*;


@Setter
@Getter
@Slf4j
@ToString
public class RequestInformation {
    String request;
    Types types;
    List<VarInfo> varInfos;
    List<FilterInfo> filterInfos;

    record KeyWords() {
        static final String DEFAULT_VALUE = "defaultValue";
        static final String DESCRIPTION = "description";
        static final String EXAMPLE = "example";
        static final String NAME = "name";
        static final String REQUIRED = "required";
        static final String VALUE = "value";
    }

    public boolean isParamsExist(Type type) {
        return type.isGetParamsExist();
    }


    @Getter
    @ToString
    static class Types {
        private final List<Type> typeList;

        boolean isHasType(Map<String, Object> enpointMap) {
            return enpointMap.containsKey(_GET) || enpointMap.containsKey(_POST) ||
                    enpointMap.containsKey(_DELETE) || enpointMap.containsKey(_PATCH) ||
                    enpointMap.containsKey(_PUT);
        }

        boolean isMoreThenOneParamInRequest(Map<String, Object> enpointMap) {
            return enpointMap.containsKey(TYPES) && enpointMap.containsKey(TYPE) ||
                    enpointMap.containsKey(TYPES) && isHasType(enpointMap) ||
                    isHasType(enpointMap) && enpointMap.containsKey(TYPE);

        }

        void throwExceptionIfRequestIsNotCorrect(Map<String, Object> enpointMap) throws IllegalArgumentException {
            if (isMoreThenOneParamInRequest(enpointMap)) {
                throw new IllegalArgumentException("not use:" +
                        TYPES + " and " + TYPE + " and ["
                        + _DELETE + " " + _GET + " " + _PUT + " " + _PATCH + " " + _POST + "] in one endpoint");
            }
            if (!enpointMap.containsKey(TYPE) && enpointMap.containsKey(SUMMARY)) {
                throw new IllegalArgumentException("use operation only with:" +
                        TYPE);
            }
        }

        String getTags(Map<String, Object> enpointMap) {
            if (enpointMap.containsKey(TAGS)) {
                return MakeCast.makeString(enpointMap.get(TAGS), TAGS);
            }
            return "";
        }


        Types(Map<String, Object> enpointMap, Endpoint parent) throws IllegalArgumentException {
            throwExceptionIfRequestIsNotCorrect(enpointMap);
            if (enpointMap.containsKey(TYPES)) {
                typeList = MakeCast.makeMapOfStringMap(enpointMap, TYPES, ENTITY, false)
                        .entrySet().stream().map(e -> new Type(e.getKey(), e.getValue(), parent)).toList();
                return;
            } else if (enpointMap.containsKey(TYPE)) {
                typeList = List.of(Type.makeType(enpointMap, parent));
                return;
            } else if (isHasType(enpointMap)) {
                typeList = Arrays.stream(RequestType.values()).map(RequestType::getType)
                        .filter(enpointMap::containsKey)
                        .map(t -> Type.makeType(
                                Map.of(
                                        TYPE, t + ":" + MakeCast.makeString(enpointMap.get(t), t),
                                        TAGS, getTags(enpointMap)
                                ), parent)).toList();
                return;
            }
            typeList = List.of(new Type(_GET, new HashMap<>(), parent));
        }
    }

    RequestInformation(Map<String, Object> enpointMap, Endpoint parent) throws IllegalArgumentException {
        types = new Types(enpointMap, parent);
        request = MakeCast.makeStringIfContainsKeyMap(enpointMap, REQUEST, true).replace(" ", "");
    }

    public void generateBd(Endpoint endpoint) {
        for (Type type : types.getTypeList()) {
            type.setInterpretDb(RequestFactory.createRequestFromType(endpoint, type));
        }
        varInfos = new ArrayList<>();
        filterInfos = new ArrayList<>();
    }

    List<ParameterSpec> getFilterDBNamesParameters() {
        return filterInfos.stream().map(f -> ParameterSpec.builder(REQUEST_PARAMS, f.getVarName()).build()).toList();
    }

    List<ParameterSpec> getFilterControllerNameParameters() {
        return filterInfos.stream().map(f -> ParameterSpec.builder(REQUEST_PARAMS, f.getVarName())
                .addAnnotation(AnnotationSpec.builder(REQUEST_PARAM_ANNOTATION_CLASS)
                        .addMember(DEFAULT_VALUE, "$S", "{}").build())
                .addAnnotation(AnnotationSpec.builder(PARAMETER_ANNOTATION_CLASS)
                        .addMember(NAME, "$S", f.getVarName())
                        .addMember(EXAMPLE, "$S", f.getExample()).build())
                .build()).toList();
    }

    String getFilterCallDBParameters() {
        return filterInfos.stream().map(FilterInfo::getVarName).collect(Collectors.joining(","));
    }

    public MethodSpec makeDBMethod(String funcName, Type type) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(type.getRequestType().toString() + funcName)
                .addModifiers(Modifier.PUBLIC);
        methodBuilder.addAnnotation(AnnotationSpec.builder(TRANSACTIONAL_ANNOTATION_CLASS).build());
        methodBuilder = addReturns(methodBuilder, type);
        if (isParamsExist(type)) {
            methodBuilder.addParameter(REQUEST_PARAMS, REQUEST_PARAM_NAME);
        }
        for (VarInfo parameterSpec : varInfos) {
            methodBuilder.addParameter(parameterSpec.getParameterSpec());
        }
        if (type.isParamsBodyExist()) {
            methodBuilder.addParameter(ParameterSpec.builder(PARAMETRIZED_MAP, REQUEST_PARAM_BODY).build());
        }
        for (var filter : getFilterDBNamesParameters()) {
            methodBuilder.addParameter(filter);
        }
        if (type.isPorts()) {
            methodBuilder.addParameter(ParameterSpec.builder(TypeName.LONG, LIMIT).build());
            methodBuilder.addParameter(ParameterSpec.builder(TypeName.LONG, OFFSET).build());
        }
        if (type.isSort()) {
            methodBuilder.addParameter(ParameterSpec.builder(STRING_CLASS, SORT).build());
            methodBuilder.addParameter(ParameterSpec.builder(TypeName.BOOLEAN, DIRECTION).build());
        }
        return type.getInterpretDb().makeMethodBody(methodBuilder).build();
    }

    MethodSpec.Builder addParametersToControllerMethod(MethodSpec.Builder methodBuilder, Type type) {
        if (isParamsExist(type)) {
            methodBuilder.addParameter(ParameterSpec.builder(REQUEST_PARAMS, REQUEST_PARAM_NAME)
                    .addAnnotation(AnnotationSpec.builder(REQUEST_PARAM_ANNOTATION_CLASS)
                            .addMember(DEFAULT_VALUE, "$S", "{}").build())
                    .addAnnotation(AnnotationSpec.builder(PARAMETER_ANNOTATION_CLASS)
                            .addMember(NAME, "$S", REQUEST_PARAM_NAME)
                            .addMember(EXAMPLE, "$S", type.getExampleParams()).build())
                    .build());
        }
        for (VarInfo parameterSpec : varInfos) {
            methodBuilder.addParameter(parameterSpec.getAnnotationParameterSpec());
        }
        if (type.isParamsBodyExist()) {
            methodBuilder.addParameter(ParameterSpec.builder(PARAMETRIZED_MAP, REQUEST_PARAM_BODY)
                    .addAnnotation(AnnotationSpec.builder(REQUEST_BODY_ANNOTATION_CLASS)
                            .addMember(REQUIRED, "true").build())
                    .addAnnotation(AnnotationSpec.builder(SCHEMA_ANNOTATION_CLASS)
                            .addMember(NAME, "$S", REQUEST_PARAM_BODY)
                            .addMember(EXAMPLE, "$S", type.getExampleEntity())
                            .build()).build());
        }
        for (var filter : getFilterControllerNameParameters()) {
            methodBuilder.addParameter(filter);
        }
        if (type.isPorts()) {
            methodBuilder.addParameter(ParameterSpec.builder(TypeName.LONG, LIMIT)
                    .addAnnotation(AnnotationSpec.builder(MIN_PARAM_ANNOTATION_CLASS)
                            .addMember(VALUE, "0").build())
                    .addAnnotation(AnnotationSpec.builder(REQUEST_PARAM_ANNOTATION_CLASS)
                            .addMember(DEFAULT_VALUE, "$S", "9223372036854775807").build())
                    .addAnnotation(AnnotationSpec.builder(PARAMETER_ANNOTATION_CLASS)
                            .addMember(NAME, "$S", LIMIT)
                            .addMember(EXAMPLE, "$S", "1").build()).build());
            methodBuilder.addParameter(ParameterSpec.builder(TypeName.LONG, OFFSET)
                    .addAnnotation(AnnotationSpec.builder(MIN_PARAM_ANNOTATION_CLASS)
                            .addMember(VALUE, "0").build())
                    .addAnnotation(AnnotationSpec.builder(REQUEST_PARAM_ANNOTATION_CLASS)
                            .addMember(DEFAULT_VALUE, "$S", "0").build()).build());
        }
        if (type.isSort()) {
            methodBuilder.addParameter(ParameterSpec.builder(STRING_CLASS, SORT)
                    .addAnnotation(AnnotationSpec.builder(REQUEST_PARAM_ANNOTATION_CLASS).build())
                    .addAnnotation(AnnotationSpec.builder(PARAMETER_ANNOTATION_CLASS)
                            .addMember(NAME, "$S", SORT)
                            .addMember(EXAMPLE, "$S", "fieldName").build()).build());
            methodBuilder.addParameter(ParameterSpec.builder(TypeName.BOOLEAN, DIRECTION)
                    .addAnnotation(AnnotationSpec.builder(REQUEST_PARAM_ANNOTATION_CLASS)
                            .addMember(DEFAULT_VALUE, "$S", "true").build())
                    .addAnnotation(AnnotationSpec.builder(PARAMETER_ANNOTATION_CLASS)
                            .addMember(NAME, "$S", DIRECTION)
                            .addMember(EXAMPLE, "$S", "true")
                            .addMember(DESCRIPTION, "$S", "asc=true, desc=false").build()).build());
        }
        return methodBuilder;
    }

    StringBuilder addACommaIfBuilderIsNotEmpty(StringBuilder params) {
        if (!params.isEmpty()) {
            params.append(", ");
        }
        return params;
    }

    StringBuilder addParametersToCallBDMethodInController(Type type) {
        StringBuilder params = new StringBuilder();
        if (isParamsExist(type)) {
            params.append(REQUEST_PARAM_NAME);
        }
        for (VarInfo parameterSpec : varInfos) {
            params = addACommaIfBuilderIsNotEmpty(params).append(parameterSpec.getName());
        }
        if (type.isParamsBodyExist()) {
            params = addACommaIfBuilderIsNotEmpty(params).append(REQUEST_PARAM_BODY);
        }
        String filterParams = getFilterCallDBParameters();
        if (!filterParams.isEmpty()) {
            params = addACommaIfBuilderIsNotEmpty(params).append(filterParams);
        }
        if (type.isPorts()) {
            params = addACommaIfBuilderIsNotEmpty(params).append(LIMIT);
            params = addACommaIfBuilderIsNotEmpty(params).append(OFFSET);
        }
        if (type.isSort()) {
            params = addACommaIfBuilderIsNotEmpty(params).append(SORT);
            params = addACommaIfBuilderIsNotEmpty(params).append(DIRECTION);
        }

        return params;
    }


    public MethodSpec makeControllerMethod(String funcName, Type type, String beanName) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(type.getRequestType().toString() + funcName)
                .addModifiers(Modifier.PUBLIC).addAnnotation(type.getMapping(request))
                .addAnnotation(type.getResponseStatus()).addAnnotation(type.getOperation());
        methodBuilder = addParametersToControllerMethod(methodBuilder, type);
        return addReturns(methodBuilder, type).addStatement(getReturnIfNotVoid(type) + beanName + "." + type.getRequestType().toString() + funcName +
                "(" + addParametersToCallBDMethodInController(type) + ")").build();
    }


    public List<MethodSpec> makeControllerMethods(String funcName, String beanName) {
        List<Type> typeList = this.types.getTypeList();
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (Type type : typeList) {
            methodSpecs.add(makeControllerMethod(funcName, type, beanName));
        }
        return methodSpecs;
    }

    public void generateVarInfos() {
        types.getTypeList().get(0).getInterpretDb().addParams(varInfos, filterInfos);
        filterInfos = filterInfos.stream().distinct().toList();
        filterInfos.forEach(f -> request = f.replaceRequest(request));
        request = request.replace(">", "")
                .replace("<", "")
                .replace("[", "")
                .replace("]", "");
    }

    public List<MethodSpec> makeDBMethods(String funcName) {
        List<Type> typeList = this.types.getTypeList();
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (Type type : typeList) {
            methodSpecs.add(makeDBMethod(funcName, type));
        }
        return methodSpecs;
    }

    String getReturnIfNotVoid(Type type) {
        if (!type.returnParam().equals(TypeName.VOID)) {
            return "return ";
        }
        return "";
    }

    MethodSpec.Builder addReturns(MethodSpec.Builder builder, Type type) {
        return builder.returns(type.returnParam());
    }
}
