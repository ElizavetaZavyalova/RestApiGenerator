package org.example.analize.rewrite.address.select;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Variables;
import org.example.analize.rewrite.address.select_table.BaseSelectFieldsParser;
import org.example.analize.rewrite.address.select_table.select_id.BaseSelectIdParser;
import org.example.analize.rewrite.address.select_table.select_id.BuildSelectId;
import org.example.analize.rewrite.address.where.BaseWhereParser;
import org.example.analize.rewrite.address.where.BuildWhere;


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
    BaseSelectFieldsParser<String, String, String> makeSelect(String request, Variables variables) {
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
