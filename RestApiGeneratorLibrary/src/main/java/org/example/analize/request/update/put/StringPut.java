package org.example.analize.request.update.put;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.request.update.update.StringUpdate;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;


import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.MAP_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_BODY;

public class StringPut extends StringUpdate {
    protected StringPut(String request, List<String> fields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) throws IllegalArgumentException {
        super(request, fields, select, parent);
    }

    @Override
    protected CodeBlock makeChooseFields() {
        var block= CodeBlock.builder().add(").set($T.of(", MAP_CLASS);
        if(fields.isEmpty()){
            return   block.add("))").build();
        }
        block.add(fields.stream()
                        .map(name->CodeBlock.builder()
                                .add(name.interpret()).add(", "+REQUEST_PARAM_BODY+".getParameter($S)!=null?($T.val("+REQUEST_PARAM_BODY+".getParameter($S))):$T.defaultValue()",name.getName(),DSL_CLASS,name.getName(),DSL_CLASS).build())
                .reduce((v,h)-> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(CodeBlock.builder().add(fields.get(0).interpret()).build()));
        block.add("))");
        return block.build();
    }
}
