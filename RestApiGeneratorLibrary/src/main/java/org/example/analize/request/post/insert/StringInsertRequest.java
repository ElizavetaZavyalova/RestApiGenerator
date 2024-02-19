package org.example.analize.request.post.insert;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.StringFieldReal;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.StringSelect;

import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.port_request.StringPortRequest;
import org.example.analize.select.port_request.StringWereInterpret;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.CONTEXT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_NAME;


public class StringInsertRequest extends BaseInsertRequest<CodeBlock> {

    public StringInsertRequest(String request, List<String> fields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        super(request, fields, select, parent);

    }
    @Override
    protected PortRequestWithCondition<CodeBlock> makePortRequest(String tableName, PortRequestWithCondition<CodeBlock> select, Endpoint parent, boolean isPathFound) {
        return new StringPortRequest(tableName, select, parent, isPathFound);
    }


    @Override
    protected BaseField<CodeBlock> makeField(String name, String table, Endpoint parent) {
        return new StringFieldReal(name, table, parent);
    }

    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder().add(CONTEXT+".insertInto(").add(makeInsert()).add(")").add(makeValues()).build();
    }


    CodeBlock makeValues() {
        var block = CodeBlock.builder();
        if (isSelectExist()) {
            block.add(".select("+CONTEXT+".select(")
                    .add("$T.field($S)",DSL_CLASS,getSelectPort().getTableName()+"."+getSelectPort().getId());
            if(!values().isEmpty()){
                block.add(", ").add(values());
            }
            block.add(").from($T.table($S)",DSL_CLASS,getSelectPort().getRealTableName());
            if(!getSelectPort().getTableName().equals(getSelectPort().getRealTableName())){
                block.add(".as($S)",getSelectPort().getTableName());
            }
            block.add(StringWereInterpret.makeWhere(getWherePort(),getAddress(),
                    getSelectPort().getTableName(),getSelectPort().getRef())).add("))");
            return block.build();
        }
        if (!fields.isEmpty()) {
            block.add(".values(").add(values()).add(")");
            return block.build();
        }
        return block.add(".defaultValues()").build();
    }

    CodeBlock values() {
        return fields.stream().map(BaseField::getName)
                .map(name -> CodeBlock.builder().add("$T.val("+REQUEST_PARAM_NAME+".get($S))", DSL_CLASS,name)
                        .build()).reduce((v, h) -> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(CodeBlock.builder().build());
    }

    CodeBlock makeInsert() {
        var block = CodeBlock.builder().add("$T.table($S)",DSL_CLASS, realTableName);
        if (!realTableName.equals(tableName)) {
            block.add(".as($S)", tableName);
        }
        if (isSelectExist()) {
            block.add(", $T.field($S)",DSL_CLASS, tableName + "." + ref);
        }
        if (!fields.isEmpty()) {
            block.add(", ").add(makeFields());
        }

        return block.build();
    }

    CodeBlock makeFields() {
        return fields.stream().map(InterpretationBd::interpret)
                .reduce((v, h) -> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(CodeBlock.builder().build());
    }


    @Override
    public void addParams(List<VarInfo> params) {
        if (this.selectNext != null) {
            selectNext.addParams(params);
        }
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new StringSelect(request, select, parent);
    }
}
