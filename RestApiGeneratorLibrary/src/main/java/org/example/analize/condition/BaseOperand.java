package org.example.analize.condition;

import org.example.analize.interpretation.Interpretation;

public abstract class BaseOperand<R> implements Interpretation<R> {
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
    BaseOperand(Interpretation<R> left, Interpretation<R> right, String op){
        operand=OperandVal.getOperand(op);
        this.left=left;
        this.right=right;
    }
    @Override
    public String requestInterpret() {
        String res=left.requestInterpret()+"-"+right.requestInterpret();
        if(res.startsWith("-")){
            return res.substring(1);
        }
        if(res.endsWith("-")){
            return res.substring(0,res.length()-1);
        }
        return res;
    }

}
