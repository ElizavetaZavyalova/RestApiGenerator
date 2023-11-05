package org.example.analize.rewrite.address.premetive.table;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;

@Slf4j
public abstract class BaseTable<TableName> implements Interpretation<TableName> {
    @Getter
    String table = null;
    protected boolean isVariable=false;

    BaseTable(String table, Variables variables) {
        Debug.debug(log, "BaseField table:", table);
        this.table = makeTable(table, variables);
        Debug.debug(log, "BaseField this.table: ", this.table);
    }
    @Override
    public String requestInterpret(){
        if(isVariable){
            return "{"+table+"}";
        }
        return Variables.make.makeVariableFromString(table);
    }

    protected abstract String makeTable(String table, Variables variables);
}
