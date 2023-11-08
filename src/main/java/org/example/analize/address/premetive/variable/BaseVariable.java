package org.example.analize.address.premetive.variable;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Variables;
import org.example.analize.Interpretation;

@Slf4j
public abstract class BaseVariable<VariableName> implements Interpretation<VariableName> {
    String variable=null;
    protected boolean isVariable=false;
    BaseVariable(String variable, Variables variables){
        Debug.debug(log," BaseVariable: variable:",variable);
        if(variable.isEmpty()){
            variable="null";

        }
        this.variable=makeVariable(variable,variables);
        Debug.debug(log,"BaseVariable: this.variable:",this.variable);
    }
    @Override
    public String requestInterpret(){
        if(isVariable){
            return "{"+variable+"}";
        }
        return Variables.make.makeVariableFromString(variable);
    }
    protected abstract String makeVariable(String variable, Variables variables);
}
