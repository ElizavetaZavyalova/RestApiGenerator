package org.example.filter;

import org.jooq.impl.DSL;

import java.util.Map;
import org.jooq.Condition;

public record CallClass() {
    public static Condition make(Map<String,Object> map,String table){
        return DSL.and(DSL.trueCondition(),DSL.falseCondition());
    }

    public static Condition makeMain(Map<String,Object> map,String table){
        return DSL.and(DSL.trueCondition(),DSL.falseCondition());
    }
}
