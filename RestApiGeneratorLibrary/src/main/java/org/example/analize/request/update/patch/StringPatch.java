package org.example.analize.request.update.patch;
import com.squareup.javapoet.CodeBlock;
import org.example.analize.request.update.update.StringUpdate;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_MAP;

public class StringPatch extends StringUpdate {
    protected StringPatch(String request, List<String> fields, PortRequestWithCondition<CodeBlock, String> select, Endpoint parent) throws IllegalArgumentException {
        super(request, fields, select, parent);
    }

    @Override
    protected CodeBlock makeChooseFields() {
        var block= CodeBlock.builder().add(".set(Map.of(");
        if(fields.isEmpty()){
            return   block.add("))").build();
        }
        block.add(fields.stream().map(name-> CodeBlock.builder().add(name.getParams()+", "+
                        REQUEST_PARAM_MAP+".get($S)==null?"+name.getParams()+":DSL.val("+REQUEST_PARAM_MAP+".get($S))", name.getName(),name.getName()).build())
               .reduce((v,h)-> CodeBlock.builder().add(v).add(", ").add(h).build())
               .orElse(CodeBlock.builder().add(fields.get(0).interpret()).build()));
        block.add("))");
        return block.build();
    }
}
