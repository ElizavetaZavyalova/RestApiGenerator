package org.example.analize.address;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.StringSelect;
import org.example.read_json.rest_controller_json.Endpoint;

public class StringAddress extends BaseAddress<CodeBlock,String>{
    public StringAddress(String url, Endpoint parent) {
        super(url, parent);
    }

    @Override
    PortRequestWithCondition<CodeBlock,String> makeSelect(String request, PortRequestWithCondition<CodeBlock,String> select, Endpoint parent) {
        return new StringSelect(request,select,parent);
    }

    @Override
    public CodeBlock interpret() {
        if(selectCurrent!=null){
            return selectCurrent.interpret();
        }
        return CodeBlock.builder().build();
    }

    @Override
    public String requestInterpret() {
        if(selectCurrent!=null){
            return selectCurrent.requestInterpret();
        }
        return "";
    }

    @Override
    public String getParams() {
        return null;
    }
}
