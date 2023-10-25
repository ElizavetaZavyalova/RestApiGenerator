package org.example;

import lombok.extern.java.Log;
import org.example.analize.requests.BaseRequest;
import org.example.analize.requests.GetRequest;
import org.example.analize.sqlRequest.SelectRequest;
import org.jooq.SQLDialect;
import org.junit.jupiter.api.Test;

@Log
class RequestAnalyzeTest {
    //static BaseRequestsAnalyze baseRequestsAnalyze=new BaseRequestsAnalyze();
    @Test
    void testSplitFunkTableFieldId(){

        String request= "id:#{name}:%100?{@id}&age<{age}/table:={name}:={age}";
        BaseRequest baseRequest=new GetRequest(request, SQLDialect.MYSQL);
        SelectRequest selectRequest=(SelectRequest) baseRequest.getInterpret();
        log.info(selectRequest.interpret());

        log.info("debug");

    }


}
