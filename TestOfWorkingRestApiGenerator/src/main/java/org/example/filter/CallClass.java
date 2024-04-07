package org.example.filter;

import org.jooq.impl.DSL;


import org.jooq.Condition;
import org.springframework.util.MultiValueMap;

public record CallClass() {
    public static Condition make(MultiValueMap<String,String> map,String table){
        return DSL.and(DSL.trueCondition(),DSL.falseCondition());
    }

    public static Condition makeMain(MultiValueMap<String,String> map, String table){
        return DSL.and(DSL.trueCondition(),DSL.falseCondition());
    }
}
