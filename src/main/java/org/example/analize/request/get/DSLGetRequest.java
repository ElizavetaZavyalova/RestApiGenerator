package org.example.analize.request.get;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.address.address.BaseAddress;
import org.example.analize.address.select.BaseSelectParser;
import org.example.analize.request.get.select.DSLSelectOfGet;
import org.example.analize.address.address.DSLAddress;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Select;
import org.jooq.Table;
@Slf4j
public class DSLGetRequest extends BaseGetRequest<Select, Condition, Table, Field,Select> {
    protected DSLGetRequest(String request) {
        super(request);
        Debug.debug(log,"DSLGetRequest request:",request);
    }

    @Override
    public Select interpret() {
        Debug.debug(log,"interpret");
        return  getResult.interpret();
    }

    @Override
    protected BaseAddress<Condition, Select, Table, Field> makeAddress(String[] request) {
        Debug.debug(log,"makeAddress request:",request);
        return new DSLAddress(request,variables);
    }

    @Override
    protected BaseSelectParser<Condition, Select, Table, Field> makeGetResult(String request) {
        Debug.debug(log,"makeGetResult request:",request);
        return new DSLSelectOfGet(request,variables,address.getAddress());
    }
}
