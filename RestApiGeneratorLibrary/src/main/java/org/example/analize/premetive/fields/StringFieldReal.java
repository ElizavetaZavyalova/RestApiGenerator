package org.example.analize.premetive.fields;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public class StringFieldReal extends BaseField<CodeBlock>{
    public StringFieldReal(String name, String tableName, Endpoint parent) {
        super(name, tableName, parent);
    }

    @Override
    public CodeBlock interpret() {
        var block= CodeBlock.builder().add("DSL.field($S)", tableName + "." + realFieldName);
        return  block.build();
    }

    @Override
    public String requestInterpret() {
        return null;
    }


    @Override
    public void addParams(List<VarInfo> params) {

    }
}
