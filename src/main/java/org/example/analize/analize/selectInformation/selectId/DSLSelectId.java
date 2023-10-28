package org.example.analize.analize.selectInformation.selectId;

import org.example.analize.analize.BaseVariables;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;

public class DSLSelectId extends BaseSelectIdParser<Select> {
    DSLContext dslContext;

    public DSLSelectId(String request, BaseVariables variables, DSLContext dslContext) {
        super(request, variables);
        this.dslContext = dslContext;
    }

    @Override
    public Select interpret() {
        if (maxMin == MAX_MIN.getMAX()) {
            return dslContext.select(DSL.max(DSL.field(id))).from(table);
        } else if (maxMin == MAX_MIN.getMIN()) {
            return dslContext.select(DSL.min(DSL.field(id))).from(table);
        } else return dslContext.select(DSL.field(id)).from(table);
    }


    @Override
    protected String addField(String field, BaseVariables variables) {
        return BaseVariables.make.makeWriteVariable(field);
    }

    @Override
    protected String addTable(String table, BaseVariables variables) {
        return BaseVariables.make.makeWriteVariable(table);
    }


}
