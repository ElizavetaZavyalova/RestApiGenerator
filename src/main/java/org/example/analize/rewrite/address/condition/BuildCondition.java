package org.example.analize.rewrite.address.condition;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.comparison.BuildComparison;
import org.example.analize.rewrite.address.premetive.table.BaseTable;
@Slf4j
public class BuildCondition extends BaseConditionParser<String,String> {
    public BuildCondition(String request, Variables variables, BaseTable<String> table) {
        super(request, variables, table);
        Debug.debug(log, "BuildCondition request:", request);
    }
    @Override
    public String interpret() {
        log.debug("interpret: " + operand.getValue());
        StringBuilder result=new StringBuilder().append(comparison.interpret());
        switch (operand) {
            case AND:{
                result.append(".and(").append(conditionNext.interpret()).append(")");
                break;
            }
            case OR:{
                result.append(".or(").append(conditionNext.interpret()).append(")");
                break;
            }
        }
        return  Debug.debugResult(log,"interpret",result.toString());
    }

    @Override
    Interpretation<String> makeComparison(String request, Variables variables, BaseTable<String> table) {
        Debug.debug(log, "makeComparison request:", request, " table:", table.getTable());
        return new BuildComparison(request, variables, table);
    }

    @Override
    Interpretation<String> makeConditionNext(String request, Variables variables, BaseTable<String> table) {
        Debug.debug(log, "makeConditionNext request: ", request, " table: ", table.getTable());
        return new BuildCondition(request, variables, table);
    }
}
