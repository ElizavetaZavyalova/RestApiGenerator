package org.example.analize.rewrite.condition;

import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.jooq.Condition;

public class DSLCondition extends BaseConditionParser<Condition>{
    DSLCondition(String request, Variables variables, String tableName) {
        super(request, variables, tableName);
    }

    @Override
    public Condition interpret() {
        return null;
    }

    @Override
    Interpretation<Condition> makeComparison(String request, Variables variables, String tableName) {
        return null;
    }

    @Override
    Interpretation<Condition> makeConditionNext(String request, Variables variables, String tableName) {
        return null;
    }
}
