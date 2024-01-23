package org.example.read_json.rest_controller_json;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.filter.EndpointFilters;
import org.example.read_json.rest_controller_json.filter.Filters;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filter;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filtering;
import org.example.read_json.rest_controller_json.pseudonyms.EndpointPseudonyms;
import org.example.read_json.rest_controller_json.pseudonyms.Pseudonyms;

import java.util.*;
import java.util.stream.Collectors;

import static org.example.read_json.rest_controller_json.Endpoint.KeyWords.*;
@Slf4j
public class Endpoint {
    @Getter
    String funcName;
    String request;
     Type type;
    Boolean perms = false;
    String call;
    String requestBd;
    String swagger;
    Filters filters;
    Pseudonyms pseudonyms;
    @Getter
    Endpoints parent;
    Endpoint(Map<String, Object> enpointMap,Endpoints parent) {
        try {
            this.parent=parent;
            makeRequest(enpointMap);
            perms = MakeCast.makeBoolean(enpointMap, PERMS, false);
            type =  new Type(MakeCast.makeString(enpointMap, TYPES, true));
            swagger = MakeCast.makeString(enpointMap, SWAGGER, true);
            filters=new EndpointFilters(MakeCast.makeStringMap(enpointMap,FILTERS,false),this);
            pseudonyms=new EndpointPseudonyms(MakeCast.makeMapOfMapOfList(enpointMap,PSEUDONYMS,false),this);
        } catch (IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            //TODO stop compile
        }
    }
    public Filtering<String> getFilter(String key) throws  IllegalArgumentException{
        if(filters.isFilterExist(key)){
            return filters.getFilterIfExist(key);
        }
        return parent.getParent().getFilters().getFilterIfExist(key);
    }

    public String getRealTableName(String key) {
        if (pseudonyms.isContainsTablePseudonym(key)) {
            return pseudonyms.getRealTableName(key);
        }
        if(parent.getParent().getPseudonyms().isContainsTablePseudonym(key)){
            return parent.getParent().getPseudonyms().getRealTableName(key);
        }
        return key;
    }

    public String getRealFieldName(String key) {
        if (pseudonyms.isContainsFieldPseudonym(key)) {
            return pseudonyms.getRealFieldName(key);
        }
        if(parent.getParent().getPseudonyms().isContainsFieldPseudonym(key)){
            return parent.getParent().getPseudonyms().getRealFieldName(key);
        }
        return key;
    }
    public List<String> getRealJoinName(String t1, String t2) {
        String key=t1+":"+t2;
        if (pseudonyms.isContainsJoinPseudonym(key)) {
            return pseudonyms.getRealJoinsName(key);
        }
        if(parent.getParent().getPseudonyms().isContainsJoinPseudonym(key)){
            return parent.getParent().getPseudonyms().getRealJoinsName(key);
        }
        return List.of();
    }
    void makeRequest(Map<String, Object> enpointMap){
        request = MakeCast.makeString(enpointMap, REQUEST,true);
        requestBd=MakeCast.makeString(enpointMap, REQUEST_BD, false);
        if(requestBd.isEmpty()){
            requestBd=request;
            request=null;
        }
        else if(requestBd.startsWith(CALL)){
            call=requestBd.replace(CALL,"");
            requestBd=CALL;
        }
    }

    record KeyWords() {
        static String REQUEST = "request";
        static String PERMS = "perms";
        static String TYPES = "types";
        static String REQUEST_BD="request_bd";
        static String CALL="call:";
        static String FILTERS="filters";
        static  String PSEUDONYMS="pseudonyms";


        record Regexp() {
            static String REQUEST_TYPE_SEPARATOR = "\\|";
        }
        static String SWAGGER = "swagger";
    }

}
