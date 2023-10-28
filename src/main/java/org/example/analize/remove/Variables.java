package org.example.analize.remove;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.Map;
import java.util.TreeMap;

@Getter
public class Variables {
    @AllArgsConstructor
    public enum Type {
        STRING("String"), LONG("long");
        @Getter
        String value = null;
    }

    Map<String, Type> variables = new TreeMap<>();

    private String addVariable(String variable, Type type) {
        if (variables.containsKey(variable)) {
            int count=0;
           while(variables.containsKey(variable+count)){
               count++;
           }
           variable=variable+count;
        }
        variables.put(variable, type);
        return variable;
    }

    private static boolean isVariable(String value) {
        return (value.charAt(0) == '{') && (value.charAt(value.length() - 1) == '}');
    }

    private static String makeVariable(String value) {
        return value.substring(1, value.length() - 1);
    }
    public String makeFieldFromVariable(String value) {
        if(isVariable(value)){
            return makeString(makeLongVariable(makeVariable(value)));
        }
        return value;
    }

    public  String makeString(String string) {
        return "\"" + string + "\"";
    }

    private static String makeLongVariable(String value) {
        if ((value.charAt(0) == '@')) {
            return value.substring(1, value.length());
        }
        return value;
    }

    private static Type makeType(String value) {
        if ((value.charAt(0) == '@')) {
            return Type.LONG;
        }
        return Type.STRING;
    }

    public String addVariableField(String variable) {
        if (isVariable(variable)) {
            variable = makeVariable(variable);
            variable=addVariable(variable, Type.STRING);
            return variable;
        }
        return makeString(variable);
    }
    public String addVariableLong(String variable) {
        if (isVariable(variable)) {
            variable = makeVariable(variable);
            variable=addVariable(variable, Type.LONG);
        }
        return variable;
    }
    private static String makeLongVariable(String value, boolean isOnlyString) {
         if(!isOnlyString){
             return makeLongVariable(value);
         }
         //TODO exception
         return value;
    }
    public static String makeVariableFromString(String value){
        if((value.charAt(0) == '\"') && (value.charAt(value.length() - 1) == '\"')){
            return makeVariable(value);
        }
        return value;
    }
    String makeTableFromId(String id){
        id=makeVariableFromString(id);
       return id.substring(0, id.length() - 2);
    }
    public String makeTableName(String id,String idNext){
        if(idNext!=null){
            return makeString(makeTableFromId(id)+"And"+makeTableFromId(idNext));
        }
        return null;
        //TODO exception
        //value.substring(1, value.length() - 1)

    }


    public String addVariableValue(String variable, boolean isOnlyString) {
        if (Variables.isVariable(variable)) {
            variable = makeVariable(variable);
            Type type = makeType(variable);
            if (type == Type.LONG) {
                variable = makeLongVariable(variable,isOnlyString);
            }
            return  addVariable(variable, type);
        }
        if (makeType(variable) == Type.LONG) {
            variable = makeLongVariable(variable,isOnlyString);
            return variable;
        }
        return makeString(variable);
    }
}