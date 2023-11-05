package org.example.analize.rewrite.address.premetive.field;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.premetive.table.BaseTable;

@Slf4j
public abstract class BaseField<Field, Table> implements Interpretation<Field> {
    @Getter
    String field = null;
    @Getter
    BaseTable<Table> table;
    protected boolean isVariable=false;
    @Override
    public String requestInterpret(){
        if(isVariable){
            return "{"+field+"}";
        }
        return Variables.make.makeVariableFromString(field);
    }

    protected BaseField(String field, BaseTable<Table> table, Variables variables) {
        Debug.debug(log, "BaseField field: ", field, " tableName: ", table.getTable());
        if (field.isEmpty()) {
            field = "Id";
        }
        this.table = table;
        this.field = makeField(field, variables);
        Debug.debug(log, "BaseField this.field:", this.field, " this.tableName: ", this.table.getTable());
    }

    protected abstract String makeField(String field, Variables variables);
}
