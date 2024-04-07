package org.example.analize.request.update.update;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;


import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.premetive.fields.BaseFieldInsertUpdate;
import org.example.analize.premetive.fields.FieldFieldInsertUpdate;
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

public abstract class Update extends BaseUpdate<CodeBlock, ClassName> {
    protected Update(String request, List<String> fields, List<String> returnFields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) throws IllegalArgumentException {
        super(request, fields, returnFields, select, parent);
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makePortRequest(String tableName, PortRequestWithCondition<CodeBlock> select, Endpoint parent, boolean isPathFound) {
        return new PortRequest(tableName, select, parent, isPathFound);
    }
    CodeBlock makeReturnFields() {
        return returnFields.stream().map(InterpretationBd::interpret)
                .reduce((v, h) -> CodeBlock.builder().add(v).add(", ").add(h).build()).orElse(CodeBlock.builder().build());
    }

    CodeBlock makeReturnPort() {
        if (isReturnSomething()) {
            var block = CodeBlock.builder().add(".returning(").add(makeReturnFields()).add(")");
            return block.build();
        }
        return CodeBlock.builder().build();
    }
    CodeBlock interpretNoRefs(){
        var block = CodeBlock.builder().add(CONTEXT + ".update($T.table($S)", DSL_CLASS, realTableName);
        if (!realTableName.equals(tableName)) {
            block.add(".as($S)", tableName);
        }
        block.add(makeChooseFields());
        block.add(WereInterpret.makeWhere(where, selectNext, tableName, ref));
        return block.add(makeReturnPort()).build();
    }
    CodeBlock interpretRefs(){
        var block = CodeBlock.builder().add(CONTEXT + ".update($T.table($S)", DSL_CLASS, realTableName);
        if (!realTableName.equals(tableName)) {
            block.add(".as($S)", tableName);
        }
        block.add(makeChooseFields());
        block.add(WereInterpret.makeWhere(where, selectNext, tableName, ref));
        return block.add(makeReturnPort()).build();
    }

    @Override
    public CodeBlock interpret() {
      if(!isRefs()){
          interpretNoRefs();
      }
      return null;
    }

    protected abstract CodeBlock makeChooseFields();


    @Override
    protected BaseFieldInsertUpdate<CodeBlock,ClassName> makeField(String name, String table, Endpoint parent) {
        return new FieldFieldInsertUpdate(name, table, parent);
    }

    @Override
    public void addParams(List<VarInfo> params, List<FilterInfo> filters) {
        if (isWhereExist()) {
            where.addParams(params, filters);
        }
        if (isSelectExist()) {
            selectNext.addParams(params, filters);
        }
    }

    @Override
    protected BaseWhere<CodeBlock> makeWhere(String request, String tableName, Endpoint parent) {
        return new Where(request, tableName, parent);
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Select(request, select, parent);
    }
}
