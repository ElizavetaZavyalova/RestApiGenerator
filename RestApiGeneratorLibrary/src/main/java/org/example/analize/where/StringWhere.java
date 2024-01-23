package org.example.analize.where;

import org.example.analize.condition.StringOperand;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.postfix_infix.Converter;
import org.example.analize.premetive.fieldsCond.StringFieldCondition;
import org.example.analize.premetive.filters.StringFilter;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.stream.Collectors;

public class StringWhere extends BaseWhere<String,String> {
    public StringWhere(String where, String table, Endpoint parent) {
        super(where, table, parent);
    }

    @Override
    public String interpret() {
        if(ports.size()==0) {
            return "";
        }
        if(ports.size()>=2) {
            return "DSL.and(" + ports.stream().map(InterpretationBd::interpret).collect(Collectors.joining(","))+")";
        }
        return ports.get(0).interpret();
    }

    @Override
    public String getParams() {
        return null;
        //TODO make params
    }

    @Override
    Interpretation<String> makeFilter(String filter) {
        return new StringFilter(filter);
        //TODO make filter
    }

    @Override
    Interpretation<String> makePrimitive(String primitive, String table, Endpoint parent) {
        return new StringFieldCondition(primitive,table,parent);
    }

    @Override
    Interpretation<String> makeOperand(Interpretation<String> left, Interpretation<String> right, String operand,String table, Endpoint parent) {
        String def=Converter.isAND(operand)?("DSL.trueCondition()"):("DSL.falseCondition()");
        makeFilterResult(left,def,table,parent);
        makeFilterResult(right,def,table,parent);
        return new StringOperand(left,right,operand);
    }
}
