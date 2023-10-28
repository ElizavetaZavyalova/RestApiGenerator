package org.example.analize.rewrite.select;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.table.selectId.BaseSelectIdParser;
import org.example.analize.rewrite.where.BaseWhereParser;

import java.lang.reflect.Field;

@Slf4j
public abstract class BaseSelectParser<Condition, Select,Table,Field> implements Interpretation<Select> {
    BaseWhereParser<Condition, Select,Table,Field> where;
    BaseSelectIdParser<Select,Table,Field> select;

    record PORT() {
        static final int SELECT = 0;
        static final int WHEN = 1;
        static final int PORT_CONT = 2;
    }

    public abstract String getTableName();

    BaseSelectParser(String request, Variables variables,
                     BaseSelectParser<Condition, Select,Table,Field> selectPrevious) {
        log.debug(" BaseSelectParser request is");
        String[] input = request.split("\\?");
        if (input.length == PORT.PORT_CONT) {
            this.select = makeSelect(input[PORT.SELECT], variables);
            log.debug(" BaseSelectParser select is" + input[PORT.SELECT]);
            this.where = makeBaseWhenParser(input[PORT.WHEN], variables, selectPrevious);
            log.debug(" BaseSelectParser when is" + input[PORT.WHEN]);
            return;
        }
        this.select = makeSelect(request, variables);
        this.where = makeBaseWhenParser("", variables, selectPrevious);
    }

    abstract BaseSelectIdParser<Select,Table,Field> makeSelect(String request, Variables variables);

    abstract BaseWhereParser<Condition, Select,Table,Field> makeBaseWhenParser
            (String request, Variables variables,
             BaseSelectParser<Condition, Select,Table,Field> selectPrevious);

}
