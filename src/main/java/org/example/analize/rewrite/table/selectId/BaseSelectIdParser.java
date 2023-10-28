package org.example.analize.rewrite.table.selectId;

import lombok.Getter;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.premetive.field.BaseField;
import org.example.analize.rewrite.table.BaseSelectFieldsParser;
import org.jooq.Table;

public abstract class BaseSelectIdParser<Select,Table,Field> extends BaseSelectFieldsParser<Select, Table,Field> {
    BaseField<Field> id;
    BaseField<Field> idNext = null;

    protected static class MAX_MIN {
        @Getter
        static final int MAX = 1;
        @Getter
        static final int NO = 0;
        @Getter
        static final int MIN = -1;
    }

    protected int maxMin = MAX_MIN.NO;

    protected BaseSelectIdParser(String request, Variables variables) {
        super(request, variables);
    }

    @Override
    public BaseField<Field> getIdIn() {
        return idNext;
    }
    @Override
    protected boolean add(String port, Variables variables) {
        if (Type.isId(port)) {
            id = addField(port, variables);
            return true;
        } else if (Type.isIdNext(port)) {
            idNext = addField(port, variables);
            return true;
        }
        if (Type.isMax(port)) {
            maxMin = MAX_MIN.getMAX();
        } else if (Type.isMin(port)) {
            maxMin = MAX_MIN.getMIN();
        }
        return false;
    }
}
