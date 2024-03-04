package org.example.analize.premetive.fields;
import org.example.analize.premetive.BaseFieldParser;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import static org.example.analize.premetive.fields.BaseFieldReal.RegExp.*;

public abstract class BaseFieldReal<C,N> extends BaseField<C> {
    protected BaseFieldParser.Type type;
    protected String defaultValue=null;
    boolean isDefaultExist(){
        return defaultValue!=null;
    }
    protected BaseFieldReal(String name, String tableName, Endpoint parent)throws IllegalArgumentException {
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
    }
   String deleteEndSetDefaultValue(String name) throws IllegalArgumentException{
        String[] ports=name.split("=");
        if(ports.length==LENGTH_IF_CONTAINS_PORT){
            defaultValue=ports[INFO_PORT];
            return ports[NAME_PORT];
        }
        if(ports.length>LENGTH_IF_CONTAINS_PORT){
            throw new IllegalArgumentException(name+": must be like name:type=defaultValue");
        }
        return name;
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

}
