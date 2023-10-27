package org.example.analize.remove.requests;

import org.jooq.SQLDialect;

public class DeleteRequest extends BaseRequest{
    DeleteRequest(String request, SQLDialect dialect) {
        super(request, dialect);
    }

    @Override
    protected void parse(String request) {

    }

}
