package org.example.analize.rewrite.premetive.table;


import org.example.analize.rewrite.Variables;
import org.jooq.Table;
import org.jooq.impl.DSL;

public class DSLTable extends BaseTable<Table> {
    DSLTable(String table, Variables variables) {
        super(table, variables);
    }

    @Override
    public Table interpret() {
        return DSL.table(table);
    }

    @Override
    protected String makeTable(String table, Variables variables) {
        return  Variables.make.makeWriteVariable(table);
    }
}
