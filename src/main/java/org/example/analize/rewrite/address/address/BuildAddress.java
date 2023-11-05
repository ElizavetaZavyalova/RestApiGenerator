package org.example.analize.rewrite.address.address;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.select.BaseSelectParser;
import org.example.analize.rewrite.address.select.BuildSelect;

@Slf4j
public class BuildAddress extends BaseAddress<String,String,String,String>{
    BuildAddress(String[] request, Variables variables) {
        super(request, variables);
        Debug.debug(log, "BuildAddress requests:", request);
    }

    @Override
    public String interpret() {
        Debug.debug(log, "interpret");
        if (address != null) {
            Debug.debug(log, "interpret address!=null");
            return Debug.debugResult(log,"interpret",address.interpret());
        }
        Debug.debug(log, "interpret address==null");
        return  Debug.debugResult(log,"interpret","emptyString");
    }
    @Override
    BaseSelectParser<String, String, String, String> makeAddress(String request, Variables variables, BaseSelectParser<String, String, String, String> address) {
        Debug.debug(log, "makeAddress request:", request);
        return new BuildSelect(request, variables, address);
    }
}
