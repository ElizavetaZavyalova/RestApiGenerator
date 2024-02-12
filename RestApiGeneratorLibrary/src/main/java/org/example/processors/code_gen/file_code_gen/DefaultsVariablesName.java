package org.example.processors.code_gen.file_code_gen;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.Map;

public record DefaultsVariablesName() {
    public record Filter() {
        public static final String TABLE_NAME_IN_FILTER = "table";//context
        public static final String REQUEST_PARAM_NAME = "requestParam";
        public static final String CONDITION_LIST_IN_FILTER = "conditions";
        public static final String DEFAULT_CONDITION_IN_FILTER = "defaultCondition";

    }
    public static final String CONTEXT = "context";//context
    public record Annotations(){

        private static final String contestAnnotations="org.springframework.context.annotation";
        public static final ClassName CONFIGURATION_ANNOTATION_CLASS = ClassName.get(contestAnnotations, "Configuration");
        public static final ClassName BEAN_ANNOTATION_CLASS = ClassName.get(contestAnnotations, "Bean");
        public static final ClassName QUALIFIER_ANNOTATION_CLASS = ClassName.get("org.springframework.beans.factory.annotation", "Qualifier");
        public record Controller(){//org.springframework.beans.factory.annotation.Qualifier;
            private static final String webBindAnnotations="org.springframework.web.bind.annotation";
            public static final ClassName HTTP_STATUS_CLASS = ClassName.get("org.springframework.http", "HttpStatus");

            public static final ClassName RESPONSE_STATUS_ANNOTATION_CLASS = ClassName.get(webBindAnnotations, "ResponseStatus");
            public static final ClassName PATH_VARIABLE_ANNOTATION_CLASS = ClassName.get(webBindAnnotations, "PathVariable");
            public static final ClassName REQUEST_PARAM_ANNOTATION_CLASS = ClassName.get(webBindAnnotations, "RequestParam");
            public static final ClassName REST_CONTROLLER_ANNOTATION_CLASS = ClassName.get(webBindAnnotations, "RestController");
            public static final ClassName OPERATION_ANNOTATION_CLASS = ClassName.get("io.swagger.v3.oas.annotations", "Operation");
            public record RequestMapping(){
                public static final ClassName PUT_MAPPING_ANNOTATION_CLASS = ClassName.get(webBindAnnotations, "PutMapping");
                public static final ClassName GET_MAPPING_ANNOTATION_CLASS = ClassName.get(webBindAnnotations, "GetMapping");
                public static final ClassName POST_MAPPING_ANNOTATION_CLASS = ClassName.get(webBindAnnotations, "PostMapping");
                public static final ClassName PATCH_MAPPING_ANNOTATION_CLASS = ClassName.get(webBindAnnotations, "PatchMapping");
                public static final ClassName DELETE_MAPPING_ANNOTATION_CLASS = ClassName.get(webBindAnnotations, "DeleteMapping");
            }

            public static final ClassName VALUE_ANNOTATION_CLASS =ClassName.get("org.springframework.beans.factory.annotation", "Value");


            public static final ClassName MULTI_VALUE_MAP=ClassName.get("org.springframework.util", "MultiValueMap");
            public static final ParameterizedTypeName REQUEST_PARAMS= ParameterizedTypeName.get(MULTI_VALUE_MAP,
                    ClassName.get("java.lang","String"),  ClassName.get("java.lang","Object"));
        }


    }
    public record DB(){//com.zaxxer.hikari.HikariConfig;
        private static final String orgJooq="org.jooq";
        private static final String comZaxXerHikari="com.zaxxer.hikari";//SQLDialect
        public static final ClassName SQL_DIALECT_CLASS = ClassName.get(orgJooq, "SQLDialect");
        public static final ClassName HIKARI_CONFIG_CLASS = ClassName.get(comZaxXerHikari, "HikariConfig");
        public static final ClassName CONTEXT_CLASS = ClassName.get(orgJooq, "DSLContext");//org.jooq.impl.DSL;
        public static final ClassName DSL_CLASS = ClassName.get(orgJooq+".impl", "DSL");//import com.zaxxer.hikari.HikariDataSource;
        public static final ClassName HIKARI_DATE_SOURCE_CLASS = ClassName.get(comZaxXerHikari, "HikariDataSource");
        public static final ClassName CONDITION_CLASS = ClassName.get(orgJooq, "Condition");
        public static final ClassName RECORD_CLASS = ClassName.get(orgJooq, "Record");
        public static final ClassName RESULT_CLASS = ClassName.get(orgJooq, "Result");
        public static final ParameterizedTypeName RESULT_OF_RECORD_CLASS = ParameterizedTypeName.get(RESULT_CLASS,RECORD_CLASS);
    }
}
