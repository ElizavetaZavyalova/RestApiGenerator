package org.example.processors;

import com.google.auto.service.AutoService;
import org.example.processors.Package.RenamePackageProcessor;

import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("org.example.processors.RestApiGenerator")
@AutoService(Processor.class)
public class MainProcessor extends BaseProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        WriterOfDiagnosticLogs.initClass("MainProcessor", AST.instance().getMessager());
    }
    /**
     * Process AST
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        AST.instance().setRoundEnvironment(roundEnv);

        return true;//стоп
    }

}