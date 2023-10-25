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

        String request= "table:&{id1}:|{rev}:%10:#value?{name}&value!={@value}/name:>/age:</year:={year1}:%10?{myname}";
        BaseRequest baseRequest=new GetRequest(request, SQLDialect.MYSQL);
        SelectRequest selectRequest=(SelectRequest) baseRequest.getInterpret();
        log.info(selectRequest.interpret());

        log.info("debug");
       // BaseRequestsAnalyze baseRequestsAnalyze;
        //String[] strings=baseRequestsAnalyze.splitString(request);
        //List<TypeAnalyze> type=baseRequestsAnalyze.makeTypeAnalyzes(strings);
    }


}
