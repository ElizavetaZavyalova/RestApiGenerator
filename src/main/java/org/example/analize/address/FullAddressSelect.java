package org.example.analize.address;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.Variables;
import org.jooq.DSLContext;
import org.jooq.Select;
@Slf4j
public class FullAddressSelect implements SelectInterpret {
    PortAddressSelect portAddressSelect=null;
    @Getter
    String tableName=null;
    @Override
    public Select makeSelect(DSLContext dsl) {
        return portAddressSelect.makeSelect(dsl);
    }

    @Override
    public String makeSelect() {
        String string= portAddressSelect.makeSelect();
        log.debug("interprit:"+string);
        return string;
    }

    public FullAddressSelect(String[] requests, Variables variables){
        this.portAddressSelect=new PortAddressSelect(requests[0],variables,null);
        for(int port=1;port<requests.length-1; port++){
            log.debug("request:"+requests[port]);
            this.portAddressSelect=new PortAddressSelect(requests[port],variables,portAddressSelect);
        }
        tableName= portAddressSelect.getTableName();
    }

}
