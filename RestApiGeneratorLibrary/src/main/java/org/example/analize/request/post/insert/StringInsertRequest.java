package org.example.analize.request.post.insert;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.interpretation.InterpretationParams;
import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.StringField;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_MAP;

public class StringInsertRequest extends BaseInsertRequest<CodeBlock,String>{

    public StringInsertRequest(String request, List<String> fields, PortRequestWithCondition<CodeBlock, String> select, Endpoint parent) {
        super(request, fields,select, parent);

    }


    @Override
    protected BaseField<CodeBlock> makeField(String name, String table, Endpoint parent) {
        return  new StringField(name,table,parent);
    }

    @Override
    public CodeBlock interpret() {
       return  CodeBlock.builder().add("context.insertInto(").add(makeInsert()).add(")").add(makeValues()).build();
    }
    CodeBlock  makeValues(){
        var block=CodeBlock.builder();
        if (selectNext != null) {
            block.add("DSL.select(");
            block.add("DSL.field($S)",tableName + "." + id);
            if(!fields.isEmpty()) {
                block.add(",\n ").add(values());

            }
            block.add(").from(").add(selectNext.interpret()).add(")");
            return block.build();
        }
        if(!fields.isEmpty()) {
            block.add(".values(").add(values()).add(")");
            return block.build();
        }
        return block.add(".defaultValues()").build();
    }
    CodeBlock values(){
    return  fields.stream().map(BaseField::getName)
                .map(name ->CodeBlock.builder().add("DSL.val(Optional.ofNullable("+REQUEST_PARAM_MAP+".get($S)).orElse(DSL.defaultValue()))",name)
            .build()).reduce((v,h)-> CodeBlock.builder().add(v).add(", ").add(h).build())
                        .orElse(CodeBlock.builder().build());
    }
    CodeBlock makeInsert(){
        var block=CodeBlock.builder().add("DSL.table($S)",realTableName);
        if(!realTableName.equals(tableName)){
            block.add(".as($S)",tableName);
        }
        if (selectNext != null) {
            block.add(", DSL.field($S)",tableName + "." + ref);
        }
        if(!fields.isEmpty()){
            block.add(", ").add(makeFields());
        }

        return block.build();
    }

    String makeFields(){
        //TODO fields
        return fields.stream().map(InterpretationParams::getParams).collect(Collectors.joining(", "));
    }
    @Override
    public String requestInterpret() {
        return null;
    }

    @Override
    public String getParams() {
        return null;
    }
}
