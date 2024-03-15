package org.example.analize.premetive.filters;


import com.squareup.javapoet.CodeBlock;

import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.*;

public class BodyFuncFilterManyParams extends BaseBodyFuncFilter {
    public BodyFuncFilterManyParams(String variable, Endpoint parent) throws IllegalArgumentException {
        super(variable, parent);
    }
    public BodyFuncFilterManyParams(String variable) throws IllegalArgumentException {
        super(variable);
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
    public String makeTypeOfDefaultValue(CodeBlock.Builder builder) {
        return switch (type) {
            case STRING -> builder.add("$S","String").build().toString();
            case BOOLEAN -> builder.add("true").build().toString();
            default -> builder.add("0").build().toString();
        };
    }
    @Override
    public String defaultValue(){
        return makeTypeOfDefaultValue(CodeBlock.builder().add("$S:",fieldName));
    }
    protected CodeBlock makeCondition() {
        CodeBlock.Builder builder = CodeBlock.builder().add(CONDITION_LIST_IN_FILTER + ".add($T.field($S).", DSL_CLASS,  realFieldName);
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
            case REG: {
                builder.add("likeRegex");
                break;
            }
            case NOT_REG: {
                builder.add("notLikeRegex");
                break;
            }
            case LIKE: {
                builder.add("like");
                break;
            }
            case NOT_LIKE: {
                builder.add("notLike");
                break;
            }
            default:{
                builder.add("eq");
                break;
            }
        }
        return builder.add("($T.val(",DSL_CLASS).add(REQUEST_PARAM_NAME + ".getFirst($S), $T.class)))",fieldName,makeType()).build();
    }
}
