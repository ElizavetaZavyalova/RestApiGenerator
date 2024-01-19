package org.example.analize.condition;

import org.example.analize.interpretation.Interpretation;

public class StringOperand extends BaseOperand<String> {
    public StringOperand(Interpretation<String> left, Interpretation<String> right, String op) {
        super(left, right, op);
    }

    @Override
    public String interpret() {
        StringBuilder builder = new StringBuilder(left.interpret());
        switch (operand) {
            case OR -> builder.append("\n.or(");
            case AND -> builder.append("\n.and(");
        }
        return builder.append(right.interpret()).append(")").toString();
    }


    @Override
    public String getParams() {
        return null;
    }
}
