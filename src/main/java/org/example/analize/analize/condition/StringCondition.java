package org.example.analize.analize.condition;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.Interpretation;

import org.example.analize.analize.compison.StringComparison;

@Slf4j
public class StringCondition extends BaseConditionParser<String> {
    public StringCondition(String request, BaseVariables variables) {
        super(request, variables);
    }
    @Override
    public String interpret() {
        log.debug("StringCondition interpret:");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(comparison.interpret());
        switch (operand){
            case ADD -> {
                return debug.debugString("StringCondition interpret",
                       stringBuilder.append(".and(").append(conditionNext.interpret())
                        .append(")").toString());
            }
            case OR -> {
                return debug.debugString("StringCondition interpret",
                        stringBuilder.append(".or(").append(conditionNext.interpret())
                                .append(")").toString());
            }
            default -> {
                return debug.debugString("StringCondition interpret",comparison.interpret());
            }
        }

    }

    @Override
    Interpretation<String> makeComparison(String request, BaseVariables variables) {
        log.debug("makeComparison:"+request);
        return new StringComparison(request,variables);
    }

    @Override
    Interpretation<String> makeConditionNext(String request, BaseVariables variables) {
        log.debug("makeConditionNex:"+request);
        return new StringCondition(request,variables);
    }
}
