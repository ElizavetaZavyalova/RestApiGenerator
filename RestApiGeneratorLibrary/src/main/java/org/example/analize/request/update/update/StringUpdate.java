package org.example.analize.request.update.update;

import com.squareup.javapoet.CodeBlock;

import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.StringFieldReal;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.StringSelect;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.port_request.StringPortRequest;
import org.example.analize.select.port_request.StringWereInterpret;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.StringWhere;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.CONTEXT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public abstract class StringUpdate extends BaseUpdate<CodeBlock>{
    protected StringUpdate(String request, List<String> fields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) throws IllegalArgumentException {
        super(request, fields, select, parent);
    }
    @Override
    protected PortRequestWithCondition<CodeBlock> makePortRequest(String tableName, PortRequestWithCondition<CodeBlock> select, Endpoint parent, boolean isPathFound) {
        return new StringPortRequest(tableName, select, parent, isPathFound);
    }

    @Override
    public CodeBlock interpret() {
        var block=CodeBlock.builder().add(CONTEXT+".update($T.table($S)",DSL_CLASS,realTableName);
        if(!realTableName.equals(tableName)){
            block.add(".as($S)",tableName);
        }
        block.add(makeChooseFields());
        block.add(StringWereInterpret.makeWhere(where,selectNext,tableName,ref));
        return block.build();
    }

    protected abstract CodeBlock makeChooseFields();


    @Override
    protected BaseField<CodeBlock> makeField(String name, String table, Endpoint parent) {
        return  new StringFieldReal(name,table,parent);
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

    @Override
    protected BaseWhere<CodeBlock> makeWhere(String request, String tableName, Endpoint parent) {
        return new StringWhere(request, tableName, parent);
    }
    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new StringSelect(request,select,parent);
    }
}
