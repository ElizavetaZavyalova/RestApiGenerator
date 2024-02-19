package org.example.analize.premetive.fields_cond;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.BOOLEAN_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.INTEGER_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public class StringFieldCondition extends BaseFieldCondition<CodeBlock> {
    public StringFieldCondition(String variable, String tableName, Endpoint parent) throws IllegalArgumentException {
        super(variable, tableName, parent);
    }

    CodeBlock codeBlockIn() {
        return switch (type) {
            case INTEGER -> CodeBlock.builder().add(".map($T::valueOf)", INTEGER_CLASS).build();
            case BOOLEAN -> CodeBlock.builder().add(".map($T::valueOf)", BOOLEAN_CLASS).build();
            default -> CodeBlock.builder().build();
        };

    }


    @Override
    public CodeBlock interpret() {
        var block = CodeBlock.builder().add("$T.field($S)", DSL_CLASS, tableName + "." + realFieldName);
        switch (action) {
            case EQ -> block.add(".eq(" + fieldName + ")");
            case NE -> block.add(".ne(" + fieldName + ")");
            case LE -> block.add(".le(" + fieldName + ")");
            case LT -> block.add(".lt(" + fieldName + ")");
            case GE -> block.add(".ge(" + fieldName + ")");
            case GT -> block.add(".gt(" + fieldName + ")");
            case LIKE -> block.add(".like(" + fieldName + ")");
            case NOT_LIKE -> block.add(".not_like(" + fieldName + ")");
            case IN -> block.add(".in(Arrays.stream(" + fieldName + ".split(", ")").add(codeBlockIn()).add("))");
        }
        return block.build();
    }
    Type createTypeByAction(){
        if(action.equals(Action.IN)){
            return Type.STRING;
        }
        return type;
    }

    public void addParams(List<VarInfo> params) {
        params.add(new VarInfo(createTypeByAction(), this.fieldName, this.nameInRequest));
    }
}
