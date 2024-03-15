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

import java.util.List;
import java.util.Map;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.STRING_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.VALUE_ANNOTATION_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.SwaggerConfig.INFO_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.SwaggerConfig.OPEN_API_CLASS;

import org.example.read_json.rest_controller_json.JsonKeyWords.ApplicationProperties.Swagger;


@Getter
@Slf4j
public class ParseJson {
    static ReadJson loadJson = new ReadJson();
    List<RestJson> restsJson = new ArrayList<>();
    static final String BEAN = "dsl";
    public ParseJson(String jsonPath) {
        try {
            loadJson.load(jsonPath).forEach(
                    (key, object) -> restsJson.add(new RestJson( loadJson.loadRest(object, jsonPath), key, BEAN)));
        } catch (IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            AST.instance().getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
        }
    }

    public void generate() {
        restsJson.parallelStream().forEach(RestJson::generate);
    }


    static MethodSpec makeSwaggerConfiguration() {
        return MethodSpec.methodBuilder("usersMicroserviceOpenAPI").addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(STRING_CLASS, Swagger.title)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE, "$S", "${" + Swagger.getParam(Swagger.title) + "}").build()).build())
                .addParameter(ParameterSpec.builder(STRING_CLASS, Swagger.description)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE, "$S", "${" + Swagger.getParam(Swagger.description) + "}").build()).build())
                .addParameter(ParameterSpec.builder(STRING_CLASS, Swagger.version)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE, "$S", "${" + Swagger.getParam(Swagger.version) + "}").build()).build())
                .addAnnotation(AnnotationSpec.builder(BEAN_ANNOTATION_CLASS).build())
                .returns(OPEN_API_CLASS)
                .addStatement("return new $T().info(new $T().title(" + Swagger.title + ")" +
                        ".description(" + Swagger.description + ").version(" + Swagger.version + "))", OPEN_API_CLASS, INFO_CLASS)
                .build();
    }


    public JavaFile getConfiguration(String className, String packageName) {
        return JavaFile.builder(packageName, makeConfigurationClass(className)).build();
    }

    TypeSpec makeConfigurationClass(String className) {
        TypeSpec.Builder repository = TypeSpec.classBuilder(className)
                .addAnnotation(CONFIGURATION_ANNOTATION_CLASS)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(makeSwaggerConfiguration());
        return repository.build();
    }


}
