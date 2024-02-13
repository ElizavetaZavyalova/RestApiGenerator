package org.example.analize.select;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.port_request.StringWereInterpret;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.StringWhere;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.CONTEXT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public class StringSelect extends PortRequestWithCondition<CodeBlock> {
    public StringSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        super(request, select, parent);
    }

    @Override
    public CodeBlock interpret() {
        var block = CodeBlock.builder();
        block.add(CONTEXT + ".select(").add(makeField("$T.field($S)"))
                .add(".from($S)", realTableName);
        if (!realTableName.equals(tableName)) {
            block.add(".as($S)", tableName);
        }
        block.add(")");
        block.add(StringWereInterpret.makeWhere(where, selectNext, tableName, ref));
        return block.build();//DSL_CLASS
    }

    CodeBlock makeField(String choseField) {
        CodeBlock.Builder block= CodeBlock.builder();
        switch (aggregationFunction) {
            case MAX -> {
                return block.add("$T.max(" + choseField + ")",DSL_CLASS,DSL_CLASS, tableName + "." + id).build();
            }
            case MIN -> {
                return block.add("$T.min(" + choseField + ")",DSL_CLASS,DSL_CLASS, tableName + "." + id).build();
            }
            default -> {
                return block.add(choseField,DSL_CLASS, tableName + "." + id).build();
            }
        }
    }


    @Override
    protected BaseWhere<CodeBlock> makeWhere(String request, String tableName, Endpoint parent) {
        return new StringWhere(request, tableName, parent);
    }

    public void addParams(List<VarInfo> params) {
        if (this.selectNext != null) {
            selectNext.addParams(params);
        }
        if (this.where != null) {
            where.addParams(params);
        }
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new StringSelect(request, select, parent);
    }
}
