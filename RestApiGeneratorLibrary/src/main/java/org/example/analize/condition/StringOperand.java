package org.example.analize.condition;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.interpretation.Interpretation;

public class StringOperand extends BaseOperand<CodeBlock> {
    public StringOperand(Interpretation<CodeBlock> left, Interpretation<CodeBlock> right, String op) {
        super(left, right, op);
    }

    @Override
    public CodeBlock interpret() {
       var block=CodeBlock.builder();
        switch (operand) {
            case OR -> block.add("DSL.or(");
            case AND -> block.add("DSL.and(");
        }
        return block.add(left.interpret()).add(", ")
                .add(right.interpret()).add(")").build();
    }


    @Override
    public String getParams() {
        return null;
    }
}
