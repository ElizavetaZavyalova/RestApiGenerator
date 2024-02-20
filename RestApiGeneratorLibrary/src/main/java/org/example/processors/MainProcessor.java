package org.example.processors;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.annotations.RestApiGenerator;
import org.example.processors.code_gen.GeneratingCode;
import org.example.processors.code_gen.GeneratorOfCode;
import org.example.processors.code_gen.GeneratorOfCode.LoggerColor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import java.util.Set;

@SupportedAnnotationTypes("org.example.processors.annotations.RestApiGenerator")
@AutoService(Processor.class)
@Slf4j
public class MainProcessor extends BaseProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(RestApiGenerator.class)) {
            GeneratingCode generatingCode = new GeneratorOfCode(element);
            generatingCode.generate();
        }
        log.info(LoggerColor.BRIGHT_CYAN+"Compiling....."+LoggerColor.RESET);
        return true;
    }

}