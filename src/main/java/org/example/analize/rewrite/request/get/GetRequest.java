package org.example.analize.rewrite.request.get;


import org.example.analize.rewrite.request.BaseRequest;

public abstract class GetRequest<Select,Condition,Table,Field,Result> extends BaseRequest<Select,Condition,Table,Field,Result> {
    protected GetRequest(String request) {
        super(request);
    }
    @Override
    public String requestInterpret() {
        return null;
    }
    @Override
    protected void parse(String request) {
        //\table:NidIn:FName1:FName2:FName3-Name4\{1}
    }
}
