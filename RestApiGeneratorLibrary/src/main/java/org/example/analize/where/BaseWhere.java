package org.example.analize.where;

import org.example.analize.interpretation.InterpretationOfRequest;
import org.example.analize.postfix_infix.Converter;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.premetive.filters.FilterCreation;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

import static org.example.analize.where.BaseWhere.RegExp.*;

public abstract class BaseWhere<R,C> implements Interpretation<R> {
    ArrayList<Interpretation<R>> ports=new ArrayList<>();
    record RegExp(){
        static final String SPLIT="/";
        static final String LEFT_SQUARE_BRACKET="[";
        static final String RIGHT_SQUARE_BRACKET="]";
        static final String LEFT_CURLY_BRACKET="{";
        static final String RIGHT_CURLY_BRACKET="}";
    }
    BaseWhere(String where, String table, Endpoint parent){
        String[] wherePorts= where.split(SPLIT);
        for(String port:wherePorts){
            if(!port.isEmpty()) {
                ports.add(makePort(port, table, parent));
            }
        }
    }
    String operator;
    Interpretation<R> makePort(String port,String table,Endpoint parent){
        Queue<String> postfix= Converter.toPostfix(port);
        Stack<Interpretation<R>> stack=new Stack<>();
        while(!postfix.isEmpty()){
            String argument=postfix.poll();
            if(Converter.isOperator(argument)){
                stack.push(makeOperand(stack.pop(),stack.pop(),argument,table,parent));
            }
            else {
                stack.push(makeFilterOrPrimitive(argument,table,parent));
            }
        }
        Interpretation<R> result=(stack.pop());
        makeFilterResult(result,null,table,parent);
        return   result;
    }
    Interpretation<R> makeFilterOrPrimitive(String port,String table,Endpoint parent){
        if(port.endsWith(RIGHT_SQUARE_BRACKET)&&port.startsWith(LEFT_SQUARE_BRACKET)){
            return makeFilter(port.substring(LEFT_SQUARE_BRACKET.length(), port.length() - RIGHT_SQUARE_BRACKET.length()));
        }
        if(port.endsWith(RIGHT_CURLY_BRACKET)&&port.startsWith(LEFT_CURLY_BRACKET)){
            return makePrimitive(port.substring(LEFT_CURLY_BRACKET.length(), port.length() - RIGHT_CURLY_BRACKET.length()),table,parent);
        }
        throw new IllegalArgumentException("FILTER MUST BE IN:"+LEFT_SQUARE_BRACKET+RIGHT_SQUARE_BRACKET+" OR" +
                "PRIMITIVE MUST BE IN:"+LEFT_CURLY_BRACKET+RIGHT_CURLY_BRACKET);
    }
    @Override
    public String requestInterpret() {
        return ports.stream().map(InterpretationOfRequest::requestInterpret)
                .filter(s->!s.isEmpty()).collect(Collectors.joining("/"));
    }
    void makeFilterResult(Interpretation<R> interpretation, C operand, String table, Endpoint parent) {
        if (interpretation instanceof FilterCreation) {
            ((FilterCreation<C>) interpretation).makeFilter(parent, operand, table);
        }
    }
    abstract Interpretation<R> makeFilter(String filter);
    abstract Interpretation<R> makePrimitive(String primitive,String table,Endpoint parent);
    abstract Interpretation<R> makeOperand(Interpretation<R> left,Interpretation<R> right, String operand,String table,Endpoint parent);
}
