package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.MakeCast;
import org.example.read_json.rest_controller_json.RequestFactory;


import javax.lang.model.element.Modifier;
import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;


import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.SwaggerConfig.PARAMETER_ANNOTATION_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.RESULT_OF_RECORD_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_BODY;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_NAME;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.ENTITY;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.RequestType._GET;


@Setter
@Getter
@ToString
public class RequestInformation {
    String request;
    Types types;
    List<VarInfo> varInfos;
    List<FilterInfo> filterInfos;

    public boolean isParamsExist(Type type) {
        return  type.isGetParamsExist();
    }

    @Getter
    @ToString
    static class Types {
        private final List<Type> typeList;

        Types(Map<String, Object> enpointMap, Endpoint parent) throws IllegalArgumentException {
            if (enpointMap.containsKey(TYPES) && enpointMap.containsKey(TYPE)) {
                throw new IllegalArgumentException("not use:" + TYPES + " and " + TYPE + " in one endpoint");
            }
            if (enpointMap.containsKey(TYPES)) {
                typeList = MakeCast.makeMapOfStringMap(enpointMap, TYPES, ENTITY, false)
                        .entrySet().stream().map(e -> new Type(e.getKey(), e.getValue(), parent)).toList();
                return;
            } else if (enpointMap.containsKey(TYPE)) {
                typeList = List.of(Type.makeType(enpointMap, parent));
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
        return filterInfos.stream().filter(FilterInfo::isVar).map(f -> ParameterSpec.builder(REQUEST_PARAMS, f.getVarName()).build()).toList();
    }

    List<ParameterSpec> getFilterControllerNameParameters() {
        return filterInfos.stream().filter(FilterInfo::isVar).map(f -> ParameterSpec.builder(REQUEST_PARAMS, f.getVarName())
                .addAnnotation(AnnotationSpec.builder(REQUEST_PARAM_ANNOTATION_CLASS)
                        .addMember("defaultValue", "$S", "{}").build())
                .addAnnotation(AnnotationSpec.builder(PARAMETER_ANNOTATION_CLASS)
                        .addMember("name", "$S", f.getVarName())
                        .addMember("example", "$S", f.getExample()).build())
                .build()).toList();
    }

    String getFilterCallDBParameters() {
        return filterInfos.stream().filter(FilterInfo::isVar).map(FilterInfo::getVarName).collect(Collectors.joining(","));
    }

    public MethodSpec makeDBMethod(String funcName, Type type) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(type.getRequestType().toString() + funcName)
                .addModifiers(Modifier.PUBLIC);
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
        return type.getInterpretDb().makeMethodBody(methodBuilder).build();
    }

    MethodSpec.Builder addParametersToControllerMethod(MethodSpec.Builder methodBuilder, Type type) {
        if (isParamsExist(type)) {
            methodBuilder.addParameter(ParameterSpec.builder(REQUEST_PARAMS, REQUEST_PARAM_NAME)
                    .addAnnotation(AnnotationSpec.builder(REQUEST_PARAM_ANNOTATION_CLASS)
                            .addMember("defaultValue", "$S", "{}").build())
                    .addAnnotation(AnnotationSpec.builder(PARAMETER_ANNOTATION_CLASS)
                            .addMember("name", "$S",REQUEST_PARAM_NAME )
                            .addMember("example", "$S", type.getExampleParams()).build())
                    .build());
        }
        for (VarInfo parameterSpec : varInfos) {
            methodBuilder.addParameter(parameterSpec.getAnnotationParameterSpec());
        }
        if (type.isParamsBodyExist()) {
            methodBuilder.addParameter(ParameterSpec.builder(PARAMETRIZED_MAP, REQUEST_PARAM_BODY)
                    .addAnnotation(AnnotationSpec.builder(REQUEST_BODY_ANNOTATION_CLASS)
                            .addMember("required", "true").build())
                    .addAnnotation(AnnotationSpec.builder(PARAMETER_ANNOTATION_CLASS)
                            .addMember("name", "$S",REQUEST_PARAM_BODY )
                            .addMember("example", "$S", type.getExampleEntity()).build())
                    .build());
        }
        for (var filter : getFilterControllerNameParameters()) {
            methodBuilder.addParameter(filter);
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
        String filterParams=getFilterCallDBParameters();
        if(!filterParams.isEmpty()) {
            params = addACommaIfBuilderIsNotEmpty(params).append(filterParams);
        }
        return params;
    }


    public MethodSpec makeControllerMethod(String funcName, Type type, String beanName) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(type.getRequestType().toString() + funcName)
                .addModifiers(Modifier.PUBLIC).addAnnotation(type.getMapping(request))
                .addAnnotation(type.getResponseStatus()).addAnnotation(type.getOperation());
        methodBuilder = addParametersToControllerMethod(methodBuilder, type);
        return addReturns(methodBuilder, type).addStatement(getReturnIfGet(type) + beanName + "." + type.getRequestType().toString() + funcName +
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
    }

    public List<MethodSpec> makeDBMethods(String funcName) {
        List<Type> typeList = this.types.getTypeList();
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (Type type : typeList) {
            methodSpecs.add(makeDBMethod(funcName, type));
        }
        return methodSpecs;
    }

    String getReturnIfGet(Type type) {
        if (type.getRequestType().equals(RequestType.GET)) {
            return "return ";
        }
        return "";
    }

    MethodSpec.Builder addReturns(MethodSpec.Builder builder, Type type) {
        if (type.getRequestType().equals(RequestType.GET)) {
            builder.returns(PARAMETERIZED_LIST);
        }
        return builder;
    }
}
