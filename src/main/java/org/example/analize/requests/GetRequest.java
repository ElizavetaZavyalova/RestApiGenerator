package org.example.analize.requests;
import org.example.analize.sqlRequest.SelectRequest;
import org.jooq.SQLDialect;

public class GetRequest extends BaseRequest{

    public GetRequest(String request, SQLDialect dialect) {
        super(request, dialect);
    }


    @Override
    protected void parse(String request) {
        this.interpret=new SelectRequest(request,variables,fullAddressSelect);
    }


}
