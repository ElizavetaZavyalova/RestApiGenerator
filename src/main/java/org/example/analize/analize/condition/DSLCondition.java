package org.example.analize.analize.condition;

import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.Interpretation;
import org.example.analize.analize.compison.DSLComparison;
import org.jooq.Condition;

public class DSLCondition extends BaseConditionParser<Condition> {
    public DSLCondition(String request, BaseVariables variables) {
        super(request, variables);
    }

    @Override
    public Condition interpret() {
        switch (operand){
            case ADD -> {
                return comparison.interpret().and(conditionNext.interpret());
            }
            case OR -> {
                return comparison.interpret().or(conditionNext.interpret());
            }
            default -> {
                return comparison.interpret();
            }
        }

    }

    @Override
    Interpretation<Condition> makeComparison(String request, BaseVariables variables) {
        return new DSLComparison(request,variables);
    }

    @Override
    Interpretation<Condition> makeConditionNext(String request, BaseVariables variables) {
        return new DSLCondition(request,variables);
    }
}
