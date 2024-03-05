package org.example.analize.premetive.fields_cond;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public class FieldCondition extends BaseFieldCondition<CodeBlock> {
    public FieldCondition(String variable, String tableName, Endpoint parent) throws IllegalArgumentException {
        super(variable, tableName, parent);
    }

    CodeBlock codeBlockIn() {
        String code=".map($T::valueOf)";
        return switch (type) {
            case INTEGER -> CodeBlock.builder().add(code, INTEGER_CLASS).build();
            case LONG -> CodeBlock.builder().add(code, LONG_CLASS).build();
            case BOOLEAN -> CodeBlock.builder().add(code, BOOLEAN_CLASS).build();
            default -> CodeBlock.builder().build();
        };

    }


    @Override
    public CodeBlock interpret() {
        var block = CodeBlock.builder().add("$T.field($S)", DSL_CLASS, tableName + "." + realFieldName);
        switch (action) {
            case NE -> block.add(".ne(" + fieldName + ")");
            case LE -> block.add(".le(" + fieldName + ")");
            case LT -> block.add(".lt(" + fieldName + ")");
            case GE -> block.add(".ge(" + fieldName + ")");
            case GT -> block.add(".gt(" + fieldName + ")");
            case LIKE -> block.add(".like(" + fieldName + ")");
            case NOT_LIKE -> block.add(".not_like(" + fieldName + ")");
            default ->  block.add(".eq(" + fieldName + ")");
        }
        return block.build();
    }


    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        params.add(new VarInfo(type, this.fieldName, this.nameInRequest));
    }
}
