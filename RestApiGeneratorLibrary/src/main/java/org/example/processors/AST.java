package org.example.processors;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;


@Getter
public class AST {
    @Setter
    static AST ast = null;
    @Setter
    protected RoundEnvironment roundEnvironment;
    protected Messager messager;
    protected ProcessingEnvironment processingEnv;
    protected Filer filer;

    AST(ProcessingEnvironment processingEnv) {
        this.messager = processingEnv.getMessager();
        this.processingEnv = processingEnv;
        this.filer = processingEnv.getFiler();
    }

    public static AST instance() {
        return ast;
    }

}
