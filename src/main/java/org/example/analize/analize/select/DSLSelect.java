package org.example.analize.analize.select;

import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.selectInformation.selectId.BaseSelectIdParser;
import org.example.analize.analize.selectInformation.selectId.DSLSelectId;
import org.example.analize.analize.when.BaseWhenParser;
import org.example.analize.analize.when.DSLWhen;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.SelectJoinStep;

public class DSLSelect extends BaseSelectParser<Condition, Select> {
    DSLContext dslContext;

    DSLSelect(String request, BaseVariables variables,
              BaseSelectParser<Condition, Select> selectPrevious, DSLContext dslContext) {
        super(request, variables, selectPrevious);
        this.dslContext = dslContext;

    }

    @Override
    public Select interpret() {
        Condition condition = when.interpret();
        if (condition != null) {
            return ((SelectJoinStep) select.interpret()).where(condition);
        }
        return select.interpret();
    }

    @Override
    public String getTableName() {
        return select.getTable();
    }

    @Override
    BaseSelectIdParser<Select> makeSelect(String request, BaseVariables variables) {
        return new DSLSelectId(request, variables, dslContext);
    }

    @Override
    BaseWhenParser<Condition, Select> makeBaseWhenParser(String request,
                                                         BaseVariables variables,
                                                         BaseSelectParser<Condition, Select> selectPrevious) {
        //select.getIdIn()
        return new DSLWhen(request, variables, selectPrevious, select.getIdIn());
    }
}
