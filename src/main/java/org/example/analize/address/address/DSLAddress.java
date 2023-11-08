package org.example.analize.address.address;


import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Variables;
import org.example.analize.address.select.BaseSelectParser;
import org.example.analize.address.select.DSLSelect;
import org.jooq.*;

@Slf4j
public class DSLAddress extends BaseAddress<Condition, Select, Table, Field> {
    public DSLAddress(String[] request, Variables variables) {
        super(request, variables);
        Debug.debug(log, "DSLAddress requests:", request);

    }

    @Override
    public Select interpret() {
        Debug.debug(log, "interpret");
        if (address != null) {
            Debug.debug(log, "interpret address!=null");
            return address.interpret();
        }
        Debug.debug(log, "interpret address==null");
        return null;
    }

    @Override
    BaseSelectParser<Condition, Select, Table, Field> makeAddress(String request, Variables variables, BaseSelectParser<Condition, Select, Table, Field> address) {
        Debug.debug(log, "makeAddress request:", request);
        return new DSLSelect(request, variables, address);

    }
}
