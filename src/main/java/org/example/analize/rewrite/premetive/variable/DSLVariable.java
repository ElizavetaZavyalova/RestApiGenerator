package org.example.analize.rewrite.premetive.variable;


import org.example.analize.rewrite.Variables;

public class DSLVariable extends BaseVariable<String>{
    public DSLVariable(String variable, Variables variables) {
        super(variable, variables);
    }

    @Override
    public String interpret() {
        return variable;
    }

    @Override
    protected String makeVariable(String variable, Variables variables) {
        return Variables.make.makeWriteVariable(variable);
    }
}
