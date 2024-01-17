package org.example.analize.address.where;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Variables;
import org.example.analize.address.condition.BaseConditionParser;
import org.example.analize.address.condition.DSLCondition;
import org.example.analize.address.premetive.field.DSLField;
import org.example.analize.address.premetive.field.in.BaseFieldIn;
import org.example.analize.address.premetive.field.in.DSLFieldIn;
import org.example.analize.address.premetive.table.BaseTable;
import org.example.analize.address.select.BaseSelectParser;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Select;
import org.jooq.Table;
@Slf4j
public class DSLWhere extends BaseWhereParser<Condition, Select, Table, Field> {
    public DSLWhere(String request, Variables variables,
                    BaseSelectParser<Condition, Select, Table, Field> selectPrevious,
                    BaseFieldIn<Field, Table> idIn, BaseTable<Table> table) {
        super(request, variables, selectPrevious, idIn, table);
        Debug.debug(log,"DSLWhere");
    }

    @Override
    BaseFieldIn<Field, Table> makeFieldIn(BaseTable<Table> tableIn, String name, BaseTable<Table> table, Variables variables) {
        Debug.debug(log,"makeFieldIn name:",name," table:",table.getTable());
        return new DSLFieldIn(tableIn,new DSLField(name,table,variables));
    }

    DSLWhere(String request, Variables variables, BaseFieldIn<Field,Table> idIn, BaseTable<Table> tableName) {
        super(request, variables, idIn, tableName);
        Debug.debug(log,"DSLWhere request:",request);
    }

    @Override
    public Condition interpret() {
        if (condition != null && select != null) {
            Debug.debug(log,"interpret no null");
            return condition.interpret().and(idIn.interpret().in(select.interpret()));
        } else if (condition != null) {
            Debug.debug(log,"interpret select null");
            return condition.interpret();
        } else if (select != null) {
            Debug.debug(log,"interpret condition null");
            return idIn.interpret().in(select.interpret());
        }
        Debug.debug(log,"interpret all null");
        return null;
    }



    @Override
    BaseConditionParser<Condition,Table> makeCondition(String request, Variables variables, BaseTable<Table> table) {
        Debug.debug(log,"makeCondition:" , request);
        return new DSLCondition(request, variables,table);
    }
}
