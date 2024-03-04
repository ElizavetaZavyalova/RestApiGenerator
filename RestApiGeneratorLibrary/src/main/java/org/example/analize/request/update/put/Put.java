package org.example.analize.request.update.put;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.fields.BaseFieldInsertUpdate;
import org.example.analize.request.update.update.Update;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;


import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.MAP_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_BODY;

public class Put extends Update {
    protected Put(String request, List<String> fields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) throws IllegalArgumentException {
        super(request, fields, select, parent);
    }
    CodeBlock makeValue(BaseFieldInsertUpdate<CodeBlock, ClassName> fieldReal){
        if(!fieldReal.isTypeString()){
            return CodeBlock.builder().add(REQUEST_PARAM_BODY+".containsKey($S)?($T.val("+REQUEST_PARAM_BODY+".getFirst($S), $T.class)):$T.val("+fieldReal.getDefaultValue()+", $T.class)",
                            fieldReal.getName(),DSL_CLASS,fieldReal.getName(),fieldReal.getType(),DSL_CLASS,fieldReal.getType())
                    .build();
        }
        return CodeBlock.builder().add(REQUEST_PARAM_BODY+".containsKey($S)?"+REQUEST_PARAM_BODY+".getFirst($S):$T.val("+fieldReal.getDefaultValue()+", $T.class)",
                        fieldReal.getName(),fieldReal.getName(),DSL_CLASS,fieldReal.getType())
                .build();
    }

    @Override
    protected CodeBlock makeChooseFields() {
        var block= CodeBlock.builder().add(").set($T.of(", MAP_CLASS);
        if(fields.isEmpty()){
            return   block.add("))").build();
        }
        block.add(fields.stream()
                        .map(name->CodeBlock.builder()
                                .add(name.interpret()).add(", ").add(makeValue(name)).build())
                .reduce((v,h)-> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(CodeBlock.builder().add(fields.get(0).interpret()).build()));
        block.add("))");
        return block.build();
    }
}
