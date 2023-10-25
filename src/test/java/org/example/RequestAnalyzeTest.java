package org.example;

import lombok.extern.java.Log;
import org.example.analize.Variables;
import org.example.analize.requests.BaseRequest;
import org.example.analize.requests.GetRequest;
import org.example.analize.sqlRequest.SelectRequest;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

@Log
class RequestAnalyzeTest {
    //static BaseRequestsAnalyze baseRequestsAnalyze=new BaseRequestsAnalyze();
    @Test
    void testSplitFunkTableFieldId(){
        String request= "id:#{name}:%100?{@id}&age<{@age}/age/hert/table:={name}:={age}?{value}&age>={age}";
        BaseRequest baseRequest=new GetRequest(request, SQLDialect.POSTGRES);
        SelectRequest selectRequest=(SelectRequest) baseRequest.getInterpret();
        log.info(selectRequest.interpret());
        var dsl=baseRequest.getDslContext();
        log.info(selectRequest.makeSelect(dsl).getSQL());
    }


}
