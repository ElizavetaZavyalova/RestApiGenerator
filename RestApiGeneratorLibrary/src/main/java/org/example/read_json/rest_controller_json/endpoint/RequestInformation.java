package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
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

import static org.example.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_MAP;
import static org.example.file_code_gen.DefaultsVariablesName.Filter.RESULT_OF_RECORD_CLASS;

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

        Types(Map<String, Object> enpointMap) throws IllegalArgumentException {
            List<Type> list = MakeCast.makeMapOfStringMap(enpointMap, TYPES, ENTITY, false)
                    .entrySet().stream().map(e -> new Type(e.getKey(), e.getValue())).toList();
            if (list.isEmpty()) {
                list = List.of(new Type(_GET, new HashMap<>()));
            }
            typeList = list;
        }
    }

    RequestInformation(Map<String, Object> enpointMap) throws IllegalArgumentException {
        types = new Types(enpointMap);
        request = MakeCast.makeStringIfContainsKeyMap(enpointMap, REQUEST, true).replace(" ", "");
    }

    public void generateBd(Endpoint endpoint) {
        List<Type> types = this.types.getTypeList();
        for (Type type : types) {
            type.setInterpretDb(new InterpretDb(endpoint, type));
        }
        varInfos = new ArrayList<>();
    }

    public MethodSpec makeDBMethod(String funcName, Type type) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(type.getRequestType().toString() + funcName)
                .addModifiers(Modifier.PUBLIC);
        for (VarInfo parameterSpec : varInfos) {
            if (!parameterSpec.isFilter()) {
                methodBuilder.addParameter(parameterSpec.getParameterSpec());
            }
        }
        methodBuilder.addParameter(ParameterizedTypeName.get(Map.class, String.class, Object.class), REQUEST_PARAM_MAP);
        return addCode(addReturns(methodBuilder, type), type).build();
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
        if (type.getRequestType().equals(RequestType.GET)) {
            builder.addCode("return ");
        }
        return builder.addCode(type.getInterpretDb().getInterpretation().interpret());
    }

    MethodSpec.Builder addReturns(MethodSpec.Builder builder, Type type) {
        if (type.getRequestType().equals(RequestType.GET)) {
            builder.returns(RESULT_OF_RECORD_CLASS);
        }
        return builder;
    }


}
