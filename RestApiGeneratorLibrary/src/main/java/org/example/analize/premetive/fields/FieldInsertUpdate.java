package org.example.analize.premetive.fields;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public class FieldInsertUpdate extends BaseFieldInsertUpdate<CodeBlock,ClassName> {
    public FieldInsertUpdate(String name, String tableName, Endpoint parent) {
        super(name, tableName, parent);

    }

    @Override
    public ClassName getType() {
        return switch (type) {
            case LONG -> LONG_CLASS;
            case BOOLEAN -> BOOLEAN_CLASS;
            case INTEGER -> INTEGER_CLASS;
            case FLOAT -> FLOAT_CLASS;
            case DOUBLE -> DOUBLE_CLASS;
            default -> STRING_CLASS;
        };
    }

    @Override
    public CodeBlock interpret() {
        var block= CodeBlock.builder().add("$T.field($S)",DSL_CLASS,  realFieldName);
        return  block.build();
    }




    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
       //not use in request
    }

    @Override
    public CodeBlock makeNameInfo() {
        return CodeBlock.builder().add("$S, $S",name,realFieldName).build();
    }
}