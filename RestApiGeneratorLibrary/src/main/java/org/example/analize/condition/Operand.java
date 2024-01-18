package org.example.analize.condition;

import org.example.analize.interpretation.Interpretation;

public abstract class Operand<R> extends Interpretation<R> {
    Interpretation<R> left;
    Interpretation<R> right;
    enum OperandVal{
        AND("&"),OR("|");
        String val;
        OperandVal(String op){
            val=op;

        }
        static OperandVal getOperand(String op){
            for(OperandVal operandVal:OperandVal.values()){
                   if(operandVal.val.equals(op)){
                       return operandVal;
                   }
            }
            return AND;
        }
    }
    OperandVal operand;
    Operand(Interpretation<R> left,Interpretation<R> right,String op){
        operand=OperandVal.getOperand(op);
        this.left=left;
        this.right=right;

    }
}
