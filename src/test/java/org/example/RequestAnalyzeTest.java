package org.example;

import lombok.extern.java.Log;
import org.example.analize.remove.requests.BaseRequest;
import org.example.analize.remove.requests.GetRequest;
import org.example.analize.remove.sqlRequest.SelectRequest;
import org.jooq.SQLDialect;
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
        var res= dsl.insertInto(DSL.table("table")).defaultValues().returning(DSL.field("id"));
        //log.info( dsl.insertInto(DSL.table("table"))..returning(DSL.field("id")).getSQL());
        log.info(selectRequest.makeSelect(dsl).getSQL());
    }


}
