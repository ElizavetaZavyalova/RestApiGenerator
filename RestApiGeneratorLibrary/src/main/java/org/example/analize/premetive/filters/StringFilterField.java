package org.example.analize.premetive.filters;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.BaseFieldParser;
import org.example.read_json.rest_controller_json.Endpoint;


import static org.example.file_code_gen.DefaultsVariablesName.Filter.*;

public class StringFilterField extends BaseFieldParser<CodeBlock> {
    public StringFilterField(String variable, Endpoint parent) throws IllegalArgumentException {
        super(variable, parent);
    }

    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder()
                .beginControlFlow("if (" + REQUEST_PARAM_MAP_IN_FILTER + ".containsKey($S))", fieldName)
                .addStatement(makeCondition(), realFieldName, fieldName)
                .endControlFlow()
                .build();
    }

    String toString(String string) {
        return "\"" + string + "\"";
    }

    String makeCondition() {
        StringBuilder builder = new StringBuilder(CONDITION_LIST_IN_FILTER + ".add(DSL.field(" + TABLE_NAME_IN_FILTER + ".$S).");
        switch (action) {
            case EQ -> builder.append("eq");
            case NE -> builder.append("ne");
            case LE -> builder.append("le");
            case LT -> builder.append("lt");
            case GE -> builder.append("ge");
            case GT -> builder.append("gt");
            case LIKE -> builder.append("like");
            case NOT_LIKE -> builder.append("not_like");
        }
        return builder.append("(").append(REQUEST_PARAM_MAP_IN_FILTER + ".get($S)))").toString();
    }

    @Override
    public String getParams() {
        return null;
    }

    @Override
    public String requestInterpret() {
        return null;
    }
}
