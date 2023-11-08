package org.example.analize.address.comparison;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Interpretation;
import org.example.analize.Variables;
import org.example.analize.address.premetive.field.DSLField;
import org.example.analize.address.premetive.table.BaseTable;
import org.example.analize.address.premetive.variable.DSLVariable;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Table;

@Slf4j
public class DSLComparison extends BaseComparisonParser<Condition, Table, Field, String> {
    public DSLComparison(String request, Variables variables, BaseTable<Table> table) {
        super(request, variables, table);
        Debug.debug(log, "DSLComparison request", request);
    }

    @Override
    public Condition interpret() {
        Debug.debug(log, "interpret:", operation.getValue());
        switch (operation) {
            case LIKE -> {
                return field.interpret().like(value.interpret());
            }
            case NOT_LIKE -> {
                return field.interpret().notLike(value.interpret());
            }
            case NOT_EQUAL -> {
                return field.interpret().notEqual(value.interpret());
            }
            case LESS_THEN -> {
                return field.interpret().lessThan(value.interpret());
            }
            case GREATER_THEN -> {
                return field.interpret().greaterThan(value.interpret());
            }
            case GREATER_OR_EQUAL -> {
                return field.interpret().greaterOrEqual(value.interpret());
            }
            case LESS_OR_EQUAL -> {
                return field.interpret().lessOrEqual(value.interpret());
            }
        }
        return field.interpret().eq(value.interpret());
    }

    @Override
    Interpretation<String> addValue(String value, Variables variables) {
        Debug.debug(log, "addValue value:", value);
        return new DSLVariable(Variables.make.makeVariable(value), variables);
    }

    @Override
    Interpretation<Field> addField(String value, Variables variables, BaseTable<Table> table, boolean isFromVariable) {
        Debug.debug(log, "addField: field", value, " table:", table.getTable());
        if (isFromVariable) {
            Debug.debug(log, "addField:", "FromVariable");
            value = Variables.make.makeVariable(value);
        }
        return new DSLField(value, table, variables);
    }
}
