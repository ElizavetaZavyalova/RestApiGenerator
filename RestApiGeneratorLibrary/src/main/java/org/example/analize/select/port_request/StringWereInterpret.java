package org.example.analize.select.port_request;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.where.BaseWhere;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;


public record StringWereInterpret() {
    public static CodeBlock makeWhere(BaseWhere<CodeBlock> where, PortRequestWithCondition<CodeBlock> selectNext, String tableName, String ref) {
        var block = CodeBlock.builder().add(".where(");
        if (where != null && selectNext != null) {
            return block.add("$T.and($T.field($S)",DSL_CLASS,DSL_CLASS, tableName + "." + ref)
                    .add(".in(").add(selectNext.interpret()).add("), (").add(where.interpret()).add(")))").build();
        }
        if (where != null) {
            return block.add(where.interpret()).add(")").build();
        }
        if (selectNext != null) {
            return block.add("$T.field($S)",DSL_CLASS, tableName + "." + ref)
                    .add(".in(").add(selectNext.interpret()).add("))").build();

        }
        return CodeBlock.builder().build();
    }
}
