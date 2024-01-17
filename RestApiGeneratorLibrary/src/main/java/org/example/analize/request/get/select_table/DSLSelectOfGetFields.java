package org.example.analize.request.get.select_table;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Variables;
import org.example.analize.address.premetive.field.BaseField;
import org.example.analize.address.premetive.field.DSLField;
import org.example.analize.address.premetive.field.in.BaseFieldIn;
import org.example.analize.address.premetive.field.in.DSLFieldIn;
import org.example.analize.address.premetive.table.BaseTable;
import org.example.analize.address.premetive.table.DSLTable;
import org.jooq.*;
import org.jooq.impl.DSL;

@Slf4j
public class DSLSelectOfGetFields extends BaseSelectOfGetFields<Select, Table, Field> {
    static DSLContext dslContext= DSL.using(SQLDialect.POSTGRES);
    public DSLSelectOfGetFields(String request, Variables variables) {
        super(request, variables);
        Debug.debug(log,"DSLSelectOfGet request:",request);
    }

    @Override
    public Select interpret() {
        Debug.debug(log,"interpret");
         if(fields.isEmpty()){
             Debug.debug(log,"interpret ALL fields");
             return dslContext.select().from(table.interpret());
         }
         Debug.debug(log,"interpret not All fields");
         return dslContext.select(interpretFields()).from(table.interpret());
    }

    private Field[] interpretFields() {
        Debug.debug(log,"interpretFields");
        Field[] dslFields=new Field[fields.size()];
        int currentFieldIndex=0;
        for (BaseField<Field,Table> field:fields){
            dslFields[currentFieldIndex]= field.interpret();
            currentFieldIndex++;
        }
        return dslFields;
    }



    @Override
    protected BaseFieldIn<Field, Table> addFieldIn(String port, Variables variables) {
        Debug.debug(log,"addFieldIn port:",port);
        return new DSLFieldIn(null,addField(port,variables));
    }

    @Override
    protected BaseField<Field,Table> addField(String field, Variables variables) {
        Debug.debug(log,"addField field:",field);
        return new DSLField(field,table,variables);
    }

    @Override
    protected BaseTable<Table> addTable(String table, Variables variables) {
        Debug.debug(log,"addTable table:",table);
        return new DSLTable(table,variables);
    }
}
