package org.example.processors.code_gen;


import com.squareup.javapoet.JavaFile;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.AST;
import org.example.processors.annotations.RestApiGenerator;
import org.example.read_json.ParseJson;
import org.example.read_json.rest_controller_json.RestJson;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Optional;

import static org.example.processors.code_gen.GeneratorOfCode.Expansion.*;
import static org.example.processors.code_gen.GeneratorOfCode.LoggerColor.*;

@Slf4j
public class GeneratorOfCode implements GeneratingCode {
    String jsonFileName;
    String packageName;

    record Expansion() {
        public static final String REPOSITORY_PACKAGE = ".repository";
        public static final String REPOSITORY = "Repository";
        public static final String CONTROLLER = "Controller";
        public static final String CONTROLLER_PACKAGE = ".controller";
        public static final String CONFIG = "ConfigRest";
        public static final String CONFIG_PACKAGE = ".config";
    }
    public record LoggerColor(){
        public static final String RESET = "\u001B[0m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String PURPLE = "\u001B[35m";
        public static final String BRIGHT_CYAN = "\u001B[96m";
        public static final String LIGHT_BLUE = "\u001B[94m";
        public static final String LIGHT_GREEN = "\u001B[92m";
        public static final String LIGHT_PURPLE = "\u001B[95m";

    }

    ParseJson parseJson;

    @Override
    public void generate() {
        try {
            parseJson.generate();
            parseJson.getRestsJson().forEach(rest -> {
                generateRepository(rest);
                generateController(rest);
            });
            generateBeans();
        }catch (IllegalArgumentException ex){
            AST.instance().getMessager().printMessage(Diagnostic.Kind.ERROR,ex.getMessage());
        }
    }

    void generateController(RestJson rest) {
        String className = rest.getLocationName() + CONTROLLER;
        String controllerPackage = packageName + CONTROLLER_PACKAGE;
        String location = controllerPackage + "." + className;

        String repositoryPackage = packageName + REPOSITORY_PACKAGE;
        String classRepository = rest.getLocationName() + REPOSITORY;

        JavaFile file = rest.getJavaController(className, controllerPackage, classRepository, repositoryPackage);
        writeClass(location, file);
        log.info(LIGHT_GREEN+"-GENERATE CONTROLLER-"+YELLOW+controllerPackage+"."+GREEN+className+RESET);
    }

    void writeClass(String location, JavaFile file) {
        try {
            JavaFileObject javaFile = AST.instance().getFiler().createSourceFile(location);
            BufferedWriter bufferedWriter = new BufferedWriter(javaFile.openWriter());
            bufferedWriter.append(file.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            log.info(e.getMessage());
            AST.instance().getMessager().printMessage(Diagnostic.Kind.ERROR, "rename:" + location);
        }
    }

    void generateRepository(RestJson rest) {
        String className = rest.getLocationName() + REPOSITORY;
        String repositoryPackage = packageName + REPOSITORY_PACKAGE;
        String location = repositoryPackage + "." + className;
        JavaFile file = rest.getJavaRepository(className, repositoryPackage);
        writeClass(location, file);
        log.info(LIGHT_BLUE+"-GENERATE REPOSITORY-"+YELLOW+ repositoryPackage+"."+BLUE+className+RESET);
    }

    void generateBeans() {
        String className = CONFIG;
        String configPackage = packageName + CONFIG_PACKAGE;
        String location = configPackage + "." + className;
        JavaFile file = parseJson.getConfiguration(className, configPackage);
        writeClass(location, file);
        log.info(LIGHT_PURPLE+"-GENERATE SWAGGER CONFIG-"+YELLOW+configPackage+"."+PURPLE+className+RESET);
    }

    public GeneratorOfCode(Element element) {
        RestApiGenerator restApiGenerator = element.getAnnotation(RestApiGenerator.class);
        jsonFileName = restApiGenerator.jsonPath();
        parseJson = new ParseJson(jsonFileName);
        packageName = Optional.ofNullable(((PackageElement) element.getEnclosingElement()).getQualifiedName().toString()).orElse("");
    }
}
