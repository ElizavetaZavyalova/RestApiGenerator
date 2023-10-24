package org.example.analize.address;

import lombok.Getter;
import org.example.analize.Variables;
import org.example.analize.connections.ConditionInterpret;
import org.example.analize.connections.WhenCondition;
import org.jooq.DSLContext;
import org.jooq.Select;
import org.jooq.impl.DSL;

public class PortAddressSelect implements SelectInterpret{
    //TODO GROUPBY LIMIT
    ConditionInterpret conditionInterpret=null;
    PortAddressSelect previousSelect=null;
    String id="\"Id\"";
    String idNext=null;
    @Getter
    String tableName=null;
    @Override
    public Select makeSelect(DSLContext dsl) {
        if(previousSelect!=null&&conditionInterpret!=null){
            return dsl.select(DSL.field(id)).from(tableName).
                    where(DSL.field(idNext).in(previousSelect.makeSelect(dsl)).and(conditionInterpret.makeCondition(dsl)));
        }
        else if(previousSelect!=null){
            return dsl.select(DSL.field(id)).from(tableName).
                    where(DSL.field(idNext).in(previousSelect.makeSelect(dsl)));
        }
        else if(conditionInterpret!=null){
            return dsl.select(DSL.field(id)).from(tableName).
                    where(conditionInterpret.makeCondition(dsl));
        }
        return dsl.select(DSL.field(id)).from(tableName);
    }


    PortAddressSelect(String request, Variables variables,PortAddressSelect previousSelect){
        this.previousSelect=previousSelect;
        String[] input=request.split(":");
        if(input.length>3||input.length<1){
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
        return id.charAt(0)=='>';
    }
    boolean isId(String id){
        return id.charAt(0)=='=';
    }

}
