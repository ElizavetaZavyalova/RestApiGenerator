package org.example.analize.condition;

import com.squareup.javapoet.CodeBlock;

import org.example.analize.interpretation.Interpretation;

import org.example.analize.premetive.info.VarInfo;

import java.util.List;

public class StringOperand extends BaseOperand<CodeBlock> {
    public StringOperand(Interpretation<CodeBlock> left, Interpretation<CodeBlock> right, String op) {
        super(left, right, op);
    }

    @Override
    public CodeBlock interpret() {
        var block = CodeBlock.builder();
        switch (operand) {
            case OR -> block.add("DSL.or(");
            case AND -> block.add("DSL.and(");
        }
        return block.add(left.interpret()).add(", ")
                .add(right.interpret()).add(")").build();
    }


    @Override
    public void addParams(List<VarInfo> params) {
        left.addParams(params);
        right.addParams(params);

    }
}
