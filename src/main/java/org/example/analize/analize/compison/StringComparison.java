package org.example.analize.analize.compison;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.jooq.impl.DSL;

@Slf4j
public class StringComparison extends BaseComparisonParser<String>{
    public StringComparison(String request, BaseVariables variables) {
        super(request, variables);
    }
    StringBuilder makeField(){
        return new StringBuilder().append("DSL.field(").append(field).append(")");
    }

    @Override
    public String interpret() {
        if(log.isDebugEnabled()){
            log.debug("StringComparison interpret operation:"+operation+" field:"+field+" value:"+value);
        }
        switch (operation){
            case LIKE -> {
                return debug.debugString("StringComparison interpret",
                        makeField().append(".like(").append(value).append(")").toString());
            }
            case NOT_LIKE -> {
                return debug.debugString("StringComparison interpret",
                        makeField().append(".notLike(").append(value).append(")").toString());
            }
            case NOT_EQUAL -> {
                return debug.debugString("StringComparison interpret",
                        makeField().append(".notEqual(").append(value).append(")").toString());
            }
            case LESS_THEN -> {
                return debug.debugString("StringComparison interpret",
                        makeField().append(".lessThan(").append(value).append(")").toString());
            }
            case GREATER_THEN -> {
                return debug.debugString("StringComparison interpret",
                        makeField().append(".greaterThan(").append(value).append(")").toString());
            }
            case GREATER_OR_EQUAL -> {
                return debug.debugString("StringComparison interpret",
                        makeField().append(".greaterOrEqual(").append(value).append(")").toString());
            }
            case LESS_OR_EQUAL -> {
                return debug.debugString("StringComparison interpret",
                        makeField().append(".lessOrEqual(").append(value).append(")").toString());

            }
        }
        return debug.debugString("StringComparison interpret",
                makeField().append(".eq(").append(value).append(")").toString());
    }

    @Override
    String addValue(String value, BaseVariables baseVariables) {
        if(BaseVariables.make.isVariable(value)){
            BaseVariables.make.TYPE type=BaseVariables.make.makeType(value);
            baseVariables.addVariable(BaseVariables.make.makeVariable(value),type);
        }//TODO LIKE NOT_LIKE
        return BaseVariables.make.makeWriteVariable(value);
    }

    @Override
    String addField(String value, BaseVariables baseVariables,boolean isFromVariable) {
        if(isFromVariable){
            return BaseVariables.make.makeWriteVariable(BaseVariables.make.makeVariable(value));
        }
        if(BaseVariables.make.isVariable(value)){
            BaseVariables.make.TYPE type=BaseVariables.make.TYPE.STRING;
            baseVariables.addVariable(BaseVariables.make.makeVariable(value),type);
        }
        return BaseVariables.make.makeWriteVariable(value);
    }
}
