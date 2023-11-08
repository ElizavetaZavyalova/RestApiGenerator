package org.example.analize.request.get;


import lombok.extern.slf4j.Slf4j;
import org.example.analize.address.select.BaseSelectParser;
import org.example.analize.Debug;
import org.example.analize.request.BaseRequest;
@Slf4j
public abstract class BaseGetRequest<Select,Condition,Table,Field,Result> extends BaseRequest<Select,Condition,Table,Field,Result> {
    protected  BaseGetRequest(String request) {
        super(request);
        Debug.debug(log,"BaseGetRequest request:",request);
    }
    BaseSelectParser<Condition, Select,Table,Field> getResult;

    @Override
    protected void parse(String request) {
        Debug.debug(log,"parse request",request);
        this.getResult=makeGetResult(request);
    }
    protected abstract BaseSelectParser<Condition, Select,Table,Field> makeGetResult(String request);


    @Override
    public String requestInterpret() {
        return Debug.debugResult(log,"requestInterpret",getResult.requestInterpret());
    }
}
