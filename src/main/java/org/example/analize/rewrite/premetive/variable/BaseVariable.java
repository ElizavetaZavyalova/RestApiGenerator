package org.example.analize.rewrite.premetive.variable;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
@Slf4j
public abstract class BaseVariable<VariableName> implements Interpretation<VariableName> {
    String variable=null;
    BaseVariable(String variable, Variables variables){
        log.debug("BaseVariable: variable:"+variable);
        if(variable.isEmpty()){
            variable="null";

        }
        this.variable=makeVariable(variable,variables);
        log.debug("BaseVariable: this.variable:"+this.variable);
    }
    protected abstract String makeVariable(String variable, Variables variables);
}
