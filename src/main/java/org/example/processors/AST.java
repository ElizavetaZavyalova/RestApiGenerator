package org.example.processors;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

@Getter
public class AST {
    @Setter
    static AST ast=null;
    @Setter
    protected RoundEnvironment roundEnvironment;
    protected Messager messager;   // Used to log during compilation
    protected JavacTrees trees;    // Provides an abstract syntax tree to be processed
    protected TreeMaker treeMaker; // Encapsulates some methods of creating AST nodes
    protected Names names;
    protected Context context;
    AST(ProcessingEnvironment processingEnv){
        this.context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }
    public static AST instance(){
        return ast;
    }

}
