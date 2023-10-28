package org.example.analize.analize.compison;

import org.example.analize.analize.BaseVariables;
import org.jooq.Condition;
import org.jooq.impl.DSL;

public class DSLComparison extends BaseComparisonParser<Condition> {
    public DSLComparison(String request, BaseVariables variables,String tableName) {
        super(request, variables,tableName);
    }

    @Override
    public Condition interpret() {
        switch (operation){
            case LIKE -> {
                return DSL.field(field).like(value);
            }
            case NOT_LIKE -> {
                return DSL.field(field).notLike(value);
            }
            case NOT_EQUAL -> {
                return DSL.field(field).notEqual(value);
            }
            case LESS_THEN -> {
                return DSL.field(field).lessThan(value);
            }
            case GREATER_THEN -> {
                return DSL.field(field).greaterThan(value);
            }
            case GREATER_OR_EQUAL -> {
                return DSL.field(field).greaterOrEqual(value);
            }
            case LESS_OR_EQUAL -> {
                return DSL.field(field).lessOrEqual(value);
            }
        }
        return DSL.field(field).eq(value);
    }

    @Override
    String addValue(String value, BaseVariables baseVariables) {
        return BaseVariables.make.makeWriteVariable(value);
    }

    @Override
    String addField(String value, BaseVariables baseVariables, String tableName,boolean isFromVariable) {
        return BaseVariables.make.makeWriteVariable(value);
    }
}
