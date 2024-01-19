package org.example.file_code_gen;

import com.squareup.javapoet.ClassName;

public record DefaultsVariablesName() {
    public record Filter() {
        public static final String TABLE_NAME_IN_FILTER = "table";
        public static final String REQUEST_PARAM_MAP_IN_FILTER = " requestParam";
        public static final String CONDITION_LIST_IN_FILTER = "conditions";
        public static final String DEFAULT_CONDITION_IN_FILTER = "defaultCondition";
        public static final ClassName CONDITION_CLASS = ClassName.get("org.jooq", "Condition");
    }
}
