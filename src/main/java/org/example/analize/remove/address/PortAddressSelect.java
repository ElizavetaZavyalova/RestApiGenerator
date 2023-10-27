package org.example.analize.remove.address;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.remove.Variables;
import org.example.analize.remove.connections.ConditionInterpret;
import org.example.analize.remove.connections.WhenCondition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Select;
import org.jooq.impl.DSL;

@Slf4j
public class PortAddressSelect implements SelectInterpret{

    //TODO GROUPBY LIMIT
    record MaxMin() {
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
            return dsl.select(makeFieldId(id)).from(tableName).
                    where(DSL.field(idNext).in(previousSelect.makeSelect(dsl)).and(conditionInterpret.makeCondition(dsl)));
        }
        else if(previousSelect!=null){
            return dsl.select(makeFieldId(id)).from(tableName).
                    where(DSL.field(idNext).in(previousSelect.makeSelect(dsl)));
        }
        else if(conditionInterpret!=null){
            return dsl.select(makeFieldId(id)).from(tableName).
                    where(conditionInterpret.makeCondition(dsl));
        }
        return dsl.select(makeFieldId(id)).from(tableName);
    }
    @Override
    public String makeSelect(){
        StringBuilder select=new StringBuilder();
        select.append("dsl.select(").append(makeField(id)).append(").from(").append(tableName).append(")");
       // String select="dsl.select("+makeField(id)+").from("+tableName+")";
        if(previousSelect!=null&&conditionInterpret!=null){
             select.append(".where(DSL.field(").append(idNext).append(").in(")
                     .append(previousSelect.makeSelect()).append(").and(")
                     .append(conditionInterpret.makeCondition()).append("))").append(addGroupByAndLimit());
        }
        else if(previousSelect!=null){
             select.append(".where(DSL.field(").append(idNext).append(").in(")
                    .append(previousSelect.makeSelect()).append("))")
                    .append(addGroupByAndLimit());
        }
        else if(conditionInterpret!=null){
            select.append(".where(").append(conditionInterpret.makeCondition())
                    .append(")").append(addGroupByAndLimit());
        }
        else {
            select.append(addGroupByAndLimit());
        }
        log.debug("makeSelect:"+select.toString());
        return select.toString();
    }
    String addGroupByAndLimit(){
        StringBuilder stringBuilder=new StringBuilder();
         if(groupBy!=null){
             stringBuilder.append(".groupBy(").append(makeField(groupBy)).append(")");
         }
        if(limit!=null){
            stringBuilder.append(".limit(").append(limit).append(")");
        }
        log.debug("addGroupByAndLimit |"+stringBuilder.toString());
        return stringBuilder.toString();
    }
    String makeField(String id){
        StringBuilder field=new StringBuilder();
        if(maxOrMin==MaxMin.MAX){
            field.append("DSL.max(DSL.field(").append(id).append("))");
        }
        else if(MaxMin.MIN == maxOrMin){
            field.append("DSL.min(DSL.field(").append(id).append("))");
        }
        else{
            field.append("DSL.field(").append(id).append(")");
        }
        log.debug("makeField id:"+id+" |"+field.toString());
        return  field.toString();
    }
    Field makeFieldId(String id){
        if(maxOrMin==MaxMin.MAX){
            return DSL.max(DSL.field(id));
        }
        else if(MaxMin.MIN == maxOrMin){
            return DSL.min((DSL.field(id)));
        }

        return DSL.field(id);
    }

    PortAddressSelect(String request, Variables variables,PortAddressSelect previousSelect){
        log.debug("request:"+request);
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
            idNext=variables.makeString(variables.makeVariableFromString(previousSelect.getTableName())+"Id");
        }
        if(tableName==null){
            tableName=variables.makeTableName(id,idNext);
        }
    }
    record Port(){
        static final int TABLE=0;
        static final int WHEN=1;
    }

    String parseWhere(String request,Variables variables){
        log.debug("request:"+request);
        String[] input=request.split("\\?");
        if(input.length==1){
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
        return id.charAt(0)=='&';
    }
    boolean isId(String id){
        return id.charAt(0)=='|';
    }
    boolean isLimit(String limit){
        return limit.charAt(0)=='%';
    }
    boolean isGroupBy(String field){
        return  field.charAt(0)=='#';
    }
    boolean isMax(String max){
        return max.equals(">");
    }
    boolean isMin(String min){
        return min.equals("<");
    }

}
