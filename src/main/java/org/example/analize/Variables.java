package org.example.analize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.analize.connections.OperationInterpret;

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

    private void addVariable(String variable, Type type) {
        if (variables.containsKey(variable)) {
            //TODO exeption
        }
        variables.put(variable, type);
    }

    private static boolean isVariable(String value) {
        return (value.charAt(0) == '{') && (value.charAt(value.length() - 1) == '}');
    }

    private static String makeVariable(String value) {
        return value.substring(1, value.length() - 1);
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
        if ((value.charAt(1) == '@')) {
            return Type.LONG;
        }
        return Type.STRING;
    }

    public String addVariableField(String variable) {
        if (isVariable(variable)) {
            addVariable(variable, Type.STRING);
            return variable;
        }
        return makeString(variable);
    }
    private static String makeLongVariable(String value, boolean isOnlyString) {
         if(!isOnlyString){
             return makeLongVariable(value);
         }
         //TODO exception
         return value;
    }
    public String makeVariableFromString(String value){
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
            addVariable(variable, type);
            return variable;
        }
        if (makeType(variable) == Type.LONG) {
            variable = makeLongVariable(variable,isOnlyString);
            return variable;
        }
        return makeString(variable);
    }
}
