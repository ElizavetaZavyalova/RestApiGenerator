package org.example.analize.address.premetive.variable;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Variables;

@Slf4j
public class BuildVariable extends BaseVariable<String> {
    public BuildVariable(String variable, Variables variables) {
        super(variable, variables);
        Debug.debug(log,"BuildVariable:",variable);
    }
    @Override
    public String interpret() {
        Debug.debug(log,"BuildVariable:",variable);
        return Debug.debugResult(log,"interpret",variable);
    }

    @Override
    protected String makeVariable(String variable, Variables variables) {
        Debug.debug(log,"makeVariable variable:",variable);
        Variables.make.TYPE type=Variables.make.makeType(variable);
        Debug.debug(log,"makeVariable TYPE:",type.getValue());
        if(Variables.make.isVariable(variable)){
           this.isVariable=true;
           Debug.debug(log,"makeVariable isVariable==true");
           variables.add(Variables.make.makeVariable(variable),type);
        }
        variable=Variables.make.makeWriteVariable(variable);
        Debug.debug(log,"makeVariable WriteVariable:",variable);
        return variable;
    }
}
