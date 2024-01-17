package org.example.analize.address.where;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.Debug;
import org.example.analize.Variables;
import org.example.analize.address.condition.BaseConditionParser;
import org.example.analize.address.condition.BuildCondition;
import org.example.analize.address.premetive.field.BuildField;
import org.example.analize.address.premetive.field.in.BaseFieldIn;
import org.example.analize.address.premetive.field.in.BuildFieldIn;
import org.example.analize.address.premetive.table.BaseTable;
import org.example.analize.address.select.BaseSelectParser;

@Slf4j
public class BuildWhere extends BaseWhereParser<String,String,String,String> {
    public BuildWhere(String request, Variables variables, BaseSelectParser<String, String, String, String> selectPrevious,
                      BaseFieldIn<String, String> idIn, BaseTable<String> table) {
        super(request, variables, selectPrevious, idIn, table);
        Debug.debug(log,"BaseWhere request:",request);
    }
    BuildWhere(String request, Variables variables, BaseFieldIn<String,
            String> idIn, BaseTable<String> table) {
        super(request, variables, idIn, table);
        Debug.debug(log,"BaseWhere request:",request);
    }
    @Override
    public String interpret() {
        Debug.debug(log,"interpret");
        if(condition!=null){
            Debug.debug(log,"interpret condition no null");
            StringBuilder interpretation=new StringBuilder(condition.interpret());
            if(select!=null){
                Debug.debug(log,"interpret condition&select no null");
                interpretation.append("and(").append(idIn.interpret())
                        .append(".in(").append(select.interpret()).append("))");
            }
            return Debug.debugResult(log,"interpret",interpretation.toString());
        }
        if(select!=null){
            Debug.debug(log,"interpret condition null");
            return Debug.debugResult(log,"interpret",
                    idIn.interpret()+".in("+select.interpret()+")");
        }
        Debug.debug(log,"interpret all null");
        return Debug.debugResult(log,"interpret","emptyString");
    }
    @Override
    BaseFieldIn<String, String> makeFieldIn(BaseTable<String> tableIn, String name,
                                            BaseTable<String> table, Variables variables) {
        Debug.debug(log,"makeFieldIn name:",name," table:",table.getTable());
        return new BuildFieldIn(tableIn,new BuildField(name,table,variables));
    }

    @Override
    BaseConditionParser<String, String> makeCondition(String request, Variables variables,
                                                      BaseTable<String> table) {
        Debug.debug(log,"makeCondition:" , request);
        return new BuildCondition(request, variables,table);
    }
}
