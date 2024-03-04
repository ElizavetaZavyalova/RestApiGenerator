package org.example.analize.request.delete;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.Address;
import org.example.analize.request.LoggerCreator;
import org.example.analize.request.delete.delete.Delete;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.RequestType;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RESULT_NAME;

public class DeleteRequest extends BaseDeleteRequest<CodeBlock, MethodSpec.Builder>{
    public DeleteRequest(String url, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
    }




    @Override
    protected BaseAddress<CodeBlock> make(String url, Endpoint parent) {
        return new Address(url,parent);
    }
    @Override
    protected PortRequestWithCondition<CodeBlock> makeDelete(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Delete(request,select,parent);
    }


    @Override
    public MethodSpec.Builder makeMethodBody(MethodSpec.Builder method) {
        method.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + " = ")
                .add(delete.interpret()).build());
        method= LoggerCreator.createLog(method,RequestType.DELETE,RESULT_NAME);
        return method.addStatement(RESULT_NAME + ".execute()");
    }
}
