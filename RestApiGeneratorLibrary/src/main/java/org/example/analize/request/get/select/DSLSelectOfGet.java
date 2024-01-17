package org.example.analize.request.get.select;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Variables;
import org.example.analize.address.select.BaseSelectParser;
import org.example.analize.address.select.DSLSelect;
import org.example.analize.address.select_table.BaseSelectFieldsParser;
import org.example.analize.request.get.select_table.DSLSelectOfGetFields;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Select;
import org.jooq.Table;

@Slf4j
public class DSLSelectOfGet extends DSLSelect {
    public DSLSelectOfGet(String request, Variables variables, BaseSelectParser<Condition, Select, Table, Field> selectPrevious) {
        super(request, variables, selectPrevious);
    }
    @Override
    protected BaseSelectFieldsParser<Select, Table, Field> makeSelect(String request, Variables variables) {
        Debug.debug(log,"makeSelect request:",request);
        if(request.isEmpty()){
            return null;
        }
        return new DSLSelectOfGetFields(request, variables);
    }
}
