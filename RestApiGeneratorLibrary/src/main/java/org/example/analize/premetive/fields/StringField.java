package org.example.analize.premetive.fields;

import com.squareup.javapoet.CodeBlock;
import org.example.read_json.rest_controller_json.Endpoint;

public class StringField extends BaseField<CodeBlock>{
    public StringField(String name, String tableName, Endpoint parent) {
        super(name, tableName, parent);
    }

    @Override
    public CodeBlock interpret() {
        var block= CodeBlock.builder().add("DSL.field($S)", tableName + "." + realFieldName);
        if(!realFieldName.equals(name)){
            block.add(".as($S)",name);
        }
        return  block.build();
    }
    String toString(String string) {
        return "\"" + string + "\"";
    }

    @Override
    public String requestInterpret() {
        return null;
    }

    @Override
    public String getParams() {
        return   "DSL.field(" + toString(tableName + "." + realFieldName) + ")";
    }
}
