package org.example.analize.request.get;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.LoggerCreator;
import org.example.analize.request.get.select.StringGetSelect;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.RequestType;
import org.example.read_json.rest_controller_json.endpoint.Type;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RESULT_NAME;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.RESULT_OF_RECORD_CLASS;
import static org.example.read_json.rest_controller_json.JsonKeyWords.ApplicationProperties.showSql;

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
        method.returns(RESULT_OF_RECORD_CLASS);
        method.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + " = ")
                .add(select.interpret()).build());
        method= LoggerCreator.createLog(method,RequestType.GET,RESULT_NAME);
        return  method.addStatement("return " + RESULT_NAME + ".fetch()");
    }
}
