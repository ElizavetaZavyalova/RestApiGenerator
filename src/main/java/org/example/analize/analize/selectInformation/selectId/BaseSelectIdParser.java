package org.example.analize.analize.selectInformation.selectId;

import lombok.Getter;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.selectInformation.BaseSelectFieldsParser;
import org.jooq.DSLContext;

public abstract class BaseSelectIdParser<Select> extends BaseSelectFieldsParser<Select> {
    String id = "id";
    String idNext = null;
    protected static class MAX_MIN{
        @Getter
        static protected final int MAX = 1;
        @Getter
        static protected final int NO=0;
        @Getter
        static protected final int MIN=-1;
    }
    protected  int maxMin=MAX_MIN.NO;

    protected BaseSelectIdParser(String request, BaseVariables variables) {
        super(request, variables);
    }
    @Override
    public String getIdIn() {
        return idNext;
    }
    @Override
    protected boolean add(String port, BaseVariables variables) {
        if (Type.isId(port)) {
            id = addField(port, variables);
            return true;
        } else if (Type.isIdNext(port)) {
            idNext = addField(port, variables);
            return true;
        }
        if(Type.isMax(port)){
            maxMin=MAX_MIN.getMAX();
        }
        else if(Type.isMin(port)){
            maxMin=MAX_MIN.getMIN();
        }
        return false;
    }
}
