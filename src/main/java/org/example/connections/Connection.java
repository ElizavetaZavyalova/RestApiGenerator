package org.example.connections;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Repeatable(Connections.class)
public @interface Connection {
    String url();
    String dialect();
    String jsonPath() default "";
    String[] type1ToN() default "";//table1.id table2.id
    String[] type1To1() default "";//table1.id id.table2
    String[] typeNtoN() default "";//table1.id id.help.id table3.id
    String[] typeRec() default "";//table1.id help.id.id
}
