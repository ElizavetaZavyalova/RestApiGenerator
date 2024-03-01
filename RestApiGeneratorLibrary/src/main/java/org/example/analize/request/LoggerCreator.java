package org.example.analize.request;

import com.squareup.javapoet.MethodSpec;
import org.example.read_json.rest_controller_json.endpoint.RequestType;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.LOG_LEVEL;
import static org.example.read_json.rest_controller_json.JsonKeyWords.ApplicationProperties.showSql;

public record LoggerCreator() {
    public  static MethodSpec.Builder createLog(MethodSpec.Builder method, RequestType forType,String foVar){
       return method.beginControlFlow("if (" + showSql + ")")
                .addStatement(LOG_NAME + ".log($T." + LOG_LEVE_NAME + ", $S+" + foVar + ".getSQL()+$S)", LOG_LEVEL, forType.getTypeColor(),RequestType.getRESET())
                .endControlFlow();
    }

}
