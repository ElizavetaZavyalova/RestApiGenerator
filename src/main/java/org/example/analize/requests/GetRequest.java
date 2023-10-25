package org.example.analize.requests;

import org.example.analize.connections.ConditionInterpret;
import org.jooq.Condition;
import org.jooq.SQLDialect;

import javax.annotation.meta.When;

public class GetRequest extends BaseRequest{

    GetRequest(String request, SQLDialect dialect) {
        super(request, dialect);
    }
    protected  ConditionInterpret conditionInterpret;
    String tableName;
    String[]fields;

    @Override
    protected void parse(String request) {
        String[]input=request.split("\\?");
        if(input.length==2){

        }



    }
    void parseFields(String tableName){

    }

}
