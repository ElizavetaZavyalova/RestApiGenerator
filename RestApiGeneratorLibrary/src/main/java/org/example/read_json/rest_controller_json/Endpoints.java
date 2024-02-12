package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.REST_CONTROLLER_ANNOTATION_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.CONTEXT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.CONTEXT_CLASS;

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
        List<MethodSpec> methods = endpoint.entrySet().
                parallelStream().map(v -> v.getValue().getDBMethods()).flatMap(List::stream).toList();
        TypeSpec.Builder repository = TypeSpec.classBuilder(repositoryName)
                .addModifiers(Modifier.PUBLIC);
        repository.addField(FieldSpec.builder(CONTEXT_CLASS, CONTEXT, Modifier.PRIVATE).build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(ParameterSpec.builder(CONTEXT_CLASS, beanName).build())
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this." + CONTEXT + " = " + beanName)
                        .build());
        methods.forEach(repository::addMethod);
        return repository.build();
    }

    public TypeSpec createController(String controllerName, String repositoryName, String repositoryPath) throws IllegalArgumentException {
        String repositoryBean = repositoryName.substring(0, 1).toLowerCase() + repositoryName.substring(1) + "Bean";
        List<MethodSpec> methods = endpoint.entrySet().
                parallelStream().map(v -> v.getValue().getControllerMethods(repositoryBean)).flatMap(List::stream).toList();
        TypeSpec.Builder controller = TypeSpec.classBuilder(controllerName)
                .addModifiers(Modifier.PUBLIC).addAnnotation(AnnotationSpec.builder(REST_CONTROLLER_ANNOTATION_CLASS).build());
        ClassName repository = ClassName.get(repositoryPath, repositoryName);
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
