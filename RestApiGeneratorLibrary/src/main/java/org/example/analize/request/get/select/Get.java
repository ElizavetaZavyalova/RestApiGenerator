package org.example.analize.request.get.select;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.Field;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.Select;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.port_request.PortRequest;
import org.example.analize.select.port_request.WereInterpret;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.Where;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.CONTEXT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;


public class Get extends BaseGet<CodeBlock> {
    public Get(String request, PortRequestWithCondition<CodeBlock> select, List<String> fields, Endpoint parent) {
        super(request, select, fields, parent);
    }
    @Override
    protected PortRequestWithCondition<CodeBlock> makePortRequest(String tableName, PortRequestWithCondition<CodeBlock> select, Endpoint parent, boolean isPathFound) {
        return new PortRequest(tableName, select, parent, isPathFound);
    }

    @Override
    protected BaseField<CodeBlock> makeField(String name, String table, Endpoint parent) {
        return  new Field(name,table,parent);
    }

    @Override
    public CodeBlock interpret() {
        var block= CodeBlock.builder();
                block.add(CONTEXT+".select(")
                .add(makeFields())
                .add(").from($T.table($S)",DSL_CLASS, realTableName);
        if (!realTableName.equals(tableName)) {
            block.add(".as($S)", tableName);
        }
        block.add(")");
        block.add(WereInterpret.makeWhere(where,selectNext,tableName,ref));
        return block.build();
    }
    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Select(request,select,parent);
    }




    CodeBlock makeFields(){
        if(!isFieldsExist()){
            return CodeBlock.builder().build();
        }
        return fields.stream().map(InterpretationBd::interpret)
                .reduce((v,h)-> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(fields.get(0).interpret()).toBuilder().build();
    }



    @Override
    protected BaseWhere<CodeBlock> makeWhere(String request, String tableName, Endpoint parent) {
        return new Where(request, tableName, parent);
    }

    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        if (isSelectExist()) {
            selectNext.addParams(params,filters);
        }
        if (isWhereExist()) {
            where.addParams(params,filters);
        }
    }
}
