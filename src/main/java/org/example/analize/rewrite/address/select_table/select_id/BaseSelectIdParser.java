package org.example.analize.rewrite.address.select_table.select_id;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.ident.Ident;
import org.example.analize.rewrite.address.premetive.field.BaseField;
import org.example.analize.rewrite.address.premetive.field.in.BaseFieldIn;
import org.example.analize.rewrite.address.select_table.BaseSelectFieldsParser;

@Slf4j
public abstract class BaseSelectIdParser<Select, Table, Field> extends BaseSelectFieldsParser<Select, Table, Field> {
    BaseField<Field, Table> id;
    protected record COUNT() {

        static final int MAX = 1;

        static final int NO = 0;

        static final int MIN = -1;
    }
    @Override
    public String requestInterpret(){
        StringBuilder result=new StringBuilder().append(table.requestInterpret());
         if(!id.getField().equals("\"Id\"")){
             result.append("-").append(id.interpret());
         }
         if(idNext!=null){
             result.append("-").append(idNext.interpret());
        }
        return result.toString();
    }

    protected int maxMin = COUNT.NO;

    protected BaseSelectIdParser(String request, Variables variables) {
        super(request, variables);
        Debug.debug(log, "BaseSelectIdParser request:", request);
        if (this.id == null) {
            Debug.debug(log, "BaseSelectId id==null request:", request);
            this.id = addField("Id", variables);
        }
    }




    @Override
    protected boolean add(String port, Variables variables) {
        Debug.debug(log, "add port:", port);
        if (Ident.isId(port)) {
            id = addField(port, variables);
            return true;
        }
        if (Ident.isMax(port)) {
            maxMin = COUNT.MAX;
        } else if (Ident.isMin(port)) {
            maxMin = COUNT.MIN;
        }
        return false;
    }
}
