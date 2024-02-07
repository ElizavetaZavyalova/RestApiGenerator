package org.example.analize.request.post;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;
import org.example.analize.request.post.insert.BaseInsertRequest;
import org.example.analize.request.post.insert.StringInsertRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;

public class StringPostRequest extends BasePostRequest<CodeBlock,String> {


    public StringPostRequest(String url, List<String> params, Endpoint parent) throws IllegalArgumentException {
        super(url, params, parent);
    }

    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder().add(insert.interpret()).add(".execute();").build();
    }

    @Override
    public String requestInterpret() {
        return null;
    }



    @Override
    protected BaseAddress<CodeBlock, String> make(String url, Endpoint parent) {
        return new StringAddress(url,parent);
    }

    @Override
    BaseInsertRequest<CodeBlock, String> makeBaseInsertRequest(String request, List<String> fields, PortRequestWithCondition<CodeBlock, String> select, Endpoint parent) {
        return new StringInsertRequest(request,fields,select,parent);
    }

}
