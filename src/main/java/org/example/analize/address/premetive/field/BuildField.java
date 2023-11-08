package org.example.analize.address.premetive.field;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Variables;
import org.example.analize.address.premetive.table.BaseTable;

@Slf4j
public class BuildField extends BaseField<String,String>{
    public BuildField(String field, BaseTable<String> table, Variables variables) {
        super(field, table, variables);
        Debug.debug(log,"BuildField field:",field," table:",table.getTable());
    }

    @Override
    public String interpret() {
        return Debug.debugResult(log,"interpret",
                this.table.interpret()+".field("+field+")");
    }

    @Override
    protected String makeField(String field, Variables variables) {
        Debug.debug(log,"makeTable field:",field);
        Variables.make.TYPE type=Variables.make.TYPE.STRING;
        Debug.debug(log,"makeVariable TYPE:",type.getValue());
        if(Variables.make.isVariable(field)){
            this.isVariable=true;
            Debug.debug(log,"makeVariable isVariable==true");
            variables.add(Variables.make.makeVariable(field),type);
        }
        field=Variables.make.makeWriteVariable(field);
        Debug.debug(log,"makeVariable WriteVariable:",field);
        return field;
    }
}
