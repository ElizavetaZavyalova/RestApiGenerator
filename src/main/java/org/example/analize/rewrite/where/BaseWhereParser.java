package org.example.analize.rewrite.where;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.condition.BaseConditionParser;
import org.example.analize.rewrite.premetive.field.BaseField;
import org.example.analize.rewrite.select.BaseSelectParser;

@Slf4j
public abstract class BaseWhereParser<Condition, Select,Table,Field> implements Interpretation<Condition> {
    BaseConditionParser<Condition> condition = null;
    BaseSelectParser<Condition, Select,Table,Field> select = null;
    BaseField<Field> idIn = null;
    BaseWhereParser(String request, Variables variables,
                    BaseSelectParser<Condition, Select,Table,Field> selectPrevious,
                    BaseField<Field> idIn, String tableName) {
        log.debug("BaseWhereParser:" + request);
        makeIdIn(idIn,tableName);
        this.select = selectPrevious;
        if (!request.isEmpty()) {
            this.condition = makeCondition(request, variables);
        }

    }
    abstract  BaseField<Field> makeIdIn( BaseField<Field> idIn, String tableName);
    BaseWhereParser(String request, Variables variables,  BaseField<Field> idIn, String tableName) {
        this(request, variables, null, idIn,tableName);
        log.debug("no previous select BaseWhenParser");
    }

    abstract BaseConditionParser<Condition> makeCondition(String request, Variables variables);

}
