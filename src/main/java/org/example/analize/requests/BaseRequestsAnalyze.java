package org.example.analize.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BaseRequestsAnalyze {
    Map<String,List<TypeAnalyze>> requests=new TreeMap<>();
    public record ParseRequest(){
        public static String[] splitString(String request){
            String[] result= request.split("/");
            for(String string:result){
                if(!isValue(string)){
                    string.replaceAll(" ","");
                }
            }
            return result;
        }

        private static boolean isValue(String value){
            return (value.charAt(0)=='{')&&(value.charAt(value.length()-1)=='}');
        }

        static boolean isTable(String[] requests){
            if(requests.length!=1){
                return false;
            }
            return (!isValue(requests[0]));
        }
        static boolean isTableId(String[] requests){
            if(requests.length!=2){
                return false;
            }
            return  (!isValue(requests[0]))&&(isValue(requests[1]));
        }
        static boolean isTableField(String[] requests){
            if(requests.length!=2){
                return false;
            }
            return (!isValue(requests[0]))&&(!isValue(requests[1]));
        }
        static boolean isTableFieldId(String[] requests){
            if(requests.length!=3){
                return false;
            }
            return (!isValue(requests[0]))&&(!isValue(requests[1]))&&(isValue(requests[2]));
        }
        static boolean  isTableIdNext(String[] requests){
            if(requests.length!=3){
                return false;
            }
            return (!isValue(requests[0]))&&(isValue(requests[1]))&&(requests[2].equals("next"));
        }
        static boolean  isTableIdTable(String[] requests){
            if(requests.length!=3){
                return false;
            }
            return (!isValue(requests[0]))&&(isValue(requests[1]))&&(!isValue(requests[2]));
        }
        static boolean  isTableIdTableId(String[] requests){
            if(requests.length!=4){
                return false;
            }
            return (!isValue(requests[0]))&&(isValue(requests[1]))&&(!isValue(requests[2]))&&(isValue(requests[3]));
        }
        public static Type makeType(String[] request){
            if(isTableIdNext(request)){
                return Type.TABLE_ID_NEXT;
            }
            else if(isTableFieldId(request)){
                return Type.TABLE_FIELD_ID;
            }
            else if(isTableField(request)){
                return Type.TABLE_FIELD;
            }
            else if(isTableId(request)){
                return Type.TABLE_ID;
            }
            else if(isTable(request)){
                return Type.TABLE;
            }
            return Type.NOT_CORRECT;
        }
        static List<String[]> makeNextAnalyzeStrings(String[] request, int index){
            String[] first=new String[1];
            String[] second=new String[2];
            String[] fired=new String[3];
            ArrayList<String[]> strings=new ArrayList<>();
            if(request.length-index==1){
                first[0]=request[index];
                strings.add(first);

            }
            else if(request.length-index==2){
                first[0]=request[index];
                second[0]=request[index];second[1]=request[index+1];
                strings.add(second);
                strings.add(first);

            }  else {
                first[0] = request[index];
                second[0] = request[index];
                second[1] = request[index + 1];
                fired[0] = request[index];
                fired[1] = request[index + 1];
                fired[2] = request[index + 2];
                strings.add(fired);
                strings.add(second);
                strings.add(first);
            }
            return strings;
        }
        public static List<TypeAnalyze> makeTypeAnalyzes(String[] request){
            List<TypeAnalyze> typeAnalyzes=new ArrayList<>();
            int index=0;
            while(index!= request.length){
                List<String[]> strings=makeNextAnalyzeStrings(request,index);
                index+=strings.size();
                Type type=Type.NOT_CORRECT;
                for(String[] currentRequest:strings){
                    TypeAnalyze typeAnalyze=new TypeAnalyze();
                    type=makeType(currentRequest);
                    if(!type.equals(Type.NOT_CORRECT)){
                        typeAnalyze.type=type;
                        typeAnalyze.ident=currentRequest;
                        typeAnalyzes.add(typeAnalyze);
                        break;
                    }
                }
                if(type.equals(Type.NOT_CORRECT)){
                    return new ArrayList<>();
                }
            }
            return typeAnalyzes;
        }
    }
    String makeValue(String value){
        return value.substring(1, value.length() - 1);
    }
    public boolean addRequest(String request){
       List<TypeAnalyze> types=ParseRequest.makeTypeAnalyzes(ParseRequest.splitString(request));
       if(types.isEmpty()){
           return false;
       }
       requests.put(request,types);
       return true;
    }


}
