package org.example.analize.request.get;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;

import org.example.analize.request.LoggerCreator;
import org.example.analize.request.get.select.StringGetSelect;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.RequestType;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RESULT_NAME;

public class StringGetRequest extends BaseGetRequest<CodeBlock,MethodSpec.Builder>{
    public StringGetRequest(String url, List<String> fields, Endpoint parent) throws IllegalArgumentException {
        super(url, fields, parent);
    }
    @Override
    protected BaseAddress<CodeBlock> make(String url, Endpoint parent) {
        return new StringAddress(url,parent);
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, List<String> fields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new StringGetSelect(request,select,fields,parent);
    }

    @Override
    public MethodSpec.Builder makeMethodBody(MethodSpec.Builder method) {
        method.returns(PARAMETERIZED_LIST);
        method.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + " = ")
                .add(select.interpret()).build());
        method= LoggerCreator.createLog(method,RequestType.GET,RESULT_NAME);
        method.addStatement( "$T<$T<$T, $T>> "+RESULT_LIST+" = new $T<>()",LIST_CLASS,MAP_CLASS,STRING_CLASS,OBJECT_CLASS,ARRAY_LIST_CLASS);
        method.beginControlFlow(RESULT_NAME+".fetch().forEach(r -> ")
                .addStatement("$T<$T, $T> "+RESULT_MAP+" = new $T<>()",MAP_CLASS,STRING_CLASS,OBJECT_CLASS,HASH_MAP_CLASS)
                .addStatement("$T.stream(r.fields()).forEach(field -> "+RESULT_MAP+".put(field.getName(), r.getValue(field)))",ARRAYS_CLASS)
                .addStatement(RESULT_LIST+".add("+RESULT_MAP+")")
                .endControlFlow().addStatement(")");
        return  method.addStatement("return " + RESULT_LIST);
    }
}
