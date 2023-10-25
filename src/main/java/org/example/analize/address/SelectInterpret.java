package org.example.analize.address;

import org.jooq.DSLContext;
import org.jooq.Select;

public interface SelectInterpret {
    Select makeSelect(DSLContext dsl);
    String makeSelect();
}
