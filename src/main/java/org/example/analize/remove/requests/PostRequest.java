package org.example.analize.remove.requests;

import org.jooq.SQLDialect;

public class PostRequest extends BaseRequest {
    PostRequest(String request, SQLDialect dialect) {
        super(request, dialect);
    }

    @Override
    protected void parse(String request) {

    }
}
