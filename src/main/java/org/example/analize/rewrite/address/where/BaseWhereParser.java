package org.example.analize.rewrite.address.where;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.condition.BaseConditionParser;
import org.example.analize.rewrite.address.premetive.field.in.BaseFieldIn;
import org.example.analize.rewrite.address.premetive.table.BaseTable;
import org.example.analize.rewrite.address.select.BaseSelectParser;

@Slf4j
public abstract class BaseWhereParser<Condition, Select, Table, Field> implements Interpretation<Condition> {
    BaseConditionParser<Condition, Table> condition = null;
    BaseSelectParser<Condition, Select, Table, Field> select = null;
    BaseFieldIn<Field, Table> idIn = null;

    BaseWhereParser(String request, Variables variables,
                    BaseSelectParser<Condition, Select, Table, Field> selectPrevious,
                    BaseFieldIn<Field, Table> idIn, BaseTable<Table> table) {
        Debug.debug(log, "BaseWhereParser:", request);
        this.idIn = makeIdIn(idIn, table, variables);
        this.select = selectPrevious;
        if (!request.isEmpty()) {
            this.condition = makeCondition(request, variables, table);
        }

    }
    @Override
    public String requestInterpret(){
         if(condition!=null){
             return "?"+condition.requestInterpret();
         }
        return "";
    }

    BaseFieldIn<Field, Table> makeIdIn(BaseFieldIn<Field, Table> idIn, BaseTable<Table> table, Variables variables) {
        Debug.debug(log, "makeIdIn");
        if (idIn == null && select != null) {
            Debug.debug(log, "makeIdIn idIn==null select!=null");
            idIn = makeFieldIn(select.getTable(), "id", table, variables);
            //idIn= new DSLField(select.getTableName()+"Id",table, variables);
        }
        Debug.debug(log, "makeIdIn select==null");
        return idIn;
    }

    abstract BaseFieldIn<Field, Table> makeFieldIn(BaseTable<Table> tableIn, String name, BaseTable<Table> table, Variables variables);

    BaseWhereParser(String request, Variables variables, BaseFieldIn<Field, Table> idIn, BaseTable<Table> table) {
        this(request, variables, null, idIn, table);
        Debug.debug(log, "no previous select BaseWhenParser");
    }

    abstract BaseConditionParser<Condition, Table> makeCondition(String request,
                                                                 Variables variables, BaseTable<Table> table);
}
