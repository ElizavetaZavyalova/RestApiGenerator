package org.example.Package;
import lombok.extern.slf4j.Slf4j;
import org.example.ASTTreeComponents;
import org.example.WriterOfDiagnosticLogs;

@Slf4j
public record RenamePackageProcessor()  {
    public static synchronized boolean renamePackage(ASTTreeComponents ast) {
        WriterOfDiagnosticLogs.initClass("RenamePackageProcessor", ast.getMessager());
        return true;
    }

}
