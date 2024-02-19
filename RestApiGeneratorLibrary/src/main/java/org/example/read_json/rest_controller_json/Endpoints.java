package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.VALUE;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.CONTEXT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.CONTEXT_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.createClass;
import static org.example.read_json.ParseJson.Swagger.*;

@Slf4j
public class Endpoints {
    Map<String, Endpoint> endpoint = new TreeMap<>();
    @Getter
    RestJson parent;

    Endpoints(Map<String, Object> map, RestJson parent) throws IllegalArgumentException {
        this.parent = parent;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            endpoint.put(entry.getKey(), new Endpoint(MakeCast.makeMap(entry.getValue(), entry.getKey()), this, entry.getKey()));
        }

    }

    public TypeSpec createRepository(String repositoryName, String beanName) throws IllegalArgumentException {
        String thisName="this.";
        List<MethodSpec> methods = endpoint.values().
                stream().map(Endpoint::getDBMethods).flatMap(List::stream).toList();
        TypeSpec.Builder repository = TypeSpec.classBuilder(repositoryName)
                .addModifiers(Modifier.PUBLIC);

        repository.addField(FieldSpec.builder(BOOLEAN_CLASS, SHOW_SQL_NAME, Modifier.PRIVATE).build());
        repository.addField(FieldSpec.builder(CONTEXT_CLASS, CONTEXT, Modifier.PRIVATE).build())
                .addField(FieldSpec.builder(LOGGER_CLASS, LOG_NAME, Modifier.PRIVATE,Modifier.STATIC,Modifier.FINAL)
                        .initializer("$T.getLogger("+repositoryName+".class.getName());", LOGGER_CLASS).build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(ParameterSpec.builder(CONTEXT_CLASS, beanName).build())
                .addParameter(ParameterSpec.builder(BOOLEAN_CLASS, SHOW_SQL_NAME)
                        .addAnnotation(AnnotationSpec.builder(VALUE_ANNOTATION_CLASS)
                                .addMember(VALUE, "$S","${"+RestJson.DB.prefix + SHOW_SQL_NAME + "}").build()).build())
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement(thisName + CONTEXT + " = " + beanName)
                        .addStatement(thisName + SHOW_SQL_NAME + " = " + SHOW_SQL_NAME)
                        .build());
        methods.forEach(repository::addMethod);
        return repository.build();
    }

    public TypeSpec createController(String controllerName, String repositoryName, String repositoryPath) throws IllegalArgumentException {
        String repositoryBean = repositoryName.substring(0, 1).toLowerCase() + repositoryName.substring(1) + "Bean";
        List<MethodSpec> methods = endpoint.entrySet().
                parallelStream().map(v -> v.getValue().getControllerMethods(repositoryBean)).flatMap(List::stream).toList();
        TypeSpec.Builder controller = TypeSpec.classBuilder(controllerName)
                .addModifiers(Modifier.PUBLIC);
        ClassName repository = createClass(repositoryPath,repositoryName);
        controller.addField(FieldSpec.builder(repository, repositoryBean, Modifier.PRIVATE).build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(ParameterSpec.builder(repository, repositoryBean).build())
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this." + repositoryBean + "=" + repositoryBean)
                        .build());
        methods.forEach(controller::addMethod);
        return controller.build();
    }


}
