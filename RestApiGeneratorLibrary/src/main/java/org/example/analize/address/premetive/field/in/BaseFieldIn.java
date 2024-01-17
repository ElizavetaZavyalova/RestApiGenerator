package org.example.analize.address.premetive.field.in;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Interpretation;
import org.example.analize.address.premetive.field.BaseField;
import org.example.analize.address.premetive.table.BaseTable;
@Slf4j
public abstract class BaseFieldIn<Field,Table> extends Interpretation<Field> {
    BaseTable<Table> tableIn;
    BaseField<Field,Table> field;
    BaseFieldIn(BaseTable<Table> tableIn,BaseField<Field,Table> field){
        Debug.debug(log,"BaseFieldIn");
        this.field=field;
        this.tableIn=tableIn;
    }
    @Override
    public String requestInterpret(){
        if(tableIn==null&&field!=null){
            return  "-"+field.requestInterpret();
        }
        return "";
    }

}
