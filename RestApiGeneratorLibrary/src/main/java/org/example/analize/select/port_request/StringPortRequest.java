package org.example.analize.select.port_request;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.StringSelect;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.StringWhere;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.CONTEXT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public class StringPortRequest extends PortRequestWithCondition<CodeBlock> {
    @Override
    public CodeBlock interpret() {
        var block = CodeBlock.builder();
        block.add(CONTEXT + ".select(").add("$T.field($S)", DSL_CLASS, tableName + "." + id)
                .add(").from($T.table($S)",DSL_CLASS, realTableName);
        if (!realTableName.equals(tableName)) {
            block.add(".as($S)", tableName);
        }
        block.add(")");
        block.add(StringWereInterpret.makeWhere(null, selectNext, tableName, ref));
        return block.build();
    }

    public StringPortRequest(String tableName, PortRequestWithCondition<CodeBlock> select, Endpoint parent, boolean isPathFound) {
    super(tableName,select,parent,isPathFound);
    }

    @Override
    protected BaseWhere<CodeBlock> makeWhere(String request, String tableName, Endpoint parent) {
        return new StringWhere(request, tableName, parent);
    }

    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        if (isSelectExist()){
            selectNext.addParams(params,filters);
        }
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makePortRequest(String tableName, PortRequestWithCondition<CodeBlock> select, Endpoint parent, boolean isPathFound) {
        return new StringPortRequest(tableName, select, parent, isPathFound);
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new StringSelect(request, select, parent);
    }
}
