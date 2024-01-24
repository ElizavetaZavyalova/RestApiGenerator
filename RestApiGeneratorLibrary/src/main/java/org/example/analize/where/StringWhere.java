package org.example.analize.where;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.condition.StringOperand;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.postfix_infix.Converter;
import org.example.analize.premetive.fieldsCond.StringFieldCondition;
import org.example.analize.premetive.filters.StringFilter;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.stream.Collectors;

public class StringWhere extends BaseWhere<CodeBlock,String> {
    public StringWhere(String where, String table, Endpoint parent) {
        super(where, table, parent);
    }

    @Override
    public CodeBlock interpret() {
        var block=CodeBlock.builder();
        if(ports.isEmpty()) {
            return block.add("").build();
        }
        return block.add("DSL.and(").add(ports.stream()
                            .map(InterpretationBd::interpret)
                            .reduce((v,h)-> CodeBlock.builder().add(v).add(", ").add(h).build())
                        .orElse(ports.get(0).interpret()))
                    .add(")").build();

    }

    @Override
    public String getParams() {
        return null;
        //TODO make params
    }

    @Override
    Interpretation<CodeBlock> makeFilter(String filter) {
        return new StringFilter(filter);
        //TODO make filter
    }

    @Override
    Interpretation<CodeBlock> makePrimitive(String primitive, String table, Endpoint parent) {
        return new StringFieldCondition(primitive,table,parent);
    }

    @Override
    Interpretation<CodeBlock> makeOperand(Interpretation<CodeBlock> left, Interpretation<CodeBlock> right, String operand,String table, Endpoint parent) {
        String def=Converter.isAND(operand)?("DSL.trueCondition()"):("DSL.falseCondition()");
        makeFilterResult(left,def,table,parent);
        makeFilterResult(right,def,table,parent);
        return new StringOperand(left,right,operand);
    }
}
