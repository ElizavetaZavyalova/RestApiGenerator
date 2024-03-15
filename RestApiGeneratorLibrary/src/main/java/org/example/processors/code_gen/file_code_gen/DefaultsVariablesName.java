package org.example.processors.code_gen.file_code_gen;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.webBindAnnotations;


public record DefaultsVariablesName() {
    public static boolean DEBUG = false;

    private static String getPath(String path) {
        if (!DEBUG) {
            return path;
        }
        return "";
    }

    public static ClassName createClass(String path, String className) {
        return ClassName.get(getPath(path), className);
    }
    public record Filter() {
        public static final String TABLE_NAME_IN_FILTER = "table";
        public static final String REQUEST_PARAM_NAME = "returnFields";
        public static final String REQUEST_PARAM_BODY = "entity";
        public static final String CONDITION_LIST_IN_FILTER = "conditions";
        public static final String DEFAULT_CONDITION_IN_FILTER = "defaultCondition";
    }

    public static final String CONTEXT = "context";//

    public record Annotations() {
        public static final String VALUE = "value";
        private static final String contestAnnotations = "org.springframework.context.annotation";
        private static final String beansFactoryAnnotations = "org.springframework.beans.factory.annotation";
        public static final ClassName CONFIGURATION_ANNOTATION_CLASS = createClass(contestAnnotations, "Configuration");
        public static final ClassName BEAN_ANNOTATION_CLASS = createClass(contestAnnotations, "Bean");

        public record SwaggerConfig() {
            private static final String swaggerV3OasModels = "io.swagger.v3.oas.models";
            private static final String swaggerV3OasAnnotations = "io.swagger.v3.oas.annotations";
            private static final String swaggerV3OasAnnotationsMedia = "io.swagger.v3.oas.annotations.media";
            private static final String swaggerV3OasModelsInfo = "io.swagger.v3.oas.models.info";
            public static final ClassName SCHEMA_ANNOTATION_CLASS = createClass( swaggerV3OasAnnotationsMedia, "Schema");
            public static final ClassName OPERATION_ANNOTATION_CLASS = createClass(swaggerV3OasAnnotations, "Operation");
            public static final ClassName PARAMETER_ANNOTATION_CLASS = createClass(swaggerV3OasAnnotations, "Parameter");
            public static final ClassName INFO_CLASS = createClass(swaggerV3OasModelsInfo, "Info");
            public static final ClassName OPEN_API_CLASS = createClass(swaggerV3OasModels, "OpenAPI");
        }

        public record Controller() {
            static final String webBindAnnotations = "org.springframework.web.bind.annotation";
            private static final String transactionAnnotation = "org.springframework.transaction.annotation";
            private static final String springStereotype = "org.springframework.stereotype";
            public static final ClassName TRANSACTIONAL_ANNOTATION_CLASS = createClass(transactionAnnotation, "Transactional");
            private static final String http = "org.springframework.http";
            private static final String validConstants = "jakarta.validation.constraints";
            public static final ClassName HTTP_STATUS_CLASS = createClass(http, "HttpStatus");
            public static final ClassName RESPONSE_STATUS_ANNOTATION_CLASS = createClass(webBindAnnotations, "ResponseStatus");
            public static final ClassName PATH_VARIABLE_ANNOTATION_CLASS = createClass(webBindAnnotations, "PathVariable");
            public static final ClassName REQUEST_BODY_ANNOTATION_CLASS = createClass(webBindAnnotations, "RequestBody");
            public static final ClassName REQUEST_PARAM_ANNOTATION_CLASS = createClass(webBindAnnotations, "RequestParam");

            public static final ClassName MIN_PARAM_ANNOTATION_CLASS = createClass(validConstants, "Min");
            public static final ClassName REST_CONTROLLER_ANNOTATION_CLASS = createClass(webBindAnnotations, "RestController");


            public record RequestMapping() {
                public static final ClassName REQUEST_MAPPING_ANNOTATION_CLASS = createClass(webBindAnnotations, "RequestMapping");
                public static final ClassName PUT_MAPPING_ANNOTATION_CLASS = createClass(webBindAnnotations, "PutMapping");
                public static final ClassName GET_MAPPING_ANNOTATION_CLASS = createClass(webBindAnnotations, "GetMapping");
                public static final ClassName POST_MAPPING_ANNOTATION_CLASS = createClass(webBindAnnotations, "PostMapping");
                public static final ClassName PATCH_MAPPING_ANNOTATION_CLASS = createClass(webBindAnnotations, "PatchMapping");
                public static final ClassName DELETE_MAPPING_ANNOTATION_CLASS = createClass(webBindAnnotations, "DeleteMapping");
            }

