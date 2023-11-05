package org.example.analize.rewrite.address.premetive.field.in;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.address.premetive.field.BaseField;
import org.example.analize.rewrite.address.premetive.table.BaseTable;

@Slf4j
public class BuildFieldIn extends BaseFieldIn<String,String>{
    public BuildFieldIn(BaseTable<String> tableIn, BaseField<String, String> field) {
        super(tableIn, field);
        Debug.debug(log," BuildFieldIn");
    }

    @Override
    public String interpret() {
        Debug.debug(log,"interpret");
        if(tableIn==null){
            Debug.debug(log,"interpret tableIn==null");
            return field.interpret();
        }
        return field.getTable().interpret()+".field("+tableIn.getTable()+"+"+ field.getField()+")";
    }
}
