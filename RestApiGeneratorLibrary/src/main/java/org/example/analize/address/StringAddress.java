package org.example.analize.address;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import org.example.analize.premetive.fieldsCond.StringFieldCondition;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.StringSelect;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;

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
    public void addParams(List<VarInfo> params) {
            if(this.selectCurrent!=null){
                selectCurrent.addParams(params);
            }
    }
}
