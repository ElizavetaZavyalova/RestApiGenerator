package org.example.read_json;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.AST;
import org.example.read_json.rest_controller_json.MakeCast;
import org.example.read_json.rest_controller_json.RestJson;

import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.STRING_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.VALUE_ANNOTATION_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.SwaggerConfig.INFO_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.SwaggerConfig.OPEN_API_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.*;
import static org.example.read_json.ParseJson.Swagger.*;
import static org.example.read_json.rest_controller_json.RestJson.DB.CONNECTION_REST;


@Slf4j
public class ParseJson {
    static LoadJson<Map<String, Object>> loadJson = new ReadJson();
    @Getter
    List<RestJson> restsJson = new ArrayList<>();
    static final String bean = "dsl";

    public ParseJson(String jsonPath) {
        try {
            loadJson.load(jsonPath).forEach(
                    (key, object) -> restsJson.add(new RestJson(MakeCast.makeMap(object, jsonPath), key, bean)));
        } catch (IOException | IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            AST.instance().getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
        }
    }

    public record Swagger() {
        static final String title = "title";
        static final String openApi = "openApi";
        static final String description = "description";
        static final String version="version";
        static  String getParam(String param){
            return openApi+"."+param;
        }
    }

    static MethodSpec makeSwaggerConfiguration() {
        return MethodSpec.methodBuilder("usersMicroserviceOpenAPI").addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(STRING_CLASS, title)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE,"$S", "${" + getParam(title) + "}").build()).build())
                .addParameter(ParameterSpec.builder(STRING_CLASS, description)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE, "$S", "${" + getParam(description) + "}").build()).build())
                .addParameter(ParameterSpec.builder(STRING_CLASS, version)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE, "$S", "${" + getParam(version) + "}").build()).build())
                .addAnnotation(AnnotationSpec.builder(BEAN_ANNOTATION_CLASS).build())
                .returns(OPEN_API_CLASS)
                .addStatement("return new $T().info(new $T().title("+title+")"+
                ".description("+description+").version("+version+"))",OPEN_API_CLASS,INFO_CLASS)
                .build();
    }

    MethodSpec makeDefaultDBBean() {
        String prop = RestJson.DB.prefix;
        String url = prop + RestJson.DB.url;
        String password = prop + RestJson.DB.password;
        String user = prop + RestJson.DB.user;
        String driver = prop + RestJson.DB.driver;
        String dialect = prop + RestJson.DB.dialect;
        MethodSpec.Builder method = RestJson.createConnectionBeanBuilder(bean, url, password, user, driver, dialect);
        method.addStatement("return $T.using(new $T("+CONNECTION_REST+"(" + RestJson.DB.url + ", " + RestJson.DB.password + ", "
                        + RestJson.DB.user + ", " + RestJson.DB.driver + ")), $T.valueOf(" + RestJson.DB.dialect + "))",
                DSL_CLASS, HIKARI_DATE_SOURCE_CLASS, SQL_DIALECT_CLASS);
        return method.build();
    }

    public JavaFile getConfiguration(String className, String packageName) {
        return JavaFile.builder(packageName, makeConfigurationClass(className)).build();
    }

    TypeSpec makeConfigurationClass(String className) {
        TypeSpec.Builder repository = TypeSpec.classBuilder(className)
                .addAnnotation(CONFIGURATION_ANNOTATION_CLASS)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(makeSwaggerConfiguration());
        restsJson.forEach(rest -> {
            if (rest.isCreateBDBean()) {
                repository.addMethod(rest.getConnectionBean());
            }
        });
        repository.addMethod(RestJson.createConnectionMethod());
        repository.addMethod(makeDefaultDBBean());
        return repository.build();
    }


}
