package org.example.analize.address;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.StringSelect;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public class StringAddress extends BaseAddress<CodeBlock> {
    public StringAddress(String url, Endpoint parent) {
        super(url, parent);
    }

    @Override
    PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new StringSelect(request, select, parent);
    }

    @Override
    public CodeBlock interpret() {
        if (selectCurrent != null) {
            return selectCurrent.interpret();
        }
        return CodeBlock.builder().build();
    }


    @Override
    public void addParams(List<VarInfo> params) {
        if (this.selectCurrent != null) {
            selectCurrent.addParams(params);
        }
    }
}
