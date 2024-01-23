package org.example.read_json.rest_controller_json;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.read_json.rest_controller_json.Type.RegExp.*;
@Getter
public class Type {
    final RequestType requestType;
    List<String> params=new ArrayList<>();
    record RegExp(){
        public static final String SPLIT_TYPE=":";
        public static final String SPLIT_PARAMS="[|]";
        public static final int MAX_COUNT=2;
        public static final int NO_PARAM_COUNT=1;
        public static final int TYPE_PORT=0;
        public static final int LIST_PORT=1;
    }
    Type(String param) throws IllegalArgumentException{
       String[] description=param.split(SPLIT_TYPE);
       if(description.length>MAX_COUNT){
           throw new IllegalArgumentException(param+"MUST be type:param1|param2");
       }
        requestType=RequestType.fromName(description[TYPE_PORT]);
       if(!requestType.equals(RequestType.DELETE)){
           if(requestType.equals(RequestType.GET)&&description.length==NO_PARAM_COUNT){
               return;
           }
           params= Arrays.stream(description[LIST_PORT].split(SPLIT_PARAMS)).filter(s->!s.isEmpty()).toList();
       }
        if(requestType.equals(RequestType.DELETE)&&description.length!=NO_PARAM_COUNT){
            throw new IllegalArgumentException(param+"MUST be delete");
        }
    }

}
