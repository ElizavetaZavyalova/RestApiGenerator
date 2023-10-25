package org.example.analize.requests;

import lombok.Getter;
import org.example.analize.Variables;
import org.example.analize.address.FullAddressSelect;
import org.example.analize.sqlRequest.SqlInterpret;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public abstract class BaseRequest {
    protected  FullAddressSelect fullAddressSelect;
    @Getter
    protected DSLContext dslContext;
    @Getter
    protected SQLDialect dialect;
    protected Variables variables;
    @Getter
    protected SqlInterpret interpret;
    @Getter
    String request;

    BaseRequest(String request,SQLDialect dialect){
        this.request=request;
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
