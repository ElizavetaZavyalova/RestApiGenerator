package org.example.file_code_gen;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.Map;

public record DefaultsVariablesName() {
    public record Filter() {
        public static final String TABLE_NAME_IN_FILTER = "table";
        public static final String REQUEST_PARAM_MAP = "requestParam";
        public static final String CONDITION_LIST_IN_FILTER = "conditions";
        public static final String DEFAULT_CONDITION_IN_FILTER = "defaultCondition";
        public static final ClassName CONDITION_CLASS = ClassName.get("org.jooq", "Condition");
        public static final ClassName RECORD_CLASS = ClassName.get("org.jooq", "Record");
        public static final ClassName RESULT_CLASS = ClassName.get("org.jooq", "Result");
        public static final ParameterizedTypeName RESULT_OF_RECORD_CLASS = ParameterizedTypeName.get(RESULT_CLASS,RECORD_CLASS);
        public static final ClassName PATH_VARIABLE_ANNOTATION_CLASS = ClassName.get("org.springframework.web.bind.annotation", "PathVariable");

        public static final AnnotationSpec PATH_VARIABLE_ANNOTATION=AnnotationSpec.builder(PATH_VARIABLE_ANNOTATION_CLASS).build();

    }
}
