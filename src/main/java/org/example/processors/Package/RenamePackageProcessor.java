package org.example.processors.Package;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.ASTTreeComponents;
import org.example.processors.WriterOfDiagnosticLogs;

@Slf4j
public record RenamePackageProcessor()  {
    public static synchronized boolean renamePackage(ASTTreeComponents ast) {
        WriterOfDiagnosticLogs.initClass("RenamePackageProcessor", ast.getMessager());
        return true;
    }

}
