package org.example.analize.rewrite.address.select_table.select_id;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.premetive.field.BaseField;
import org.example.analize.rewrite.address.premetive.field.DSLField;
import org.example.analize.rewrite.address.premetive.field.in.BaseFieldIn;
import org.example.analize.rewrite.address.premetive.field.in.DSLFieldIn;
import org.example.analize.rewrite.address.premetive.table.BaseTable;
import org.example.analize.rewrite.address.premetive.table.DSLTable;
import org.jooq.*;
import org.jooq.impl.DSL;

@Slf4j
public class DSLSelectId extends BaseSelectIdParser<Select, Table, Field> {
    static DSLContext dslContext= DSL.using(SQLDialect.POSTGRES);
    public DSLSelectId(String request, Variables variables) {
        super(request, variables);
        Debug.debug(log,"DSLSelectId request:",request);
    }



    @Override
    public Select interpret() {
        Debug.debug(log,"interpret");
        return  dslContext.select(id.interpret()).from(table.interpret());
    }
    @Override
    protected BaseFieldIn<Field, Table> addFieldIn(String port, Variables variables) {
        Debug.debug(log,"addFieldIn port:",port);
        return new DSLFieldIn(null,addField(port,variables));
    }

    @Override
    protected BaseField<Field,Table> addField(String field, Variables variables) {
        Debug.debug(log,"addField field:",field);
        return new DSLField(field,table,variables);
    }

    @Override
    protected BaseTable<Table> addTable(String table, Variables variables) {
        Debug.debug(log,"addTable table:",table);
        return new DSLTable(table,variables);
    }
}
