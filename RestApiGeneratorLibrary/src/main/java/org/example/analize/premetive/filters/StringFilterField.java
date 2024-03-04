package org.example.analize.premetive.filters;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.BaseFieldParser;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.STRING_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.*;

public class StringFilterField extends BaseFieldParser<CodeBlock> {
    public StringFilterField(String variable, Endpoint parent) throws IllegalArgumentException {
        super(variable, parent);
    }
    public ClassName makeType() {
        return switch (type) {
            case STRING -> STRING_CLASS;
            case BOOLEAN -> BOOLEAN_CLASS;
            case INTEGER -> INTEGER_CLASS;
            default -> LONG_CLASS;
        };
    }

    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder()
                .beginControlFlow("if (" + REQUEST_PARAM_NAME + ".containsKey($S))", fieldName)
                .addStatement(makeCondition(), DSL_CLASS, "." + realFieldName,DSL_CLASS, fieldName,makeType())
                .endControlFlow()
                .build();
    }

    protected String makeCondition() {
        StringBuilder builder = new StringBuilder(CONDITION_LIST_IN_FILTER + ".add($T.field(" + TABLE_NAME_IN_FILTER + "+$S).");
        switch (action) {
            case NE: {
                builder.append("ne");
                break;
            }
            case LE: {
                builder.append("le");
                break;
            }
            case LT: {
                builder.append("lt");
                break;
            }
            case GE: {
                builder.append("ge");
                break;
            }
            case GT: {
                builder.append("gt");
                break;
            }
            case LIKE: {
                builder.append("like");
                break;
            }
            case NOT_LIKE: {
                builder.append("not_like");
                break;
            }
            case IN: {
                builder.append("in");
                break;
            }
            default:{
                builder.append("eq");
                break;
            }
        }
        return builder.append("($T.val(").append(REQUEST_PARAM_NAME + ".getFirst($S), $T.class)))").toString();
    }


    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
          //not in request
    }
}
