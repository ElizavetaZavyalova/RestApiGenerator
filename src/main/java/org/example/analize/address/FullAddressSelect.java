package org.example.analize.address;

import lombok.Getter;
import org.example.analize.Variables;
import org.jooq.DSLContext;
import org.jooq.Select;

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
        return portAddressSelect.makeSelect();
    }

    public FullAddressSelect(String[] requests, Variables variables){
        this.portAddressSelect=new PortAddressSelect(requests[0],variables,null);
        for(int port=1;port<requests.length-1; port++){
            this.portAddressSelect=new PortAddressSelect(requests[port],variables,portAddressSelect);
        }
        tableName= portAddressSelect.getTableName();
    }

}
