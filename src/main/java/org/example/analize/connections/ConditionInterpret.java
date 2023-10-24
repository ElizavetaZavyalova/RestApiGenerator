package org.example.analize.connections;

import org.jooq.Condition;
import org.jooq.DSLContext;

public interface ConditionInterpret {
    Condition makeCondition(DSLContext dsl);

}
