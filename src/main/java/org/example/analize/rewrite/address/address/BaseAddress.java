package org.example.analize.rewrite.address.address;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.premetive.field.in.BaseFieldIn;
import org.example.analize.rewrite.address.premetive.table.BaseTable;
import org.example.analize.rewrite.address.select.BaseSelectParser;

@Slf4j
public abstract class BaseAddress<Condition, Select, Table, Field> implements Interpretation<Select> {
    BaseSelectParser<Condition, Select, Table, Field> address = null;

    record  TYPE() {
        static final int ONLY_REQUEST = 1;
        static final int FIRST_PORT_ADDRESS = 0;
        static final int SECOND_PORT_ADDRESS = 1;

        static int lastPortAddress(int count) {
            return count - 1;
        }
    }
    @Override
    public String requestInterpret(){
        if(address!=null){
            return "/"+address.requestInterpret();
        }
        return "";

    }
    BaseAddress(String[] request, Variables variables) {
        Debug.debug(log, "BaseAddress requests:", request);
        if (request.length > TYPE.ONLY_REQUEST) {
            Debug.debug(log, "BaseAddress request:", request[TYPE.FIRST_PORT_ADDRESS]);
            address = makeAddress(request[TYPE.FIRST_PORT_ADDRESS], variables, null);
            for (int port = TYPE.SECOND_PORT_ADDRESS; port < TYPE.lastPortAddress(request.length); port++) {
                Debug.debug(log, "BaseAddress request:", request[port]);
                address = makeAddress(request[port], variables, address);
            }
        }
    }
    abstract BaseSelectParser<Condition, Select, Table, Field> makeAddress(String request,Variables variables, BaseSelectParser<Condition, Select, Table, Field> address);
}
