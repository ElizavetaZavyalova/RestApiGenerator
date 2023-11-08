package org.example.analize.address.premetive.field.in;


import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.address.premetive.field.BaseField;
import org.example.analize.address.premetive.table.BaseTable;

import org.jooq.Field;
import org.jooq.Table;
@Slf4j
public class DSLFieldIn extends BaseFieldIn<Field, Table>{

    public DSLFieldIn(BaseTable<Table> tableIn, BaseField<Field, Table> field) {
        super(tableIn, field);
        Debug.debug(log,"DSLFieldIn");
    }

    @Override
    public Field interpret() {
        Debug.debug(log,"interpret");
        if(tableIn==null){
            Debug.debug(log,"interpret tableIn==null");
            return field.interpret();
        }
        return field.getTable().interpret().field(tableIn.getTable()+"+"+ field.getField());
    }
}
