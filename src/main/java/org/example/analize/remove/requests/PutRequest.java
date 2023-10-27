package org.example.analize.remove.requests;

import org.jooq.SQLDialect;

public class PutRequest extends BaseRequest{
    PutRequest(String request, SQLDialect dialect) {
        super(request, dialect);
    }

    @Override
    protected void parse(String request) {

    }
}
