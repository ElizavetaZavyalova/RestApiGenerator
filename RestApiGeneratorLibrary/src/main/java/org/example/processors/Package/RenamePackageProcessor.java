package org.example.processors.Package;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record RenamePackageProcessor()  {
    public static synchronized boolean renamePackage( ) {
       // WriterOfDiagnosticLogs.initClass("RenamePackageProcessor", ast.getMessager());
        return true;
    }

}
