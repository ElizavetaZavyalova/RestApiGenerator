package org.example.analize.analize.when;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.condition.BaseConditionParser;
import org.example.analize.analize.condition.DSLCondition;
import org.example.analize.analize.select.BaseSelectParser;
import org.jooq.Condition;
import org.jooq.Select;
import org.jooq.impl.DSL;

@Slf4j
public class DSLWhen extends BaseWhenParser<Condition, Select> {
    public DSLWhen(String request, BaseVariables variables, BaseSelectParser<Condition, Select> selectPrevious, String idIn) {
        super(request, variables, selectPrevious, idIn);
    }

    @Override
    void setIdIn(String idIn) {
        log.debug("setIdIn:" + idIn);
        if (idIn == null && select != null) {
            idIn = select.getTableName() + "Id";
        }
        log.debug("setIdIn:" + idIn);
        this.idIn = idIn;
    }

    @Override
    public Condition interpret() {
        if (condition != null && select != null) {
            log.debug("DSLWHEN:interpret no null");
            return condition.interpret().and(DSL.field(idIn).in(select.interpret()));
        } else if (condition != null) {
            log.debug("DSLWHEN:interpret select null");
            return condition.interpret();
        } else if (select != null) {
            log.debug("DSLWHEN:interpret condition null");
            return DSL.field(idIn).in(select.interpret());
        }
        log.debug("DSLWHEN:interpret all null");
        return null;
    }

    @Override
    BaseConditionParser<Condition> makeCondition(String request, BaseVariables variables) {
        log.debug("makeCondition:" + request);
        return new DSLCondition(request, variables);
    }
}
