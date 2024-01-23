package org.example.analize.condition;

import org.example.analize.interpretation.Interpretation;

public class StringOperand extends BaseOperand<String> {
    public StringOperand(Interpretation<String> left, Interpretation<String> right, String op) {
        super(left, right, op);
    }

    @Override
    public String interpret() {
        StringBuilder builder = new StringBuilder("\nDSL.");
        switch (operand) {
            case OR -> builder.append("or(");
            case AND -> builder.append("and(");
        }
        return builder.append(left.interpret()).append(", ")
                .append(right.interpret()).append(")").toString();
    }


    @Override
    public String getParams() {
        return null;
    }
}
