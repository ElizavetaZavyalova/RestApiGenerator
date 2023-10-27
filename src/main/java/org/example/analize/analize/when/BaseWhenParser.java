package org.example.analize.analize.when;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.Interpretation;
import org.example.analize.analize.condition.BaseConditionParser;
import org.example.analize.analize.select.BaseSelectParser;
@Slf4j
public abstract class BaseWhenParser<Condition,Select> implements Interpretation<Condition> {
    BaseConditionParser<Condition> condition=null;
    BaseSelectParser<Condition,Select> select=null;
    String idIn=null;
    BaseWhenParser(String request, BaseVariables variables,
                   BaseSelectParser<Condition,Select> selectPrevious,String idIn){
        log.debug("BaseWhenParser:"+request);
        setIdIn(idIn);
        this.select=selectPrevious;
        if(!request.isEmpty()) {
            this.condition = makeCondition(request, variables);
        }

    }
    record debug(){
        static String debugString(String information,String result){
            if(log.isDebugEnabled()){
                log.debug(information+": "+result);
            }
            return result;
        }
    }
    abstract void setIdIn(String idIn);
    BaseWhenParser(String request, BaseVariables variables,String idIn){
         this(request,variables,null,idIn);
         log.debug("no previous select BaseWhenParser");
    }
    abstract BaseConditionParser<Condition> makeCondition(String request, BaseVariables variables);

}
