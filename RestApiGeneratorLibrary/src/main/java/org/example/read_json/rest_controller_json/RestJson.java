package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.example.read_json.ReadJson;
import org.example.read_json.rest_controller_json.filter.RestJsonFilters;
import org.example.read_json.rest_controller_json.pseudonyms.RestJsonPseudonyms;


import java.util.Map;


import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RequestMapping.REQUEST_MAPPING_ANNOTATION_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.VALUE;
import static org.example.read_json.rest_controller_json.JsonKeyWords.*;


@Slf4j
public class RestJson {
    @Getter
    private  final String locationName;
    private  final Endpoints http;
    @Getter
    private  final RestJsonPseudonyms pseudonyms;
    @Getter
    private  final RestJsonFilters filters;
    private  final String beanName;
    private  final  String mappingPrefix;


    ReadJson readeJson = new ReadJson();

    public RestJson(Map<String, Object> map, String locationName, String beanName) throws IllegalArgumentException {
        this.locationName = locationName;
        this.beanName = beanName;
        this.mappingPrefix = MakeCast.makeStringIfContainsKeyMap(map, ADDRESS_PREFIX, false);
        pseudonyms = new RestJsonPseudonyms(readeJson.loadPseudonyms(map), this);
        filters = new RestJsonFilters(readeJson.loadFilters(map), this);
        http = new Endpoints(readeJson.loadEndpoints(map), this);
    }


    public JavaFile getJavaRepository(String className, String packageName) {
        TypeSpec typeSpec = http.createRepository(className, beanName);
        typeSpec = addRepositoryAnnotation(typeSpec);
        return JavaFile.builder(packageName, typeSpec)
                .addStaticImport(HTTP_STATUS_CLASS, "*").build();


    }

    private static TypeSpec addRepositoryAnnotation(TypeSpec typeSpec) {
        return typeSpec.toBuilder().addAnnotation(AnnotationSpec.builder(REPOSITORY_ANNOTATION_CLASS).build()).build();
    }

    private TypeSpec addMappingPrefix(TypeSpec typeSpec) {
        if (!mappingPrefix.isEmpty()) {
            return typeSpec.toBuilder().addAnnotation(
                    AnnotationSpec.builder(REQUEST_MAPPING_ANNOTATION_CLASS)
                            .addMember(VALUE, "$S", mappingPrefix).build()).build();
        }
        return typeSpec;
    }

    private static TypeSpec addRestAnnotation(TypeSpec typeSpec) {
        return typeSpec.toBuilder().addAnnotation(AnnotationSpec.builder(REST_CONTROLLER_ANNOTATION_CLASS).build()).build();
    }

    public JavaFile getJavaController(String className, String packageName, String repositoryName, String repositoryPath) {
        TypeSpec typeSpec = http.createController(className, repositoryName, repositoryPath);
        typeSpec = addMappingPrefix(typeSpec);
        typeSpec = addRestAnnotation(typeSpec);
        return JavaFile.builder(packageName, typeSpec).build();
    }


}
