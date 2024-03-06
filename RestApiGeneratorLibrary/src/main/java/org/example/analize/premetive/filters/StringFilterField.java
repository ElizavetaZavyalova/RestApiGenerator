package org.example.analize.premetive.filters;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.BaseFieldParser;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.jooq.impl.DSL;

import java.util.Arrays;
import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.STRING_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.*;

public class StringFilterField extends BaseFieldParser<CodeBlock> {
    public StringFilterField(String variable, Endpoint parent) throws IllegalArgumentException {
        super(variable, parent);
    }
    public StringFilterField(String variable) throws IllegalArgumentException {
        super(variable);
    }
    public ClassName makeType() {
        return switch (type) {
            case STRING -> STRING_CLASS;
            case BOOLEAN -> BOOLEAN_CLASS;
            case INTEGER -> INTEGER_CLASS;
            default -> LONG_CLASS;
        };
    }
    public String makeTypeOfDefaultValue(CodeBlock.Builder builder) {
        return switch (type) {
            case STRING -> builder.add("$S","String").build().toString();
            case BOOLEAN -> builder.add("true").build().toString();
            default -> builder.add("0").build().toString();
        };
    }
    public String defaultValue(){
        return makeTypeOfDefaultValue(CodeBlock.builder().add("$S:",fieldName));
    }

    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder()
                .beginControlFlow("if (" + REQUEST_PARAM_NAME + ".containsKey($S))", fieldName)
                .addStatement(makeCondition())
                .endControlFlow()
                .build();
    }
    CodeBlock makeArray(){
        return  CodeBlock.builder().add(REQUEST_PARAM_NAME+".get($S).stream().map(t-> $T.val(t, $T.class))",fieldName,DSL_CLASS,makeType()).build();
    }

    protected CodeBlock makeCondition() {
        CodeBlock.Builder builder = CodeBlock.builder().add(CONDITION_LIST_IN_FILTER + ".add($T.field(" + TABLE_NAME_IN_FILTER + "+$S).", DSL_CLASS, "." + realFieldName);
        switch (action) {
            case IN:{
                builder.add("in");
                return builder.add("(").add(makeArray()).add(")").build();
            }
            case NOT_IN:{
                builder.add("notIn");
                return builder.add("(").add(makeArray()).add(")").build();
            }
            case NE: {
                builder.add("ne");
                break;
            }
            case LE: {
                builder.add("le");
                break;
            }
            case LT: {
                builder.add("lt");
                break;
            }
            case GE: {
                builder.add("ge");
                break;
            }
            case GT: {
                builder.add("gt");
                break;
            }
            case LIKE: {
                builder.add("like");
                break;
            }
            case NOT_LIKE: {
                builder.add("not_like");
                break;
            }
            default:{
                builder.add("eq");
                break;
            }
        }
        return builder.add("($T.val(",DSL_CLASS).add(REQUEST_PARAM_NAME + ".getFirst($S), $T.class)))",fieldName,makeType()).build();
    }


    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
          //not in request
    }
}
