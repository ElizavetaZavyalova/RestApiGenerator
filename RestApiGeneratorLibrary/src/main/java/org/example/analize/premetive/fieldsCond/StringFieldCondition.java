package org.example.analize.premetive.fieldsCond;

import com.squareup.javapoet.CodeBlock;
import org.example.read_json.rest_controller_json.Endpoint;


public class StringFieldCondition extends BaseFieldCondition<CodeBlock> {
    public StringFieldCondition(String variable, String tableName, Endpoint parent) throws IllegalArgumentException {
        super(variable, tableName, parent);
    }

    @Override
    public CodeBlock interpret() {
       var block=CodeBlock.builder().add("DSL.field($S)",tableName + "." + realFieldName);
        switch (action) {
            case EQ -> block.add(".eq("+fieldName+")");
            case NE -> block.add(".ne("+fieldName+")");
            case LE -> block.add(".le("+fieldName+")");
            case LT -> block.add(".lt("+fieldName+")");
            case GE -> block.add(".ge("+fieldName+")");
            case GT -> block.add(".gt("+fieldName+")");
            case LIKE -> block.add("like("+fieldName+")");
            case NOT_LIKE -> block.add("not_like("+fieldName+")");
        }
        return block.build();
    }


    @Override
    public String getParams() {
        return "";
    }
}
