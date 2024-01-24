package org.example.analize.select;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.port_request.StringWereInterpret;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.StringWhere;
import org.example.read_json.rest_controller_json.Endpoint;

public class StringSelect extends PortRequestWithCondition<CodeBlock,String> {
    public StringSelect(String request, PortRequestWithCondition<CodeBlock,String> select, Endpoint parent) {
        super(request, select, parent);
    }

    @Override
    public CodeBlock interpret() {
        var block=CodeBlock.builder();
                block.add("context.select(DSL.field($S)",tableName + "." + id)
                        .add(".from($S)",realTableName);
        if(!realTableName.equals(tableName)){
            block.add(".as($S)",tableName);
        }
        block.add(")");
        block.add(StringWereInterpret.makeWhere(where,selectNext,tableName,ref));
        return block.build();
    }


    @Override
    public String getParams() {
        return null;
    }

    @Override
    protected BaseWhere<CodeBlock,String> makeWhere(String request, String tableName, Endpoint parent) {
        return new StringWhere(request, tableName, parent);
    }
}