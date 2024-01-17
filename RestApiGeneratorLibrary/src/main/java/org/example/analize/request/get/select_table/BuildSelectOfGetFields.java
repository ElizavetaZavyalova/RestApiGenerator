package org.example.analize.request.get.select_table;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Interpretation;
import org.example.analize.Variables;
import org.example.analize.address.premetive.field.BaseField;
import org.example.analize.address.premetive.field.BuildField;
import org.example.analize.address.premetive.field.in.BaseFieldIn;
import org.example.analize.address.premetive.field.in.BuildFieldIn;
import org.example.analize.address.premetive.table.BaseTable;
import org.example.analize.address.premetive.table.BuildTable;

import java.util.stream.Collectors;

@Slf4j
public class BuildSelectOfGetFields extends BaseSelectOfGetFields<String,String,String>{
    public BuildSelectOfGetFields(String request, Variables variables) {
        super(request, variables);
        Debug.debug(log,"BuilderSelectOfGet request:",request);
    }

    @Override
    public String interpret() {
        Debug.debug(log,"interpret");
        String builderSelect = "dslContext.select(" + interpretFields() +
                ").from(" + table.interpret() + ")";
        return Debug.debugResult(log,"interpret", builderSelect);
    }

    private String interpretFields() {
        Debug.debug(log,"interpretFields");
        return Debug.debugResult(log,"interpretFields",
                fields.stream().map(Interpretation::interpret).collect(Collectors.joining(",")));
    }



    @Override
    protected BaseFieldIn<String, String> addFieldIn(String port, Variables variables) {
        Debug.debug(log,"addFieldIn port:",port);
        return new BuildFieldIn(null,addField(port,variables));
    }

    @Override
    protected BaseField<String,String> addField(String field, Variables variables) {
        Debug.debug(log,"addField field:",field);
        return new BuildField(field,table,variables);
    }

    @Override
    protected BaseTable<String> addTable(String table, Variables variables) {
        Debug.debug(log,"addTable table:",table);
        return new BuildTable(table,variables);
    }
}
