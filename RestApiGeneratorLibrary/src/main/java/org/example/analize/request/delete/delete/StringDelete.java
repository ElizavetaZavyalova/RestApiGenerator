package org.example.analize.request.delete.delete;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.StringSelect;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.port_request.StringWereInterpret;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.StringWhere;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public class StringDelete extends BaseDelete<CodeBlock, String> {
    public StringDelete(String request, PortRequestWithCondition<CodeBlock, String> select, Endpoint parent) {
        super(request, select, parent);
    }

    @Override
    public CodeBlock interpret() {
        var block = CodeBlock.builder();
        block.add("context.deleteFrom(DSL.table($S)", realTableName);
        if (!realTableName.equals(tableName)) {
            block.add(".as($S)", tableName);
        }
        block.add(")");
        block.add(StringWereInterpret.makeWhere(where, selectNext, tableName, ref));
        return block.build();
    }


    @Override
    protected BaseWhere<CodeBlock, String> makeWhere(String request, String tableName, Endpoint parent) {
        return new StringWhere(request, tableName, parent);
    }

    @Override
    public void addParams(List<VarInfo> params) {
        if (this.selectNext != null) {
            selectNext.addParams(params);
        }
        if (this.where != null) {
            where.addParams(params);
        }
    }

    @Override
    protected PortRequestWithCondition<CodeBlock, String> makeSelect(String request, PortRequestWithCondition<CodeBlock, String> select, Endpoint parent) {
        return new StringSelect(request,select,parent);
    }
}
