package org.example.analize.request.delete;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;
import org.example.analize.request.delete.delete.StringDelete;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

public class StringDeleteRequest extends BaseDeleteRequest<CodeBlock,String>{
    protected StringDeleteRequest(String url, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
    }

    @Override
    public CodeBlock interpret() {
        return delete.interpret();
    }

    @Override
    public String requestInterpret() {
        return delete.requestInterpret();
    }

    @Override
    public String getParams() {
        return null;
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
