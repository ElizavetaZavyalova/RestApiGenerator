package org.example.analize.rewrite.address.premetive.table;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Variables;

@Slf4j
public class BuildTable extends BaseTable<String>{
    public BuildTable(String table, Variables variables) {
        super(table, variables);
    }

    @Override
    public String interpret() {
        return Debug.debugResult(log,"interpret", "DSL.table(" + table + ")");
    }

    @Override
    protected String makeTable(String table, Variables variables) {
        Debug.debug(log,"makeTable table:",table);
        Variables.make.TYPE type=Variables.make.TYPE.STRING;
        Debug.debug(log,"makeVariable TYPE:",type.getValue());
        if(Variables.make.isVariable(table)){
            this.isVariable=true;
            Debug.debug(log,"makeVariable isVariable==true");
            variables.add(Variables.make.makeVariable(table),type);
        }
        table=Variables.make.makeWriteVariable(table);
       Debug.debug(log,"makeVariable WriteVariable:",table);
        return table;
    }
}
