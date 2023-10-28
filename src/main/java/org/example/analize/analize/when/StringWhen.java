package org.example.analize.analize.when;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.condition.BaseConditionParser;
import org.example.analize.analize.condition.StringCondition;
import org.example.analize.analize.select.BaseSelectParser;

@Slf4j
public class StringWhen extends BaseWhenParser<String, String> {
    public StringWhen(String request, BaseVariables variables,
                      BaseSelectParser<String, String> selectPrevious, String idIn) {
        super(request, variables, selectPrevious, idIn);
    }
    public StringWhen(String request, BaseVariables variables, String idIn) {
        super(request, variables, idIn);
    }

    @Override
    public String interpret() {
        StringBuilder stringBuilder = new StringBuilder();
        if (condition != null) {
            log.debug("StringWHEN:interpret condition!= null");
            stringBuilder.append(condition.interpret());
            if (select != null) {
                log.debug("StringWHEN:interpret select!= null");
                stringBuilder.append(".and(DSL.field(").append(idIn)
                        .append(").in(").append(select.interpret()).append("))");
            }
            return debug.debugString("StringWhen", stringBuilder.toString());
        }
        if (select != null) {
            log.debug("StringWHEN:interpret condition null");
            stringBuilder.append("DSL.field(").append(idIn)
                    .append(").in(").append(select.interpret()).append(")");
            return debug.debugString("StringWhen", stringBuilder.toString());
        }
        log.debug("StringWHEN:interpret all null");
        return null;
    }

    @Override
    void setIdIn(String idIn) {
        log.debug("setIdIn:" + idIn);
        if (idIn == null && select != null) {
            idIn = BaseVariables.make.makeVariableFromString(select.getTableName()) + "Id";
            idIn = BaseVariables.make.makeOnlyString(idIn);
        }
        log.debug("setIdIn:" + idIn);
        this.idIn = idIn;
    }

    @Override
    BaseConditionParser<String> makeCondition(String request, BaseVariables variables) {
        log.debug("makeCondition :" + request);
        return new StringCondition(request, variables);
    }
}
