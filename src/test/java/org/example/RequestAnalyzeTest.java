package org.example;

import lombok.extern.java.Log;
import org.example.analize.requests.BaseRequestsAnalyze;
import org.example.analize.requests.TypeAnalyze;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

@Log
class RequestAnalyzeTest {
    static BaseRequestsAnalyze baseRequestsAnalyze=new BaseRequestsAnalyze();
    @Test
    void testSplitFunkTableFieldId(){
        String request="table/{id}/next/table/{id}";
        BaseRequestsAnalyze baseRequestsAnalyze;
        //String[] strings=baseRequestsAnalyze.splitString(request);
        //List<TypeAnalyze> type=baseRequestsAnalyze.makeTypeAnalyzes(strings);
    }


}
