package org.example.processors;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
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
    protected JavacTrees trees;
    protected TreeMaker treeMaker;
    protected Names names;
    protected Context context;
    protected ProcessingEnvironment processingEnv;
    protected Filer filer;

    AST(ProcessingEnvironment processingEnv) {
        this.context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        this.processingEnv = processingEnv;
        this.filer = processingEnv.getFiler();

    }

    public static AST instance() {
        return ast;
    }

}
