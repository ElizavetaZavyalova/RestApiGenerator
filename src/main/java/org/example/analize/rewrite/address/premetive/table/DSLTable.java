package org.example.analize.rewrite.address.premetive.table;


import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Variables;
import org.jooq.Table;
import org.jooq.impl.DSL;

@Slf4j
public class DSLTable extends BaseTable<Table> {
    public DSLTable(String table, Variables variables) {
        super(table, variables);
        Debug.debug(log, "DSLTable table:", table);
    }

    @Override
    public Table interpret() {
        Debug.debug(log, "interpret:", table);
        return DSL.table(table);
    }

    @Override
    protected String makeTable(String table, Variables variables) {
        Debug.debug(log, "makeTable:", table);
        return Variables.make.makeWriteVariable(table);
    }
}
