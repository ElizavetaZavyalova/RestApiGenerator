package org.example;

import com.google.auto.service.AutoService;
import org.example.Package.RenamePackageProcessor;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("org.example.RestApiGenerator")
@SupportedSourceVersion(SourceVersion.RELEASE_18)
@AutoService(Processor.class)
public class MainProcessor extends BaseProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        WriterOfDiagnosticLogs.initClass("MainProcessor", ast.getMessager());
    }
    /**
     * Process AST
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ast.setRoundEnvironment(roundEnv);
        RenamePackageProcessor.renamePackage(ast);
        return true;//стоп
    }
}