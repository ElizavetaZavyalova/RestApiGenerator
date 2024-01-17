package org.example.analize.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.Interpretation;
import org.example.analize.Variables;
import org.example.analize.address.address.BaseAddress;
import org.example.analize.ident.Ident;

@Slf4j
public abstract class BaseRequest<Select,Condition,Table,Field,Result> extends Interpretation<Result> {
    @Getter
    protected Variables variables;
    protected BaseAddress<Condition,Select,Table,Field> address;

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
            this.address=makeAddress(input);
        }
        parse(input[TYPE.lastPortAddress(input.length)]);
    }
    protected abstract BaseAddress<Condition,Select,Table,Field> makeAddress(String[] request);
    protected abstract void parse(String request);
}
