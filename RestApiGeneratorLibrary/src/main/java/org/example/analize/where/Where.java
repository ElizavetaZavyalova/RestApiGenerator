package org.example.analize.where;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.condition.Operand;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.postfix_infix.Converter;
import org.example.analize.premetive.fields_cond.FieldCondition;
import org.example.analize.premetive.filters.StringFilter;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.analize.where.Where.NoCondition.ONE_PORT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public class Where extends BaseWhere<CodeBlock> {
    public Where(String where, String table, Endpoint parent) {
        super(where, table, parent);
    }
    record NoCondition(){
        static final int ONE_PORT=1;
    }
    @Override
    public CodeBlock interpret() {
        var block = CodeBlock.builder();
        if (ports.isEmpty()) {
            return block.add("").build();
        }
        if (ports.size()==ONE_PORT) {
            return block.add(ports.get(0).interpret()).build();
        }
        return block.add("$T.and(",DSL_CLASS).add(ports.stream()
                        .map(InterpretationBd::interpret)
                        .reduce((v, h) -> CodeBlock.builder().add(v).add(", ").add(h).build())
                        .orElse(ports.get(0).interpret()))
                .add(")").build();

    }


    @Override
    Interpretation<CodeBlock> makeFilter(String filter) {
        return new StringFilter(filter);
    }

    @Override
    Interpretation<CodeBlock> makePrimitive(String primitive, String table, Endpoint parent) {
        return new FieldCondition(primitive, table, parent);
    }

    @Override
    Interpretation<CodeBlock> makeOperand(Interpretation<CodeBlock> left, Interpretation<CodeBlock> right, String operand, String table, Endpoint parent) {
        CodeBlock def = Converter.isAND(operand) ?
                (CodeBlock.builder().add("$T.trueCondition()",DSL_CLASS).build()) :
                (CodeBlock.builder().add("$T.falseCondition()",DSL_CLASS).build());
        makeFilterResult(left, def, table, parent);
        makeFilterResult(right, def, table, parent);
        return new Operand(left, right, operand);
    }

    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        ports.forEach(port -> port.addParams(params,filters));
    }
}
