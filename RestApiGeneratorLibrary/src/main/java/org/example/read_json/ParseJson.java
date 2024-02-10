package org.example.read_json;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
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

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.CONFIGURATION_ANNOTATION_CLASS;



@Slf4j
public class ParseJson {
    static LoadJson<Map<String,Object>> loadJson = new ReadJson();
    @Getter
    List<RestJson> restsJson=new ArrayList<>();

    public ParseJson(String jsonPath){
        try {
            loadJson.load(jsonPath).forEach(
                    (key,object) -> restsJson.add(new RestJson(MakeCast.makeMap(object, jsonPath),key,null)));
        } catch (IOException|IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            AST.instance().getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
        }
    }
    MethodSpec makeSwaggerConfiguration(){
        return null;
    }
    public JavaFile getConfiguration(String className, String packageName){
        return JavaFile.builder(packageName,makeConfigurationClass(className)).build();
    }
    TypeSpec makeConfigurationClass(String className){
        TypeSpec.Builder repository = TypeSpec.classBuilder(className)
                .addAnnotation(CONFIGURATION_ANNOTATION_CLASS)
                .addModifiers(Modifier.PUBLIC).addMethod(makeSwaggerConfiguration());
        restsJson.forEach(rest-> repository.addMethod(rest.getConnectionBean()));
        return repository.build();
    }


}
