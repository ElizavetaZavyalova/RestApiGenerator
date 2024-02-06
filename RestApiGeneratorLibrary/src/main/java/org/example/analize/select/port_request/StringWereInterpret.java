package org.example.analize.select.port_request;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.where.BaseWhere;


public record StringWereInterpret() {
    public static CodeBlock makeWhere(BaseWhere< CodeBlock,String> where,PortRequestWithCondition<CodeBlock,String> selectNext,String tableName,String ref) {
        var block= CodeBlock.builder().add(".where(");
        if (where != null && selectNext != null) {
            return block.add("DSL.field($S)",tableName + "." + ref)
                    .add(".in(").add(selectNext.interpret()).add(").and(").add(where.interpret()).add("))").build();
        }
        if (where != null) {
            return block.add(where.interpret()).add(")").build();
        }
        if (selectNext != null) {
            return block.add("DSL.field($S)",tableName + "." + ref)
                    .add(".in(").add(selectNext.interpret()).add("))").build();

        }
        return CodeBlock.builder().build();
    }
}
