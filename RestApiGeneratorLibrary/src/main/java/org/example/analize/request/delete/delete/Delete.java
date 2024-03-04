package org.example.analize.request.delete.delete;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.Select;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.port_request.PortRequest;
import org.example.analize.select.port_request.WereInterpret;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.Where;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.CONTEXT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public class Delete extends BaseDelete<CodeBlock> {
    public Delete(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        super(request, select, parent);
    }
    @Override
    protected PortRequestWithCondition<CodeBlock> makePortRequest(String tableName, PortRequestWithCondition<CodeBlock> select, Endpoint parent, boolean isPathFound) {
        return new PortRequest(tableName, select, parent, isPathFound);
    }
    @Override
    public CodeBlock interpret() {
        var block = CodeBlock.builder();
        block.add(CONTEXT+".deleteFrom($T.table($S)",DSL_CLASS, realTableName);
        if (!realTableName.equals(tableName)) {
            block.add(".as($S)", tableName);
        }
        block.add(")");
        block.add(WereInterpret.makeWhere(where, selectNext, tableName, ref));
        return block.build();
    }


    @Override
    protected BaseWhere<CodeBlock> makeWhere(String request, String tableName, Endpoint parent) {
        return new Where(request, tableName, parent);
    }

    @Override
    public void addParams(List<VarInfo> params, List<FilterInfo> filters) {
        if (isSelectExist()) {
            selectNext.addParams(params,filters);
        }
        if (isWhereExist()) {
            where.addParams(params,filters);
        }
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Select(request,select,parent);
    }
}
