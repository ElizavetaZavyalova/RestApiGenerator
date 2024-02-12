package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.read_json.ReadJson;
import org.example.read_json.rest_controller_json.filter.RestJsonFilters;
import org.example.read_json.rest_controller_json.pseudonyms.RestJsonPseudonyms;


import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.BEAN_ANNOTATION_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.VALUE_ANNOTATION_CLASS;
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

    @Getter
    boolean createBDBean = false;
    private ReadJson readeJson = new ReadJson();


    public RestJson(Map<String, Object> map, String locationName, String beanName) {
        try {
            this.locationName = locationName;
            createBDBean = MakeCast.makeBoolean(map, CREATE_DB_BEAN, false);
            this.beanName = createBean(beanName);
            http = new Endpoints(readeJson.loadEndpoints(map), this);
            pseudonyms = new RestJsonPseudonyms(readeJson.loadPseudonyms(map), this);
            filters = new RestJsonFilters(readeJson.loadFilters(map), this);
        } catch (IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            //TODO stop compilation
        }
    }

    private String createBean(String beanName) {
        if (createBDBean) {
            this.beanName = locationName;
            return locationName;
        }
        return beanName;
    }

    public JavaFile getJavaRepository(String className, String packageName) {
        return JavaFile.builder(packageName, http.createRepository(className, beanName)).build();

    }

    public JavaFile getJavaController(String className, String packageName, String repositoryName, String repositoryPath) {
        return JavaFile.builder(packageName, http.createController(className, repositoryName, repositoryPath)).build();
    }

    public static MethodSpec.Builder createConnectionBeanBuilder(String beanName, String url, String password, String user, String driver, String dialect) {
        return MethodSpec.methodBuilder(beanName).addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(String.class, DB.url)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember("value", "${" + url + "}").build()).build())
                .addParameter(ParameterSpec.builder(String.class, DB.password)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember("value", "${" + password + "}").build()).build())
                .addParameter(ParameterSpec.builder(String.class, DB.user)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember("value", "${" + user + "}").build()).build())
                .addParameter(ParameterSpec.builder(String.class, DB.driver)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember("value", "${" + driver + "}").build()).build())
                .addParameter(ParameterSpec.builder(String.class, DB.dialect)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember("value", "${" + dialect + "}").build()).build())
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
        static final String CONNECTION_REST = "connectionRest";
    }

    public static MethodSpec createConnectionMethod() {
        return MethodSpec.methodBuilder(CONNECTION_REST).addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(String.class, url).build())
                .addParameter(ParameterSpec.builder(String.class, password).build())
                .addParameter(ParameterSpec.builder(String.class, user).build())
                .addParameter(ParameterSpec.builder(String.class, driver).build())
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
        method.addStatement("return $T.using(new $T(CONNECTION_REST(" + url + ", " + password + ", " + user + ", " + driver + "), $T.valueOf(" + dialect + "))",
                DSL_CLASS, HIKARI_DATE_SOURCE_CLASS, SQL_DIALECT_CLASS);
        return method.build();
    }


}
