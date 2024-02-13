package org.example.analize.condition;

import org.example.analize.interpretation.Interpretation;

import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Request.*;

public abstract class BaseOperand<R> implements Interpretation<R> {
    Interpretation<R> left;
    Interpretation<R> right;

    enum OperandVal {
        AND(AND_), OR(OR_);
        String val;

        OperandVal(String op) {
            val = op;

        }

        static OperandVal getOperand(String op) {
            for (OperandVal operandVal : OperandVal.values()) {
                if (operandVal.val.equals(op)) {
                    return operandVal;
                }
            }
            return AND;
        }
    }

    OperandVal operand;

    BaseOperand(Interpretation<R> left, Interpretation<R> right, String op) {
        operand = OperandVal.getOperand(op);
        this.left = left;
        this.right = right;
    }


}
