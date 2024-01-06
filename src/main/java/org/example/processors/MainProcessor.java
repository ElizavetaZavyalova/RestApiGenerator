package org.example.processors;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.Package.RenamePackageProcessor;

import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

@SupportedAnnotationTypes("org.example.processors.RestApiGenerator")
@AutoService(Processor.class)
@Slf4j
public class MainProcessor extends BaseProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        WriterOfDiagnosticLogs.initClass("MainProcessor", AST.instance().getMessager());
    }
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
    /**
     * Process AST
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        AST.instance().setRoundEnvironment(roundEnv);
        writePath();

        return true;//стоп
    }

}