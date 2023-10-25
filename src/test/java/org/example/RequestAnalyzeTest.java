package org.example;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

@Log
class RequestAnalyzeTest {
    //static BaseRequestsAnalyze baseRequestsAnalyze=new BaseRequestsAnalyze();
    @Test
    void testSplitFunkTableFieldId(){
        String request="table/name/address?@5&{name}/age:date-m:-{@current}";
        String test="=Year";
        String[] input=test.split("=");
        log.info("debug");
       // BaseRequestsAnalyze baseRequestsAnalyze;
        //String[] strings=baseRequestsAnalyze.splitString(request);
        //List<TypeAnalyze> type=baseRequestsAnalyze.makeTypeAnalyzes(strings);
    }


}