            public static final ClassName REPOSITORY_ANNOTATION_CLASS = createClass(springStereotype, "Repository");
            public static final ClassName AUTOWIRED_ANNOTATION_CLASS = createClass(beansFactoryAnnotations, "Autowired");
            public static final ClassName VALUE_ANNOTATION_CLASS = createClass(beansFactoryAnnotations, "Value");
            private static final String javaLang = "java.lang";
            private static final String javaLangLevelLogger = "java.util.logging";
            private static final String javaUtil = "java.util";
            private static final String javaUtilLogging = "java.util.logging";
            public static final String LOG_NAME = "log";
            public static final String RESULT_NAME = "result";
            public static final String RESULT_MAP = "resultMap";
            public static final String LOG_LEVE_NAME = "INFO";
            public static final String FIELDS_NAME="fieldsName";
            public static final String LIST_NAME="list";
            public static final String LIST_NAME_DEFAULT="listDefault";
            public static final ClassName LOG_LEVEL = createClass(javaLangLevelLogger, "Level");
            public static final ClassName LOGGER_CLASS = createClass(javaUtilLogging, "Logger");
            public static final ClassName MAP_CLASS = createClass(javaUtil, "Map");
            public static final ClassName HASH_MAP_CLASS = createClass(javaUtil, " HashMap");
            public static final ClassName LIST_CLASS = createClass(javaUtil, "List");
            public static final ClassName STRING_CLASS = createClass(javaLang, "String");
            public static final ClassName ARRAY_LIST_CLASS = createClass(javaUtil, "ArrayList");
            public static final ClassName INTEGER_CLASS = createClass(javaLang, "Integer");
            public static final ClassName LONG_CLASS = createClass(javaLang, "Long");
            public static final ClassName FLOAT_CLASS = createClass(javaLang, "Float");
            public static final ClassName DOUBLE_CLASS = createClass(javaLang, "Double");
            public static final ClassName BOOLEAN_CLASS = createClass(javaLang, "Boolean");
            public static final ClassName OBJECT_CLASS = createClass(javaLang, "Object");
            public static final ParameterizedTypeName PARAMETRIZED_MAP = ParameterizedTypeName.get(MAP_CLASS,
                    STRING_CLASS, OBJECT_CLASS);

            private static final String springUtil="org.springframework.util";
            public static final ClassName MULTI_VALUE_MAP_CLASS = createClass(springUtil, "MultiValueMap");
            public static final ParameterizedTypeName REQUEST_PARAMS  = ParameterizedTypeName.get(MULTI_VALUE_MAP_CLASS,
                    STRING_CLASS, STRING_CLASS);

            public static final ParameterizedTypeName PARAMETERIZED_LIST = ParameterizedTypeName.get(LIST_CLASS,
                    PARAMETRIZED_MAP);
        }
    }


    public record DB() {//java.sql.SQLException
       public record LoggerColor(){
           public static final String _DELETE_COLOR="\n\u001B[35m";
           public static final String _GET_COLOR="\n\u001B[34m";
           public static final String _PUT_COLOR="\n\u001B[33m";
           public static final String _POST_COLOR="\n\u001B[32m";
           public static final String _PATCH_COLOR="\n\u001B[36m";
           public static final String _RESET_COLOR="\n\u001B[0m";
        }
        private static final String javaSql = "java.sql";
        private static final String orgJooq = "org.jooq";
        private static final String orgJooqIml = "org.jooq.impl";
        public static final ClassName FIELD_CLASS=createClass(orgJooq, "Field");
        public static final ClassName CONTEXT_CLASS = createClass(orgJooq, "DSLContext");
        public static final ClassName DSL_CLASS = createClass(orgJooqIml, "DSL");
        public static final ClassName CONDITION_CLASS = createClass(orgJooq, "Condition");
        public  record ExceptionOfSQL(){

           static final String http="org.springframework.http";
            static final String springDocApi="org.springdoc.api";
            public static final ClassName RESPONSE_ENTITY_CLASS=createClass(http, "ResponseEntity");
            public static final ClassName SQL_EXCEPTION_CLASS=createClass(javaSql, "SQLException");
            public static final ClassName EXCEPTION_HANDLER_CLASS=createClass(webBindAnnotations, "ExceptionHandler");
            public static final ClassName ERROR_MASSAGE_CLASS=createClass(springDocApi, "ErrorMessage");
            public static final ParameterizedTypeName PARAMETERIZED_RESPONSE_ENTITY_CLASS =
                    ParameterizedTypeName.get(RESPONSE_ENTITY_CLASS,
                            ERROR_MASSAGE_CLASS);
        }
    }
}
