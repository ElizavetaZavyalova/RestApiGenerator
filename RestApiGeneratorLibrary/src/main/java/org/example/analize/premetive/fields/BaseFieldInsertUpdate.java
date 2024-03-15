package org.example.analize.premetive.fields;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.BaseFieldParser;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import static org.example.analize.premetive.fields.BaseFieldInsertUpdate.RegExp.*;
import static org.example.analize.premetive.fields.BaseFieldInsertUpdate.RegExpPattern.*;

public abstract class BaseFieldInsertUpdate<C,N> extends BaseField<C> {
    protected BaseFieldParser.Type type;
    protected String defaultValue;
    boolean isDefaultExist(){
        return defaultValue!=null;
    }
    protected BaseFieldInsertUpdate(String name, String tableName, Endpoint parent)throws IllegalArgumentException {
        super();
        name=deleteEndSetDefaultValue(name);
        type=BaseFieldParser.Type.getType(name, BaseFieldParser.Type.STRING);
        name=BaseFieldParser.Type.deleteType(name,type);
        super.init(name,tableName,parent);
    }
    public boolean isTypeString(){
        return type.equals(BaseFieldParser.Type.STRING);
    }
    public abstract N getType();

    record RegExp(){
        static final int LENGTH_IF_CONTAINS_PORT=2;
        static final int NAME_PORT=0;
        static final int INFO_PORT=1;
        static final String SPLIT_EQUAL="(?<!\\\\)=";
        static final String REPLACE_EQUAL="\\=";
        static final String EQUAL="=";

    }
   String deleteEndSetDefaultValue(String name) throws IllegalArgumentException{
        String[] ports=name.split(SPLIT_EQUAL);
        if(ports.length==LENGTH_IF_CONTAINS_PORT){
            defaultValue=ports[INFO_PORT].replace(REPLACE_EQUAL,EQUAL);
            return ports[NAME_PORT];
        }
        if(ports.length>LENGTH_IF_CONTAINS_PORT){
            throw new IllegalArgumentException(name+": must be like name:type=defaultValue");
        }
       throwExceptionIfDefaultValueIsNotCorrect();
        return name;
    }
    record RegExpPattern(){
        static final String IS_DIGIT="^-?\\d+$";
        static final String IS_FLOAT="^-?\\d+.\\d+$";
        static final String IS_BOOL="^(true|false)$";
    }
    boolean isDefaultReal(){
        return defaultValue.matches(IS_FLOAT);
    }
    boolean isDefaultDigit(){
        return defaultValue.matches(IS_DIGIT);
    }
    boolean isDefaultBoolean(){
        return defaultValue.matches(IS_BOOL);
    }
    void throwExceptionIfDefaultValueIsNotCorrect(){
        if(!isDefaultExist()){
            return;
        }
        if((type.equals(BaseFieldParser.Type.INTEGER)||type.equals(BaseFieldParser.Type.LONG))
                &&!isDefaultDigit()){
               throw new IllegalArgumentException(name+": not correct default value of Long or Int");
        }
        if((type.equals(BaseFieldParser.Type.FLOAT)||type.equals(BaseFieldParser.Type.DOUBLE))
                &&!isDefaultReal()){
            throw new IllegalArgumentException(name+": not correct default value of Float or Double");
        }
        else if(type.equals(BaseFieldParser.Type.BOOLEAN)
                && (!isDefaultBoolean())){
                throw new IllegalArgumentException(name+": not correct default value of bool");

        }
    }
    public String getDefaultValue(){
        if(!isDefaultExist()){
            return "null";
        }
         if(isTypeString()){
             return "\""+defaultValue+"\"";
         }
         return defaultValue;
    }


    public  String getExample(){
        return CodeBlock.builder().add("$S:",name).add(getDefaultValue()).build().toString();
    }

}
