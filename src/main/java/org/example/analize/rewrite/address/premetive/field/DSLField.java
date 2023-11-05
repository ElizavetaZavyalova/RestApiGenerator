package org.example.analize.rewrite.address.premetive.field;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.premetive.table.BaseTable;
import org.jooq.Field;
import org.jooq.Table;

@Slf4j
public class DSLField extends BaseField<Field, Table> {
    public DSLField(String field, BaseTable<Table> table, Variables variables) {
        super(field, table, variables);
        Debug.debug(log, "DSLField field", field, " table:", table.getTable());
    }

    @Override
    public Field interpret() {
        Debug.debug(log, "interpret:", field);
        return table.interpret().field(field);
    }

    @Override
    protected String makeField(String field, Variables variables) {
        Debug.debug(log, "makeField:", field);
        return Variables.make.makeWriteVariable(field);
    }
}
