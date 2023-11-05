package org.example.analize.rewrite.request.get.select_table;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.premetive.field.BaseField;
import org.example.analize.rewrite.address.select_table.BaseSelectFieldsParser;
import org.example.analize.rewrite.ident.Ident;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public  abstract class BaseSelectOfGetParser<Select,Table,Field> extends BaseSelectFieldsParser<Select,Table,Field> {
    protected List<BaseField<Field, Table>> fields=null;
    protected BaseSelectOfGetParser(String request, Variables variables) {
        super(request, variables);
        Debug.debug(log, "BaseSelectOfGetParser request:", request);

    }
    @Override
    public String requestInterpret() {
        Debug.debug(log, "requestInterpret");
        StringBuilder fieldsBuilder=new StringBuilder(table.requestInterpret());
        if(idNext!=null){
            Debug.debug(log, "requestInterpret idNext!=null");
            fieldsBuilder.append("-").append(idNext.interpret());
        }
        if(!fields.isEmpty()){
           // fieldsBuilder.append("/");
            Debug.debug(log, "requestInterpret fields not empty");
            for (BaseField<Field, Table> field:fields) {
                fieldsBuilder.append("-").append(field.requestInterpret());
            }
        }
        return   Debug.debugResult(log,"requestInterpret",fieldsBuilder.toString());
    }

    @Override
    protected boolean add(String port, Variables variables) {
        this.fields=new ArrayList<>();
        Debug.debug(log, "add port:", port);
        if (Ident.isField(port)) {
            fields.add(addField(port, variables));
            return true;
        }
        return false;
    }


}
