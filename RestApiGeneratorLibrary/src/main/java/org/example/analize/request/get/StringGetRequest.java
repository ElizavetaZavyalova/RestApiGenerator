package org.example.analize.request.get;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;
import org.example.analize.request.get.select.StringGetSelect;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import java.util.List;

public class StringGetRequest extends BaseGetRequest<CodeBlock>{
    public StringGetRequest(String url, List<String> fields, Endpoint parent) throws IllegalArgumentException {
        super(url, fields, parent);
    }

    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder().add(select.interpret()).add(".fetch();").build();
    }




    @Override
    protected BaseAddress<CodeBlock> make(String url, Endpoint parent) {
        return new StringAddress(url,parent);
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, List<String> fields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new StringGetSelect(request,select,fields,parent);
    }
}
