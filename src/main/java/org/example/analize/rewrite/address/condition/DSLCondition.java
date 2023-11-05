package org.example.analize.rewrite.address.condition;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.comparison.DSLComparison;
import org.example.analize.rewrite.address.premetive.table.BaseTable;
import org.jooq.Condition;
import org.jooq.Table;

@Slf4j
public class DSLCondition extends BaseConditionParser<Condition, Table> {
    public DSLCondition(String request, Variables variables, BaseTable<Table> table) {
        super(request, variables, table);
        Debug.debug(log, "DSLCondition request:", request);
    }

    @Override
    Interpretation<Condition> makeComparison(String request, Variables variables, BaseTable<Table> table) {
        Debug.debug(log, "makeComparison request:", request, " table:", table.getTable());
        return new DSLComparison(request, variables, table);
    }

    @Override
    Interpretation<Condition> makeConditionNext(String request, Variables variables, BaseTable<Table> table) {
        Debug.debug(log, "makeConditionNext request: ", request, " table: ", table.getTable());
        return new DSLCondition(request, variables, table);
    }


    @Override
    public Condition interpret() {
        log.debug("interpret: " + operand.getValue());
        switch (operand) {
            case AND -> {
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


}
