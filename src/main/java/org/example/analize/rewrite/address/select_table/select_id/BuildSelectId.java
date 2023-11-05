package org.example.analize.rewrite.address.select_table.select_id;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.premetive.field.BaseField;
import org.example.analize.rewrite.address.premetive.field.BuildField;
import org.example.analize.rewrite.address.premetive.field.in.BaseFieldIn;
import org.example.analize.rewrite.address.premetive.field.in.BuildFieldIn;
import org.example.analize.rewrite.address.premetive.table.BaseTable;
import org.example.analize.rewrite.address.premetive.table.BuildTable;


@Slf4j
public class BuildSelectId extends BaseSelectIdParser<String,String,String>{
    public BuildSelectId(String request, Variables variables) {
        super(request, variables);
        Debug.debug(log,"BaseSelectId request:",request);
    }

    @Override
    public String interpret() {
        Debug.debug(log,"interpret");
        return  Debug.debugResult(log,"interpret",
                "dslContext.select("+id.interpret()+").from("+table.interpret()+")");
    }

    @Override
    protected BaseField<String, String> addField(String field, Variables variables) {
        Debug.debug(log,"addField field:",field);
        return new BuildField(field,table,variables);
    }

    @Override
    protected BaseTable<String> addTable(String table, Variables variables) {
        Debug.debug(log,"addTable table:",table);
        return new BuildTable(table,variables);
    }

    @Override
    protected BaseFieldIn<String, String> addFieldIn(String port, Variables variables) {
        Debug.debug(log,"addFieldIn port:",port);
        return new BuildFieldIn(null,addField(port,variables));
    }
}
