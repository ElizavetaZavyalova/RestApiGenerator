package org.example.analize.sqlRequest;

import lombok.Getter;
import org.example.analize.Variables;
import org.example.analize.address.FullAddressSelect;
import org.example.analize.address.PortAddressSelect;
import org.example.analize.connections.ConditionInterpret;
import org.example.analize.connections.WhenCondition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Select;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;

public class SelectRequest implements SqlInterpret {
    ConditionInterpret conditionInterpret=null;
    FullAddressSelect fullAddressSelect=null;
    String limit=null;
    String groupBy=null;

    String idNext=null;
    String tableName=null;
    List<String> fields=new ArrayList<>();

    public Select makeSelect(DSLContext dsl) {
         return null;
    }
    @Override
    public String interpret(){
         return null;
    }


    SelectRequest(String request, Variables variables, FullAddressSelect fullAddressSelect){
        this.fullAddressSelect=fullAddressSelect;
        String[] input=request.split(":");
        input[input.length-1]=parseWhere(input[input.length-1],variables);
        for(String port:input){
            if(isIdNext(port)){
                idNext=variables.addVariableField(makeId(port));
            }
            else if(isGroupBy(port)){
                groupBy=variables.addVariableField(makeId(port));
            }
            else if(isLimit(port)){
                limit=variables.addVariableLong(makeId(port));
            }
            else if(isField(port)){
                 fields.add(variables.addVariableField(makeId(port)));
            }
            else {
                tableName = variables.addVariableField(port);
            }
        }
        if(fullAddressSelect!=null&&idNext==null){
            idNext=variables.makeString(variables.makeVariableFromString(fullAddressSelect.getTableName())+"Id");
        }
        if(tableName==null){
           // tableName=variables.makeTableName(id,idNext);
            //TODO exeption
        }
    }
    static private class Port{
        static final int TABLE=0;
        static final int WHEN=1;
    }
    String parseWhere(String request,Variables variables){
        String[] input=request.split("\\?");
        if(input.length==0){
            return request;
        }
        if(input.length!=2){
            //TODO exception
        }
        conditionInterpret=new WhenCondition(input[Port.WHEN],variables);
        return input[Port.TABLE];
    }

    String makeId(String id){
        return id.substring(1, id.length());
    }
    boolean isIdNext(String id){
        return id.charAt(0)=='<';
    }
    boolean isField(String id){
        return id.charAt(0)=='=';
    }

    boolean isLimit(String limit){
        return limit.charAt(0)=='%';
    }
    boolean isGroupBy(String field){
        return limit.charAt(0)=='#';
    }

}
