package org.example.analize.rewrite.address.premetive.variable;


import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Variables;
@Slf4j
public class DSLVariable extends BaseVariable<String>{
    public DSLVariable(String variable, Variables variables) {
        super(variable, variables);
        Debug.debug(log,"DSLVariable variable:",variable);
    }

    @Override
    public String interpret() {
        Debug.debug(log,"interpret:",variable);
        return variable;
    }

    @Override
    protected String makeVariable(String variable, Variables variables) {
        Debug.debug(log,"makeVariable variable:",variable);
        return Variables.make.makeWriteVariable(variable);
    }
}
