package org.example.analize.address.select_table;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.example.analize.address.premetive.field.in.BaseFieldIn;
import org.example.analize.address.premetive.table.BaseTable;
import org.example.analize.ident.Ident;
import org.example.analize.Debug;
import org.example.analize.Interpretation;
import org.example.analize.Variables;
import org.example.analize.address.premetive.field.BaseField;

@Slf4j
public abstract class BaseSelectFieldsParser<Select,Table,Field> implements Interpretation<Select> {

    @Getter
    protected BaseTable<Table> table;
    @Getter
    protected BaseFieldIn<Field, Table> idNext = null;
    protected BaseSelectFieldsParser(String request, Variables variables) {
        Debug.debug(log,"BaseSelectFieldsParser request:",request);
        String[] input = request.split(Ident.REGEXP.REGEX_COLON_NOT_AFTER_SLASH);
        for(String port : input){
            if (Ident.isTable(port)) {
                Debug.debug(log,"BaseSelectFieldsParse table:",port);
                table = addTable(makeValue(port), variables);
                break;
            }
        }
        for (String port : input) {
            Debug.debug(log,"BaseSelectFieldsParse port:",port);
            if (Ident.isIdNext(port)) {
                idNext = addFieldIn(makeValue(port), variables);
            }
            else if(!add(port, variables)){
                Debug.debug(log,"BaseSelectFieldsParse port:",port);
                //Todo exeption
            }

        }
    }
    public static String makeValue(String value) {
        return Debug.debugResult(log,"makeValue", value,
                value.substring(1));
    }
    protected abstract BaseFieldIn<Field, Table> addFieldIn(String port, Variables variables);


    protected abstract boolean add(String port, Variables variables);

    protected abstract BaseField<Field,Table> addField(String field, Variables variables);

    protected abstract BaseTable<Table> addTable(String table, Variables variables);


}
