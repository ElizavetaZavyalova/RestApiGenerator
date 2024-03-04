package org.example.analize.request.post;

import com.squareup.javapoet.*;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;
import org.example.analize.premetive.fields.BaseFieldReal;
import org.example.analize.request.LoggerCreator;
import org.example.analize.request.post.insert.BaseInsertRequest;
import org.example.analize.request.post.insert.StringInsertRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.RequestType;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RESULT_NAME;
import static org.example.read_json.rest_controller_json.JsonKeyWords.ApplicationProperties.showSql;

public class StringPostRequest extends BasePostRequest<CodeBlock, MethodSpec.Builder, ClassName> {


    public StringPostRequest(String url, List<String> params, Endpoint parent) throws IllegalArgumentException {
        super(url, params, parent);
    }




    @Override
    protected BaseAddress<CodeBlock> make(String url, Endpoint parent) {
        return new StringAddress(url,parent);
    }

    @Override
    BaseInsertRequest<CodeBlock,ClassName> makeBaseInsertRequest(String request, List<String> fields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new StringInsertRequest(request,fields,select,parent);
    }

    @Override
    public MethodSpec.Builder makeMethodBody(MethodSpec.Builder method) {
        method.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + " = ")
                .add(insert.interpret()).build());
        method= LoggerCreator.createLog(method,RequestType.POST,RESULT_NAME);
        return method.addStatement(RESULT_NAME + ".execute()");
    }
}
