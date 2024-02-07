package org.example.analize.request.update.patch;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.StringAddress;
import org.example.analize.premetive.fieldsCond.StringFieldCondition;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.get.select.StringGetSelect;
import org.example.analize.request.update.BaseUpdateRequest;
import org.example.analize.request.update.update.BaseUpdate;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;

public class StringPatchRequest extends BaseUpdateRequest<CodeBlock,String> {
    public StringPatchRequest(String url, List<String> fields, Endpoint parent) throws IllegalArgumentException {
        super(url, fields, parent);
    }

    @Override
    protected BaseAddress<CodeBlock, String> make(String url, Endpoint parent) {
        return new StringAddress(url,parent);
    }

    @Override
    protected BaseUpdate<CodeBlock,String> makeUpdate(String request, List<String> fields, PortRequestWithCondition<CodeBlock,String> select, Endpoint parent) {
        return new StringPatch(request, fields,select,parent);
    }

}
