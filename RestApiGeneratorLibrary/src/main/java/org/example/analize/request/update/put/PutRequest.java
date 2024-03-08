package org.example.analize.request.update.put;

import com.squareup.javapoet.*;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.Address;
import org.example.analize.request.LoggerCreator;
import org.example.analize.request.update.BaseUpdateRequest;
import org.example.analize.request.update.update.BaseUpdate;
import org.example.analize.request.update.update.UpdateRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.RequestType;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.PARAMETERIZED_LIST;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RESULT_NAME;

public class PutRequest extends UpdateRequest {
    public PutRequest(String url, List<String> fields, List<String> returnFields, Endpoint parent) throws IllegalArgumentException {
        super(url, fields, returnFields, parent);
    }

    @Override
    protected BaseUpdate<CodeBlock, ClassName> makeUpdate(String request, List<String> fields, List<String> returnFields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Put(request, fields, returnFields, select, parent);
    }

}
