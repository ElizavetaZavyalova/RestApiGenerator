package org.example.analize.premetive.filters;

import com.squareup.javapoet.CodeBlock;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.*;

public class BodyFuncFilterOneParam extends BaseBodyFuncFilter{
    String paramName="";
    public BodyFuncFilterOneParam(String variable) throws IllegalArgumentException {
        super(variable);

    }
    public BodyFuncFilterOneParam(String variable, Endpoint parent,String paramName) throws IllegalArgumentException {
        super(variable, parent);
        this.paramName=paramName;
    }
    @Override
    public String defaultValue(){
        return CodeBlock.builder().add("$S:",fieldName).build().toString();
    }

    @Override
    public CodeBlock interpret() {
        return makeCondition();
    }
    protected CodeBlock makeCondition() {
        CodeBlock.Builder builder = CodeBlock.builder().add(CONDITION_LIST_IN_FILTER + ".add($T.field($S).", DSL_CLASS, realFieldName);
        switch (action) {
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
                builder.add("notLike");
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
            default:{
                builder.add("eq");
                break;
            }
        }
        return builder.add("($T.val(",DSL_CLASS).add(REQUEST_PARAM_NAME + ".getFirst($S), $T.class)))",paramName,makeType()).build();
    }
}
