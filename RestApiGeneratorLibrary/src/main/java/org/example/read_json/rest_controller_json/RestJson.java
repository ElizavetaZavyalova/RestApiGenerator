package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.BaseFieldParser;
import org.example.read_json.ReadJson;
import org.example.read_json.rest_controller_json.filter.RestJsonFilters;
import org.example.read_json.rest_controller_json.pseudonyms.RestJsonPseudonyms;


import javax.lang.model.element.Modifier;


import java.util.Map;



import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.BEAN_ANNOTATION_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RequestMapping.REQUEST_MAPPING_ANNOTATION_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.VALUE;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.*;
import static org.example.read_json.rest_controller_json.RestJson.DB.*;


@Slf4j
public class RestJson {
    @Getter
    String locationName;
    Endpoints http;
    @Getter
    RestJsonPseudonyms pseudonyms;
    @Getter
    RestJsonFilters filters;
    String beanName = "";
    String mappingPrefix="";//ADDRESS_PREFIX

    @Getter
    boolean createBDBean = false;
    ReadJson readeJson = new ReadJson();//@RequestMapping("deduplication")

    public RestJson(Map<String, Object> map, String locationName, String beanName) throws IllegalArgumentException {
        this.locationName = locationName;
        createBDBean = MakeCast.makeBoolean(map, CREATE_DB_BEAN, false);
        this.beanName = createBean(beanName);
        this.mappingPrefix=MakeCast.makeStringIfContainsKeyMap(map,ADDRESS_PREFIX,false);
        pseudonyms = new RestJsonPseudonyms(readeJson.loadPseudonyms(map), this);
        filters = new RestJsonFilters(readeJson.loadFilters(map), this);
        http = new Endpoints(readeJson.loadEndpoints(map), this);
    }

    private String createBean(String beanName) {
        if (createBDBean) {
            this.beanName = locationName;
            return locationName;
        }
        return beanName;
    }

    public JavaFile getJavaRepository(String className, String packageName) {
        return JavaFile.builder(packageName, http.createRepository(className, beanName))
                .addStaticImport(HTTP_STATUS_CLASS,"*").build();


    }
    private TypeSpec addMappingPrefix(TypeSpec typeSpec){
        if(!mappingPrefix.isEmpty()) {
            return typeSpec.toBuilder().addAnnotation(
                    AnnotationSpec.builder(REQUEST_MAPPING_ANNOTATION_CLASS)
                            .addMember(VALUE, "$S", mappingPrefix).build()).build();
        }
        return typeSpec;
    }

    public JavaFile getJavaController(String className, String packageName, String repositoryName, String repositoryPath) {
        TypeSpec typeSpec=http.createController(className, repositoryName, repositoryPath);
        typeSpec=addMappingPrefix(typeSpec);
        return JavaFile.builder(packageName, typeSpec).build();
    }


    public static MethodSpec.Builder createConnectionBeanBuilder(String beanName, String url, String password, String user, String driver, String dialect) {
        return MethodSpec.methodBuilder(beanName).addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(STRING_CLASS, DB.url)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE, "$S","${" + url + "}").build()).build())
                .addParameter(ParameterSpec.builder(STRING_CLASS, DB.password)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE, "$S","${" + password + "}").build()).build())
                .addParameter(ParameterSpec.builder(STRING_CLASS, DB.user)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE, "$S","${" + user + "}").build()).build())
                .addParameter(ParameterSpec.builder(STRING_CLASS, DB.driver)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE, "$S","${" + driver + "}").build()).build())
                .addParameter(ParameterSpec.builder(STRING_CLASS, DB.dialect)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE, "$S","${" + dialect + "}").build()).build())
                .addAnnotation(AnnotationSpec.builder(BEAN_ANNOTATION_CLASS).build())
                .returns(CONTEXT_CLASS);
    }

    public record DB() {
        public static final String url = "url";

        public static final String password = "password";
        public static final String user = "user";
        public static final String driver = "driver";
        public static final String dialect = "dialect";
        public static final String prefix = "db.";
        public static final String CONNECTION_REST = "connectionRest";
    }

    public static MethodSpec createConnectionMethod() {
        return MethodSpec.methodBuilder(CONNECTION_REST).addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(STRING_CLASS, url).build())
                .addParameter(ParameterSpec.builder(STRING_CLASS, password).build())
                .addParameter(ParameterSpec.builder(STRING_CLASS, user).build())
                .addParameter(ParameterSpec.builder(STRING_CLASS, driver).build())
                .returns(HIKARI_CONFIG_CLASS)
                .addStatement("$T hikariConfig = new $T()", HIKARI_CONFIG_CLASS, HIKARI_CONFIG_CLASS)
                .addStatement("hikariConfig.setJdbcUrl(" + url + ")")
                .addStatement("hikariConfig.setUsername(" + user + ")")
                .addStatement("hikariConfig.setPassword(" + password + ")")
                .addStatement("hikariConfig.setDriverClassName(" + driver + ")")
                .addStatement("return hikariConfig").build();
    }

    public MethodSpec getConnectionBean() {
        String prop = DB.prefix + locationName + ".";
        String url = prop + DB.url;
        String password = prop + DB.password;
        String user = prop + DB.user;
        String driver = prop + DB.driver;
        String dialect = prop + DB.dialect;
        MethodSpec.Builder method = createConnectionBeanBuilder(beanName, url, password, user, driver, dialect);
        method.addStatement("return $T.using(new $T("+CONNECTION_REST+"(" + DB.url + ", " + DB.password + ", " + DB.user + ", " + DB.driver + ")), $T.valueOf(" + DB.dialect + "))",
                DSL_CLASS, HIKARI_DATE_SOURCE_CLASS, SQL_DIALECT_CLASS);
        return method.build();
    }


}
