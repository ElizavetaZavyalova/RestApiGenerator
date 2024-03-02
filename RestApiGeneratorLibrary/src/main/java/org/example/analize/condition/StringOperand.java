package org.example.analize.condition;

import com.squareup.javapoet.CodeBlock;

import org.example.analize.interpretation.Interpretation;

import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;

import java.util.List;
import java.util.Objects;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public class StringOperand extends BaseOperand<CodeBlock> {
    public StringOperand(Interpretation<CodeBlock> left, Interpretation<CodeBlock> right, String op) {
        super(left, right, op);
    }

    @Override
    public CodeBlock interpret() {
        var block = CodeBlock.builder();
        if (Objects.requireNonNull(operand) == OperandVal.OR) {
            block.add("$T.or(", DSL_CLASS);
        }
        else if(Objects.requireNonNull(operand) == OperandVal.AND){
            block.add("$T.and(", DSL_CLASS);
        }
        return block.add(left.interpret()).add(", ")
                .add(right.interpret()).add(")").build();
    }


    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        left.addParams(params,filters);
        right.addParams(params,filters);

    }
}
