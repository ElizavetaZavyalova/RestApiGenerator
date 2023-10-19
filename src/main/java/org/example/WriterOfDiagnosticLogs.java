package org.example;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
@Slf4j
public record WriterOfDiagnosticLogs() {
    public  static void initClass(String className, Messager messager){
        messager.printMessage(Diagnostic.Kind.NOTE, "========="+className +" was init =========");
        log.debug("========="+className +" was init =========");
    }
}
