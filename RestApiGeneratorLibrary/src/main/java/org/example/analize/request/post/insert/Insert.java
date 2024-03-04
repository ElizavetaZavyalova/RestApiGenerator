package org.example.analize.request.post.insert;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.premetive.fields.BaseFieldInsertUpdate;
import org.example.analize.premetive.fields.FieldFieldInsertUpdate;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.Select;

import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.port_request.PortRequest;
import org.example.analize.select.port_request.WereInterpret;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;


import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.CONTEXT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.SELECT_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_BODY;


public class Insert extends BaseInsert<CodeBlock, ClassName> {

    public Insert(String request, List<String> fields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        super(request, fields, select, parent);
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makePortRequest(String tableName, PortRequestWithCondition<CodeBlock> select, Endpoint parent, boolean isPathFound) {
        return new PortRequest(tableName, select, parent, isPathFound);
    }


    @Override
    protected BaseFieldInsertUpdate<CodeBlock,ClassName> makeField(String name, String table, Endpoint parent) {
        return new FieldFieldInsertUpdate(name, table, parent);
    }

    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder().add(CONTEXT+".insertInto(").add(makeInsert()).add(")").add(makeValues()).build();
    }


    CodeBlock makeValues() {
        var block = CodeBlock.builder();
        if (isSelectExist()) {
            block.add(".select(($T)"+CONTEXT+".select(",SELECT_CLASS)
                    .add("$T.field($S)",DSL_CLASS,getSelectPort().getTableName()+"."+getSelectPort().getId());
            if(!fields.isEmpty()){
                block.add(", ").add(values());
            }
            block.add(").from($T.table($S)",DSL_CLASS,getSelectPort().getRealTableName());
            if(!getSelectPort().getTableName().equals(getSelectPort().getRealTableName())){
                block.add(".as($S)",getSelectPort().getTableName());
            }
            block.add(WereInterpret.makeWhere(getWherePort(),getAddress(),
                    getSelectPort().getTableName(),getSelectPort().getRef())).add("))");
            return block.build();
        }
        if (!fields.isEmpty()&&!isSelectExist()) {
            block.add(".values(").add(values()).add(")");
            return block.build();
        }
        return block.add(".defaultValues()").build();
    }
    CodeBlock makeValue(BaseFieldInsertUpdate<CodeBlock,ClassName> fieldReal){
        return CodeBlock.builder().add("$T.val("+REQUEST_PARAM_BODY+".containsKey($S)?"+REQUEST_PARAM_BODY+".get($S):"+fieldReal.getDefaultValue()+")",
                           DSL_CLASS,fieldReal.getName(),fieldReal.getName())
                    .build();
    }


    CodeBlock values() {
        return fields.stream()
                .map(this::makeValue).reduce((v, h) -> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(CodeBlock.builder().build());
    }

    CodeBlock makeInsert() {
        var block = CodeBlock.builder().add("$T.table($S)",DSL_CLASS, realTableName);
        if (isSelectExist()) {
            block.add(", $T.field($S)",DSL_CLASS,  ref);
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
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        if (isSelectExist()) {
            selectNext.addParams(params,filters);
        }
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Select(request, select, parent);
    }
}
