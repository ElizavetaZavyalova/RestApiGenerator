package org.example.analize.rewrite.address.select;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.select_table.BaseSelectFieldsParser;
import org.example.analize.rewrite.address.select_table.select_id.BaseSelectIdParser;
import org.example.analize.rewrite.address.select_table.select_id.DSLSelectId;
import org.example.analize.rewrite.address.where.BaseWhereParser;
import org.example.analize.rewrite.address.where.DSLWhere;
import org.jooq.*;

@Slf4j
public class DSLSelect extends BaseSelectParser<Condition, Select, Table, Field>{

    public DSLSelect(String request, Variables variables, BaseSelectParser<Condition, Select, Table, Field> selectPrevious) {
        super(request, variables, selectPrevious);
        Debug.debug(log,"DSLSelect");
       // this.dslContext=dslContext;
    }

    @Override
    public Select interpret() {
        Debug.debug(log,"interpret");
        if(selectFields!=null) {
            Debug.debug(log,"interpret selectId");
            SelectJoinStep selectJoinStep = ((SelectJoinStep) selectFields.interpret());
            if (where != null) {
                Debug.debug(log," interpret selectId where");
                return  selectJoinStep.where(where.interpret());
            }
            return selectJoinStep;
        }
        return null;//TODO exeption
    }



    @Override
    BaseSelectFieldsParser<Select, Table, Field> makeSelect(String request, Variables variables) {
        Debug.debug(log,"makeSelect request:",request);
        if(request.isEmpty()){
            return null;
        }
        return new DSLSelectId(request, variables);
    }

    @Override
    BaseWhereParser<Condition, Select, Table, Field> makeBaseWhereParser(String request, Variables variables, BaseSelectParser<Condition, Select, Table, Field> selectPrevious) {
        Debug.debug(log,"makeBaseWhereParser request:",request);
        if(request.isEmpty()){
            return null;
        }
       return new DSLWhere(request,variables,selectPrevious,getFieldIn(),getTable());
    }
}
