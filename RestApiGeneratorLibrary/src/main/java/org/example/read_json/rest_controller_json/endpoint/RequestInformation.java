package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.InterpretDb;
import org.example.read_json.rest_controller_json.MakeCast;


import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.RESULT_OF_RECORD_CLASS;
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

    @Getter
    @ToString
    static class Types {
        private final List<Type> typeList;

        Types(Map<String, Object> enpointMap, Endpoint parent) throws IllegalArgumentException {
            if (enpointMap.containsKey(TYPES) && enpointMap.containsKey(TYPE)) {
                throw new IllegalArgumentException("NOT USE:" + TYPES + " and " + TYPE + " in one endpoint");
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
            type.setInterpretDb(new InterpretDb(endpoint, type));
        }
        varInfos = new ArrayList<>();
    }

    public MethodSpec makeDBMethod(String funcName, Type type) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(type.getRequestType().toString() + funcName)
                .addModifiers(Modifier.PUBLIC);
        methodBuilder.addParameter(REQUEST_PARAMS, REQUEST_PARAM_NAME);
        for (VarInfo parameterSpec : varInfos) {
            if (!parameterSpec.isFilter()) {
                methodBuilder.addParameter(parameterSpec.getParameterSpec());
            }
        }
        return addCode(addReturns(methodBuilder, type), type).build();
    }

    public MethodSpec makeControllerMethod(String funcName, Type type, String beanName) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(type.getRequestType().toString() + funcName)
                .addModifiers(Modifier.PUBLIC).addAnnotation(type.getMapping(request))
                .addAnnotation(type.getResponseStatus()).addAnnotation(type.getOperation());
        StringBuilder params = new StringBuilder().append(REQUEST_PARAM_NAME);
        for (VarInfo parameterSpec : varInfos) {
            if (!parameterSpec.isFilter()) {
                methodBuilder.addParameter(parameterSpec.getAnnotationParameterSpec());
                params.append(", ").append(parameterSpec.getName());
            }
        }
        methodBuilder.addParameter(ParameterSpec.builder(REQUEST_PARAMS, REQUEST_PARAM_NAME)
                .addAnnotation(AnnotationSpec.builder(REQUEST_PARAM_ANNOTATION_CLASS).build()).build());
        return addReturns(methodBuilder, type).addStatement(getReturnIfGet(type) + beanName + "." + type.getRequestType().toString() + funcName +
                "(" + params + ")").build();
    }

    public List<MethodSpec> makeControllerMethods(String funcName, String beanName) {
        List<Type> typeList = this.types.getTypeList();
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (Type type : typeList) {
            methodSpecs.add(makeControllerMethod(funcName, type, beanName));
        }
        return methodSpecs;
    }

    public List<MethodSpec> makeDBMethods(String funcName) {
        List<Type> typeList = this.types.getTypeList();
        typeList.get(0).getInterpretDb().getInterpretation().addParams(varInfos);
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (Type type : typeList) {
            methodSpecs.add(makeDBMethod(funcName, type));
        }
        return methodSpecs;
    }


    MethodSpec.Builder addCode(MethodSpec.Builder builder, Type type) {
        builder.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + " = ")
                .add(type.getInterpretDb().getInterpretation().interpret()).build());
        builder.beginControlFlow("if (" + SHOW_SQL_NAME + ")")
                .addStatement(LOG_NAME + ".log($T." + LOG_LEVE_NAME + ", $S+" + RESULT_NAME + ".getSQL()+$S)", LOG_LEVEL, "\n", "\n")
                .endControlFlow();
        if (type.getRequestType().equals(RequestType.GET)) {
            return builder.addStatement("return " + RESULT_NAME + ".fetch()");
        }
        return builder.addStatement(RESULT_NAME + ".execute()");
    }

    String getReturnIfGet(Type type) {
        if (type.getRequestType().equals(RequestType.GET)) {
            return "return ";
        }
        return "";
    }

    MethodSpec.Builder addReturns(MethodSpec.Builder builder, Type type) {
        if (type.getRequestType().equals(RequestType.GET)) {
            builder.returns(RESULT_OF_RECORD_CLASS);
        }
        return builder;
    }
}
