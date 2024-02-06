package org.example.processors;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.annotations.RestApiGenerator;
import org.example.read_json.ParseJson;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes("org.example.processors.annotations.RestApiGenerator")
@AutoService(Processor.class)
@Slf4j
public class MainProcessor extends BaseProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        WriterOfDiagnosticLogs.initClass("MainProcessor", AST.instance().getMessager());
    }
    @Deprecated
    void writePath(){
        var ast=AST.instance();
        for(var location:StandardLocation.values() ) {
            log.info("------" + location.getName() + "------------------------------------");
            try {
                String str = ast.getFiler().getResource(location, "", "hello").getName();
                log.info("------" + str + "------------------------------------");
            } catch (IOException e) {


                //log.info("------" + resource + "------------------------------------");
            }
        }
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for(var element:roundEnv.getElementsAnnotatedWith(RestApiGenerator.class)){
            RestApiGenerator restApiGenerator=element.getAnnotation(RestApiGenerator.class);
            String jsonFileName=restApiGenerator.jsonPath();
            String packageName=restApiGenerator.packagePath().isEmpty()?
                    (new File(jsonFileName).getName().replace(".json","")):
                    restApiGenerator.packagePath();
            ParseJson parseJson=new ParseJson(jsonFileName);
        }
        writePath();

        return true;//стоп
    }

}