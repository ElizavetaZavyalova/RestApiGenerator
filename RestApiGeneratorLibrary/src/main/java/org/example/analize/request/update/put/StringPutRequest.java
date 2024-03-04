package org.example.analize.request.update.put;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;
import org.example.analize.premetive.fields.BaseFieldReal;
import org.example.analize.request.LoggerCreator;
import org.example.analize.request.update.BaseUpdateRequest;
import org.example.analize.request.update.update.BaseUpdate;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.RequestType;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RESULT_NAME;
import static org.example.read_json.rest_controller_json.JsonKeyWords.ApplicationProperties.showSql;

public class StringPutRequest extends BaseUpdateRequest<CodeBlock, MethodSpec.Builder, ClassName> {
    public StringPutRequest(String url, List<String> fields, Endpoint parent) throws IllegalArgumentException {
        super(url, fields, parent);
    }


    @Override
    protected BaseAddress<CodeBlock> make(String url, Endpoint parent) {
        return new StringAddress(url,parent);
    }

    @Override
    protected BaseUpdate<CodeBlock,ClassName> makeUpdate(String request, List<String> fields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new StringPut(request, fields,select,parent);
    }

    @Override
    public MethodSpec.Builder makeMethodBody(MethodSpec.Builder method) {
        method.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + " = ")
                .add(update.interpret()).build());
        method= LoggerCreator.createLog(method,RequestType.PUT,RESULT_NAME);
        return method.addStatement(RESULT_NAME + ".execute()");
    }
}
