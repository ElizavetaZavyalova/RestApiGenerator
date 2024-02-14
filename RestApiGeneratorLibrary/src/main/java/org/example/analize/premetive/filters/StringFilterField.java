package org.example.analize.premetive.filters;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.BaseFieldParser;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.jooq.impl.DSL;


import java.util.Arrays;
import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.*;

public class StringFilterField extends BaseFieldParser<CodeBlock> {
    public StringFilterField(String variable, Endpoint parent) throws IllegalArgumentException {
        super(variable, parent);
    }

    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder()
                .beginControlFlow("if (" + REQUEST_PARAM_NAME + ".containsKey($S))", fieldName)
                .addStatement(makeCondition(),DSL_CLASS, "." + realFieldName, fieldName)
                .endControlFlow()
                .build();
    }

    protected String makeCondition() {
        StringBuilder builder = new StringBuilder(CONDITION_LIST_IN_FILTER + ".add($T.field(" + TABLE_NAME_IN_FILTER + "+$S).");
        switch (action) {
            case EQ -> builder.append("eq");
            case NE -> builder.append("ne");
            case LE -> builder.append("le");
            case LT -> builder.append("lt");
            case GE -> builder.append("ge");
            case GT -> builder.append("gt");
            case LIKE -> builder.append("like");
            case NOT_LIKE -> builder.append("not_like");
            case IN -> builder.append("in");
        }
        return builder.append("(").append(REQUEST_PARAM_NAME + ".get($S)))").toString();
    }


    @Override
    public void addParams(List<VarInfo> params) {}
}
