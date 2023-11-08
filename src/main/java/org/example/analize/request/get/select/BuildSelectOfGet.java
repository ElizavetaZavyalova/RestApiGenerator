package org.example.analize.request.get.select;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Variables;
import org.example.analize.address.select.BaseSelectParser;
import org.example.analize.address.select_table.BaseSelectFieldsParser;
import org.example.analize.request.get.select_table.BuildSelectOfGetFields;
import org.example.analize.address.select.BuildSelect;

@Slf4j
public class BuildSelectOfGet  extends BuildSelect {
    public BuildSelectOfGet(String request, Variables variables, BaseSelectParser<String,String,String,String> selectPrevious) {
        super(request, variables, selectPrevious);
        Debug.debug(log,"BuildSelectOfGet request:",request);
    }
    @Override
    protected BaseSelectFieldsParser<String, String, String> makeSelect(String request, Variables variables) {
        Debug.debug(log,"makeSelect request:",request);
        if(request.isEmpty()){
            return null;
        }
        return new BuildSelectOfGetFields(request, variables);
    }
}
