package org.example.analize.address;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.Select;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public class Address extends BaseAddress<CodeBlock> {
    public Address(String url, Endpoint parent) {
        super(url, parent);
    }

    @Override
    PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Select(request, select, parent);
    }

    @Override
    public CodeBlock interpret() {
        if (isSelectCurrentExist()) {
            return selectCurrent.interpret();
        }
        return CodeBlock.builder().build();
    }


    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        if (isSelectCurrentExist()) {
            selectCurrent.addParams(params,filters);
        }
    }
}
