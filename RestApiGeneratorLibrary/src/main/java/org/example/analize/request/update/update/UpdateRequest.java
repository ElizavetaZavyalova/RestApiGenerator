package org.example.analize.request.update.update;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import org.example.analize.address.Address;
import org.example.analize.address.BaseAddress;
import org.example.analize.request.LoggerCreator;
import org.example.analize.request.update.BaseUpdateRequest;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.RequestType;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;


public abstract class UpdateRequest extends BaseUpdateRequest<CodeBlock, MethodSpec.Builder, TypeName,ClassName> {
    protected UpdateRequest(String url, List<String> fields, List<String> returnFields, Endpoint parent) throws IllegalArgumentException {
        super(url, fields,returnFields, parent);
    }
    @Override
    protected BaseAddress<CodeBlock> make(String url, Endpoint parent) {
        return new Address(url,parent);
    }

    @Override
    public MethodSpec.Builder makeMethodBody(MethodSpec.Builder method) {
        method.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + " = ")
                .add(update.interpret()).build());
        method= LoggerCreator.createLog(method, RequestType.PUT,RESULT_NAME);
        return makeMethodReturn(method);
    }
    public MethodSpec.Builder makeMethodReturn(MethodSpec.Builder method) {
        if(update.isReturnSomething()){
            return  method.addStatement("return " + RESULT_NAME+".fetch().intoMaps()");
        }
        return method.addStatement(RESULT_NAME + ".execute()");
    }
    @Override
    public TypeName returnParam() {
        if(update.isReturnSomething()){
            return PARAMETERIZED_LIST;
        }
        return TypeName.VOID;
    }
}
