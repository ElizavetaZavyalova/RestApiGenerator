package org.example.analize.rewrite.premetive.field;

import org.example.analize.rewrite.Variables;
import org.jooq.Field;
import org.jooq.impl.DSL;

public class DSLField extends BaseField<Field>{
    DSLField(String field, String tableName, Variables variables) {
        super(field, tableName, variables);
    }

    @Override
    public Field interpret() {
        return DSL.table(tableName).field(field);
    }

    @Override
    protected String makeField(String field, Variables variables) {
        return  Variables.make.makeWriteVariable(field);
    }
}
