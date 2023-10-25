package org.example.analize.address;

import lombok.Getter;
import org.example.analize.Variables;
import org.example.analize.connections.ConditionInterpret;
import org.example.analize.connections.WhenCondition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Select;
import org.jooq.impl.DSL;

import java.text.Format;

public class PortAddressSelect implements SelectInterpret{

    //GROUPBY LIMIT
    private static class MaxMin {
        private static final int MAX=1;
        private static final int MIN=-1;

    }
    ConditionInterpret conditionInterpret=null;
    PortAddressSelect previousSelect=null;
    String id="\"Id\"";

    String limit=null;
    String groupBy=null;
    int maxOrMin=0;
    String idNext=null;
    @Getter
    String tableName=null;
    @Override
    public Select makeSelect(DSLContext dsl) {
        if(previousSelect!=null&&conditionInterpret!=null){
            return dsl.select(makeFieldId()).from(tableName).
                    where(DSL.field(idNext).in(previousSelect.makeSelect(dsl)).and(conditionInterpret.makeCondition(dsl)));
        }
        else if(previousSelect!=null){
            return dsl.select(makeFieldId()).from(tableName).
                    where(DSL.field(idNext).in(previousSelect.makeSelect(dsl)));
        }
        else if(conditionInterpret!=null){
            return dsl.select(makeFieldId()).from(tableName).
                    where(conditionInterpret.makeCondition(dsl));
        }
        return dsl.select(makeFieldId()).from(tableName);
    }
    Field makeFieldId(){
        if(maxOrMin==MaxMin.MAX){
            return DSL.max(DSL.field(id));
        }
        else if(MaxMin.MIN == maxOrMin){
            return DSL.min((DSL.field(id)));
        }

        return DSL.field(id);
    }
    PortAddressSelect(String request, Variables variables,PortAddressSelect previousSelect){
        this.previousSelect=previousSelect;
        String[] input=request.split(":");
        if(input.length>6||input.length<1){
            return;
        }
        input[input.length-1]=parseWhere(input[input.length-1],variables);
        for(String port:input){
            if(isIdNext(port)){
                idNext=variables.addVariableField(makeId(port));
            }
            else if(isId(port)){
                id=variables.addVariableField(makeId(port));
            }
            else if(isGroupBy(port)){
                groupBy=variables.addVariableField(makeId(port));
            }

            else if(isLimit(port)){
                limit=variables.addVariableLong(makeId(port));
            }
            else if(isMax(port)){
                maxOrMin=MaxMin.MAX;
            }
            else if(isMin(port)){
                maxOrMin=MaxMin.MIN;
            }
            else {
                tableName = variables.addVariableField(port);
            }
        }
        if(previousSelect!=null&&idNext==null){
            idNext=variables.makeString(variables.makeVariableFromString(previousSelect.previousSelect.tableName)+"Id");
        }
        if(tableName==null){
            tableName=variables.makeTableName(id,idNext);
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
    boolean isId(String id){
        return id.charAt(0)=='=';
    }
    boolean isLimit(String limit){
        return limit.charAt(0)=='%';
    }
    boolean isGroupBy(String field){
        return limit.charAt(0)=='$';
    }
    boolean isMax(String max){
        return max.equals(">");
    }
    boolean isMin(String min){
        return min.equals("<");
    }

}
