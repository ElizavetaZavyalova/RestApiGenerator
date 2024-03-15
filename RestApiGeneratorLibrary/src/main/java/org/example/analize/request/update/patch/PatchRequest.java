package org.example.analize.request.update.patch;

import com.squareup.javapoet.*;

import org.example.analize.request.update.update.BaseUpdate;
import org.example.analize.request.update.update.UpdateRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;


import java.util.List;

public class PatchRequest extends UpdateRequest {
    public PatchRequest(String url, List<String> fields,List<String> returnFields, Endpoint parent) throws IllegalArgumentException {
        super(url, fields,returnFields, parent);
    }

    @Override
    protected BaseUpdate<CodeBlock,ClassName> makeUpdate(String request, List<String> fields,List<String> returnFields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Patch(request, fields,returnFields,select,parent);
    }
}
