package org.example.analize.premetive.fields;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.jooq.impl.DSL;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public class StringField extends BaseField<CodeBlock>{
    public StringField(String name, String tableName, Endpoint parent) {
        super(name, tableName, parent);
    }

    @Override
    public CodeBlock interpret() {
        var block= CodeBlock.builder().add("$T.field($S)",DSL_CLASS, tableName + "." + realFieldName);
       // if(!realFieldName.equals(name)){
            block.add(".as($S)",name);
       // }
        return  block.build();
    }



    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        //Not use in request
    }
}
