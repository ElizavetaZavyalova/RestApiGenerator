package org.example.analize.rewrite.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.remove.Variables;
import org.example.analize.remove.address.FullAddressSelect;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.address.address.BaseAddress;
import org.example.analize.rewrite.ident.Ident;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
@Slf4j
public abstract class BaseRequest<Select,Condition,Table,Field,Result> implements Interpretation<Result> {
    @Getter
    Variables variables;
    BaseAddress<Condition,Select,Table,Field> address;
    record  TYPE() {
        static final int ONLY_REQUEST = 1;

        static int lastPortAddress(int count) {
            return count - 1;
        }
    }

    protected BaseRequest(String request){
        this.variables=new Variables();
        String[] input=request.split(Ident.REGEXP.REGEX_BACK_SLASH_NOT_AFTER_SLASH);
        if(input.length>TYPE.ONLY_REQUEST){
            this.address=makeAddress(input,variables);
        }
        parse(input[TYPE.lastPortAddress(input.length)]);
    }
    protected abstract BaseAddress<Condition,Select,Table,Field> makeAddress(String[] request, Variables variables);
    protected abstract void parse(String request);
}
