package org.example.analize.request.get.select;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.StringField;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.port_request.StringWereInterpret;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.StringWhere;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;


public class StringGetSelect extends GetBaseSelect<CodeBlock,String>{
    public StringGetSelect(String request, PortRequestWithCondition<CodeBlock, String> select, List<String> fields, Endpoint parent) {
        super(request, select, fields, parent);
    }

    @Override
    protected BaseField<CodeBlock> makeField(String name, String table, Endpoint parent) {
        return  new StringField(name,table,parent);
    }

    @Override
    public CodeBlock interpret() {
        var block= CodeBlock.builder();
                block.add("context.select(")
                .add(makeFields())
                .add(").from($S)",realTableName);
        if(!realTableName.equals(tableName)){
            block.add(".as($S)",tableName);
        }
        block.add(")");
        block.add(StringWereInterpret.makeWhere(where,selectNext,tableName,ref));
        return block.build();
    }



    CodeBlock makeFields(){
        if(fields==null||fields.isEmpty()){
            return CodeBlock.builder().build();
        }
        return fields.stream().map(InterpretationBd::interpret)
                .reduce((v,h)-> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(fields.get(0).interpret()).toBuilder().build();
    }



    @Override
    protected BaseWhere<CodeBlock,String> makeWhere(String request, String tableName, Endpoint parent) {
        return new StringWhere(request, tableName, parent);
    }

    @Override
    public void addParams(List<VarInfo> params) {
           if(this.where!=null){
               where.addParams(params);
           }
        if(this.selectNext!=null){
            selectNext.addParams(params);
        }
    }
}
