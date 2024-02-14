package org.example.processors.code_gen.file_code_gen;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;



public record DefaultsVariablesName() {
    static boolean DEBUG=true;
    private static String getPath(String path){
        if(!DEBUG){
            return path;
        }
        return "";
    }
    public static ClassName createClass(String path,String className){
        return ClassName.get(getPath(path), className);
    }
    public record Filter() {
        public static final String TABLE_NAME_IN_FILTER = "table";
        public static final String REQUEST_PARAM_NAME = "requestParam";
        public static final String CONDITION_LIST_IN_FILTER = "conditions";
        public static final String DEFAULT_CONDITION_IN_FILTER = "defaultCondition";

    }
    public static final String CONTEXT = "context";
    public record Annotations(){


        private static final String contestAnnotations="org.springframework.context.annotation";
        private static final String beansFactoryAnnotations="org.springframework.beans.factory.annotation";
        public static final ClassName CONFIGURATION_ANNOTATION_CLASS = createClass(contestAnnotations,"Configuration");
        public static final ClassName BEAN_ANNOTATION_CLASS = createClass(contestAnnotations, "Bean");
        public static final ClassName QUALIFIER_ANNOTATION_CLASS = createClass(beansFactoryAnnotations, "Qualifier");
        public record Controller(){
            private static final String webBindAnnotations="org.springframework.web.bind.annotation";
            private static final String http="org.springframework.http";
            private static final String swaggerV3OasAnnotations="io.swagger.v3.oas.annotations";
            public static final ClassName HTTP_STATUS_CLASS = createClass(http, "HttpStatus");

            public static final ClassName RESPONSE_STATUS_ANNOTATION_CLASS = createClass(webBindAnnotations, "ResponseStatus");
            public static final ClassName PATH_VARIABLE_ANNOTATION_CLASS = createClass(webBindAnnotations, "PathVariable");
            public static final ClassName REQUEST_PARAM_ANNOTATION_CLASS = createClass(webBindAnnotations, "RequestParam");
            public static final ClassName REST_CONTROLLER_ANNOTATION_CLASS = createClass(webBindAnnotations, "RestController");
            public static final ClassName OPERATION_ANNOTATION_CLASS = createClass(swaggerV3OasAnnotations, "Operation");
            public record RequestMapping(){
                public static final ClassName PUT_MAPPING_ANNOTATION_CLASS = createClass(webBindAnnotations, "PutMapping");
                public static final ClassName GET_MAPPING_ANNOTATION_CLASS = createClass(webBindAnnotations, "GetMapping");
                public static final ClassName POST_MAPPING_ANNOTATION_CLASS = createClass(webBindAnnotations, "PostMapping");
                public static final ClassName PATCH_MAPPING_ANNOTATION_CLASS = createClass(webBindAnnotations, "PatchMapping");
                public static final ClassName DELETE_MAPPING_ANNOTATION_CLASS = createClass(webBindAnnotations, "DeleteMapping");
            }

            public static final ClassName VALUE_ANNOTATION_CLASS =createClass(beansFactoryAnnotations, "Value");

            private static final String springUtil="org.springframework.util";
            private static final String javaLang="java.lang";
            private static final String javaUtil="java.util";
            public static final ClassName MAP_CLASS=createClass(javaUtil, "Map");
            public static final ClassName LIST_CLASS=createClass(javaUtil, "List");//STRRING_CLASS
            public static final ClassName STRING_CLASS=createClass(javaLang, "String");

            public static final ClassName ARRAY_LIST_CLASS=createClass(javaUtil, "ArrayList");
            public static final ClassName INTEGER_CLASS=createClass(javaLang, "Integer");
            public static final ClassName BOOLEAN_CLASS=createClass(javaLang, "Boolean");

            public static final ClassName MULTI_VALUE_MAP=createClass(springUtil, "MultiValueMap");
            public static final ParameterizedTypeName REQUEST_PARAMS= ParameterizedTypeName.get(MULTI_VALUE_MAP,
                    STRING_CLASS, createClass(javaLang,"Object"));
        }


    }
    public record DB(){
        private static final String orgJooq="org.jooq";
        private static final String orgJooqIml="org.jooq.iml";
        private static final String comZaxXerHikari="com.zaxxer.hikari";
        public static final ClassName SQL_DIALECT_CLASS = createClass(orgJooq, "SQLDialect");
        public static final ClassName HIKARI_CONFIG_CLASS = createClass(comZaxXerHikari, "HikariConfig");
        public static final ClassName CONTEXT_CLASS = createClass(orgJooq, "DSLContext");
        public static final ClassName DSL_CLASS = createClass(orgJooqIml, "DSL");
        public static final ClassName HIKARI_DATE_SOURCE_CLASS = createClass(comZaxXerHikari, "HikariDataSource");
        public static final ClassName CONDITION_CLASS = createClass(orgJooq, "Condition");
        public static final ClassName RECORD_CLASS = createClass(orgJooq, "Record");
        public static final ClassName RESULT_CLASS = createClass(orgJooq, "Result");
        public static final ParameterizedTypeName RESULT_OF_RECORD_CLASS = ParameterizedTypeName.get(RESULT_CLASS,RECORD_CLASS);
    }
}
