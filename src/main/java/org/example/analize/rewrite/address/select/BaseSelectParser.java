package org.example.analize.rewrite.address.select;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.premetive.field.in.BaseFieldIn;
import org.example.analize.rewrite.address.premetive.table.BaseTable;
import org.example.analize.rewrite.address.select_table.BaseSelectFieldsParser;
import org.example.analize.rewrite.address.select_table.select_id.BaseSelectIdParser;
import org.example.analize.rewrite.address.where.BaseWhereParser;
import org.example.analize.rewrite.ident.Ident;

@Slf4j
public abstract class BaseSelectParser<Condition, Select,Table,Field> implements Interpretation<Select> {
    BaseWhereParser<Condition, Select,Table,Field> where;
    BaseSelectFieldsParser<Select,Table,Field> selectFields;

    record PORT() {
        static final int SELECT = 0;
        static final int WHERE = 1;
        static final int PORT_CONT = 2;

    }
    @Override
    public String requestInterpret(){
         if(where!=null){
             return selectFields.requestInterpret()+where.requestInterpret();
         }
        return selectFields.requestInterpret();
    }

    public BaseTable<Table> getTable(){
        Debug.debug(log," getTable");
        if(selectFields!=null) {
            Debug.debug(log," getTable selectId!=null");
            return selectFields.getTable();
        }
        Debug.debug(log," getTable selectId==null");
        return null;
    }
    public BaseFieldIn<Field,Table> getFieldIn(){
        Debug.debug(log,"getFieldIn");
        if(selectFields!=null) {
            Debug.debug(log,"getFieldIn selectId!=null");
            return selectFields.getIdNext();
        }
        Debug.debug(log,"getFieldIn selectId==null");
        return null;
    }

    BaseSelectParser(String request, Variables variables,
                     BaseSelectParser<Condition, Select,Table,Field> selectPrevious) {
        Debug.debug(log," BaseSelectParser request is:",request);
        String[] input = request.split(Ident.REGEXP.REGEX_QUESTION_NOT_AFTER_SLASH);
        if (input.length == PORT.PORT_CONT) {
            this.selectFields = makeSelect(input[PORT.SELECT], variables);
            Debug.debug(log," BaseSelectParser select is:", input[PORT.SELECT]);
            this.where = makeBaseWhereParser(input[PORT.WHERE], variables, selectPrevious);
            Debug.debug(log," BaseSelectParser where is:" , input[PORT.WHERE]);
            return;
        }
        this.selectFields = makeSelect(request, variables);
        this.where = makeBaseWhereParser("", variables, selectPrevious);
    }

    abstract BaseSelectFieldsParser<Select,Table,Field> makeSelect(String request, Variables variables);

    abstract BaseWhereParser<Condition, Select,Table,Field> makeBaseWhereParser
            (String request, Variables variables,
             BaseSelectParser<Condition, Select,Table,Field> selectPrevious);

}
