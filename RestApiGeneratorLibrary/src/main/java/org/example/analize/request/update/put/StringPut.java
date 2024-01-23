package org.example.analize.request.update.put;

import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.request.update.update.StringUpdate;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_MAP;

public class StringPut extends StringUpdate {
    protected StringPut(String request, List<String> fields, PortRequestWithCondition<String, String> select, Endpoint parent) throws IllegalArgumentException {
        super(request, fields, select, parent);
    }

    @Override
    protected String makeChooseFields() {
        StringBuilder builder=new StringBuilder(".set(Map.of(");
        builder.append(fields.stream().map(name->"\n"+name.getParams()+", "+"DSL.val(Optional.ofNullable("+REQUEST_PARAM_MAP+".get(" + toString(name.getName()) + ")).orElse(DSL.defaultValue()))")
                .collect(Collectors.joining(", ")));
        builder.append("))\n");
        return builder.toString();
    }
}
