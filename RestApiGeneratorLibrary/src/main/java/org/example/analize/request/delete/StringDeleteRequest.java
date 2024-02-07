package org.example.analize.request.delete;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;
import org.example.analize.request.delete.delete.StringDelete;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;

public class StringDeleteRequest extends BaseDeleteRequest<CodeBlock,String>{
    public StringDeleteRequest(String url, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
    }

    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder().add(delete.interpret()).add(".execute();").build();
    }

    @Override
    public String requestInterpret() {
        return delete.requestInterpret();
    }



    @Override
    protected BaseAddress<CodeBlock, String> make(String url, Endpoint parent) {
        return new StringAddress(url,parent);
    }
    @Override
    protected PortRequestWithCondition<CodeBlock, String> makeDelete(String request, PortRequestWithCondition<CodeBlock,String> select, Endpoint parent) {
        return new StringDelete(request,select,parent);
    }


}
