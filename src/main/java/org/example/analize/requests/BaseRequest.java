package org.example.analize.requests;

import org.example.analize.Variables;
import org.example.analize.address.FullAddressSelect;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public abstract class BaseRequest {
    protected  FullAddressSelect fullAddressSelect;
    protected DSLContext dslContext;
    protected SQLDialect dialect;
    protected Variables variables;

    BaseRequest(String request,SQLDialect dialect){
        this.variables=new Variables();
        this.dialect=dialect;
        String[] input=request.split("/");
        dslContext= DSL.using(dialect);
        if(input.length>1){
            this.fullAddressSelect=new FullAddressSelect(input,variables);
        }
        parse(input[input.length-1]);
    }
    protected abstract void parse(String request);

}
