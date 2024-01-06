package org.example.analize.address.select;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Variables;
import org.example.analize.address.select_table.BaseSelectFieldsParser;
import org.example.analize.address.where.BaseWhereParser;
import org.example.analize.address.where.BuildWhere;
import org.example.analize.address.select_table.select_id.BuildSelectId;


@Slf4j
public class BuildSelect extends BaseSelectParser<String,String,String,String>{
    public BuildSelect(String request, Variables variables, BaseSelectParser<String, String, String, String> selectPrevious) {
        super(request, variables, selectPrevious);
        Debug.debug(log,"BuildSelect request:",request);
    }

    @Override
    public String interpret() {
        Debug.debug(log,"interpret");
        if(selectFields!=null) {
            Debug.debug(log,"interpret selectId");
            StringBuilder selectJoinStep=new StringBuilder().append(selectFields.interpret());
            if (where != null) {
                Debug.debug(log," interpret selectId where");
                selectJoinStep.append(".where(").append(where.interpret()).append(")");
            }
            return  Debug.debugResult(log,"interpret",selectJoinStep.toString());
        }
        return Debug.debugResult(log,"interpret", "emptyString");//TODO exeption
    }

    @Override
    protected BaseSelectFieldsParser<String, String, String> makeSelect(String request, Variables variables) {
        Debug.debug(log,"makeSelect request:",request);
        if(request.isEmpty()){
            return null;
        }
        return new BuildSelectId(request, variables);
    }

    @Override
    BaseWhereParser<String, String, String, String> makeBaseWhereParser(String request, Variables variables, BaseSelectParser<String, String, String, String> selectPrevious) {
        Debug.debug(log,"makeBaseWhereParser request:",request);
        if(request.isEmpty()){
            return null;
        }
        return new BuildWhere(request,variables,selectPrevious,getFieldIn(),getTable());
    }
}