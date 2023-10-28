package org.example.analize.rewrite.comparison;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.premetive.variable.DSLVariable;
import org.jooq.Condition;
import org.jooq.Field;

public class DSLComparison extends BaseComparisonParser<Condition, Field,String>{
    DSLComparison(String request, Variables variables, String tableName) {
        super(request, variables, tableName);
    }

    @Override
    public Condition interpret() {
        return null;
    }

    @Override
    Interpretation<String> addValue(String value, Variables variables) {
        return new DSLVariable(value,variables);
    }

    @Override
    Interpretation<Field> addField(String value, Variables variables, String tableName, boolean isFromVariable) {
        return null;
    }
}
