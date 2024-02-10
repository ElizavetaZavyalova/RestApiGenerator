package org.example.processors.code_gen;


import com.squareup.javapoet.JavaFile;
import org.example.processors.AST;
import org.example.processors.annotations.RestApiGenerator;
import org.example.read_json.ParseJson;
import org.example.read_json.rest_controller_json.RestJson;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Optional;

import static org.example.processors.code_gen.GeneratorOfCode.Expansion.*;


public class GeneratorOfCode implements GeneratingCode{
    String jsonFileName;
    String packageName;
    record Expansion(){
        public static final String JSON=".json";
        public static final String REPOSITORY_PACKAGE=".repository";
        public static final String REPOSITORY="Repository";
        public static final String CONTROLLER="Controller";
        public static final String CONTROLLER_PACKAGE=".controller";
        public static final String CONFIG="ConfigRest";
        public static final String CONFIG_PACKAGE=".config";
    }
    ParseJson parseJson;

    @Override
    public void generate() {
        for(RestJson rest:parseJson.getRestsJson()){
            generateController(rest);
            generateRepository(rest);
        }

    }
    void generateController(RestJson rest) {
        String className= rest.getLocationName()+CONTROLLER;
        String controllerPackage=packageName+CONTROLLER_PACKAGE;
        String location= controllerPackage+"."+className;

        String repositoryPackage=packageName+REPOSITORY_PACKAGE;
        String classRepository= rest.getLocationName()+REPOSITORY;

        JavaFile file=rest.getJavaController(className,controllerPackage,classRepository,repositoryPackage);
        writeClass(location,file);
    }
    void writeClass(String location, JavaFile file){
        try {
             JavaFileObject javaFile=AST.instance().getFiler().createSourceFile(location);
             BufferedWriter  bufferedWriter = new BufferedWriter(javaFile.openWriter());
             bufferedWriter.append(file.toString());
             bufferedWriter.close();
        } catch (IOException e) {
             //TODO stopCompile
        }
    }
    void generateRepository(RestJson rest){
        String className= rest.getLocationName()+REPOSITORY;
        String repositoryPackage=packageName+REPOSITORY_PACKAGE;
        String location= repositoryPackage+"."+className;

        JavaFile file=rest.getJavaRepository(className,repositoryPackage);
        writeClass(location,file);
    }
    void generateBeans(RestJson rest){
        String className= rest.getLocationName()+REPOSITORY;
        String controllerPackage=packageName+REPOSITORY_PACKAGE;
        String location= controllerPackage+"."+className;
        JavaFile file=rest.getJavaRepository(className,controllerPackage);
        writeClass(location,file);
    }

   public GeneratorOfCode(Element element){
        RestApiGenerator restApiGenerator=element.getAnnotation(RestApiGenerator.class);
        jsonFileName=restApiGenerator.jsonPath();
        parseJson=new ParseJson(jsonFileName);
        packageName= Optional.ofNullable(element.getEnclosingElement().getSimpleName().toString()).orElse("");
    }
}
