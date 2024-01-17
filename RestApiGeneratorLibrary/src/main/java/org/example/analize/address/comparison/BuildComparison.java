package org.example.analize.address.comparison;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Interpretation;
import org.example.analize.Variables;
import org.example.analize.address.premetive.field.BuildField;
import org.example.analize.address.premetive.table.BaseTable;
import org.example.analize.address.premetive.variable.BuildVariable;

@Slf4j
public class BuildComparison extends BaseComparisonParser<String,String,String,String> {
    public BuildComparison(String request, Variables variables, BaseTable<String> table) {
        super(request, variables, table);
        Debug.debug(log, "BuildComparison request:", request);
    }

    @Override
    public String interpret() {
        Debug.debug(log, "interpret:", operation.getValue());
        StringBuilder result=new StringBuilder().append(field.interpret());
        switch (operation) {
            case LIKE -> {
                result.append(".like(");
            }
            case NOT_LIKE -> {
                result.append(".notLike(");

            }
            case NOT_EQUAL -> {
                 result.append(".notEqual(");

            }
            case LESS_THEN -> {
                result.append(".lessThan(");

            }
            case GREATER_THEN -> {
                result.append(".greaterThan(");

            }
            case GREATER_OR_EQUAL -> {
                result.append(".greaterOrEqual(");

            }
            case LESS_OR_EQUAL -> {
                result.append(".lessOrEqual(");

            }
            default /*EQ*/-> {
                result.append(".eq(");
            }
        }
        result.append(value.interpret()).append(")");
        return  Debug.debugResult(log,"interpret",result.toString());
    }
    @Override
    Interpretation<String> addValue(String value, Variables variables) {
        Debug.debug(log, "addValue value:", value);
        return new BuildVariable(value, variables);
    }

    @Override
    Interpretation<String> addField(String value, Variables variables, BaseTable<String> table, boolean isFromVariable) {
        Debug.debug(log, "addField: field", value, " table:", table.getTable());
        if (isFromVariable) {
            Debug.debug(log, "addField:", "FromVariable");
            value = Variables.make.makeVariable(value);
        }
        return new BuildField(value, table, variables);
    }
}
