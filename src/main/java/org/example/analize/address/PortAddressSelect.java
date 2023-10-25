package org.example.analize.address;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.Variables;
import org.example.analize.connections.ConditionInterpret;
import org.example.analize.connections.WhenCondition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Select;
import org.jooq.impl.DSL;

import java.text.Format;
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
         String string;
        String select="dsl.select("+makeField(id)+").from("+tableName+")";
        if(previousSelect!=null&&conditionInterpret!=null){
            string= select+ ".where(DSL.field("+idNext+").in("+previousSelect.makeSelect()+
                    ").and("+conditionInterpret.makeCondition()+"))"+addGroupByAndLimit();
        }
        else if(previousSelect!=null){
            string=  select+ ".where(DSL.field(\"+idNext+\").in(" +previousSelect.makeSelect()+"))"+addGroupByAndLimit();
        }
        else if(conditionInterpret!=null){
            string=  select+ ".where("+conditionInterpret.makeCondition()+")"+addGroupByAndLimit();
        }
        else {
            string = select + addGroupByAndLimit();
        }
        log.debug("interprit:"+string);
        return string;
    }
    String addGroupByAndLimit(){
        StringBuilder stringBuilder=new StringBuilder();
         if(groupBy!=null){
             stringBuilder.append(".groupBy("+makeField(groupBy)+")");
         }
        if(limit!=null){
            stringBuilder.append(".limit("+limit+")");
        }
        return stringBuilder.toString();
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
    String makeField(String id){
        if(maxOrMin==MaxMin.MAX){
            return "DSL.max(DSL.field("+id+"))";
        }
        else if(MaxMin.MIN == maxOrMin){
            return "DSL.min(DSL.field("+id+"))";
        }
        return "DSL.field("+id+")";
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
