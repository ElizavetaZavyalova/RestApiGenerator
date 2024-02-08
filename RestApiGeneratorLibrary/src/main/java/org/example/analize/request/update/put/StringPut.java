package org.example.analize.request.update.put;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.request.update.update.StringUpdate;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_MAP;

public class StringPut extends StringUpdate {
    protected StringPut(String request, List<String> fields, PortRequestWithCondition<CodeBlock, String> select, Endpoint parent) throws IllegalArgumentException {
        super(request, fields, select, parent);
    }

    @Override
    protected CodeBlock makeChooseFields() {
        var block= CodeBlock.builder().add(".set(Map.of(");
        if(fields.isEmpty()){
            return   block.add("))").build();
        }
        block.add(fields.stream()
                        .map(name->CodeBlock.builder()
                                .add(name.interpret()+", "+"DSL.val(Optional.ofNullable("+REQUEST_PARAM_MAP+".get($S)).orElse(DSL.defaultValue()))",name.getName()).build())
                .reduce((v,h)-> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(CodeBlock.builder().add(fields.get(0).interpret()).build()));
        block.add("))");
        return block.build();
    }
}
