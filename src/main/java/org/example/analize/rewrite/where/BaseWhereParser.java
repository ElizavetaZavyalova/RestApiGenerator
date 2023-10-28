package org.example.analize.rewrite.where;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.Interpretation;
import org.example.analize.analize.condition.BaseConditionParser;
import org.example.analize.analize.select.BaseSelectParser;

@Slf4j
public abstract class BaseWhereParser<Condition, Select,Field> implements Interpretation<Condition> {
    BaseConditionParser<Condition> condition = null;
    BaseSelectParser<Condition, Select> select = null;
    Interpretation<Field> idIn = null;

    BaseWhereParser(String request, BaseVariables variables,
                   BaseSelectParser<Condition, Select> selectPrevious, String idIn, String tableName) {
        log.debug("BaseWhereParser:" + request);
        makeIdIn(idIn,tableName);
        this.select = selectPrevious;
        if (!request.isEmpty()) {
            this.condition = makeCondition(request, variables);
        }

    }
    abstract  Interpretation<Field> makeIdIn(String idIn, String tableName);
    BaseWhereParser(String request, BaseVariables variables, String idIn, String tableName) {
        this(request, variables, null, idIn,tableName);
        log.debug("no previous select BaseWhenParser");
    }

    abstract BaseConditionParser<Condition> makeCondition(String request, BaseVariables variables);

}
