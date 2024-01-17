package org.example.read_json.rest_controller_json.filter;
import lombok.Getter;
import org.example.read_json.rest_controller_json.Endpoint;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filtering;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.AndFilter;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.CallFilter;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.OrFilter;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.SqlFilter;

import java.util.*;
import java.util.stream.Collectors;
import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames;
import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames.*;

public abstract class Filters {
    @Getter
    Map<String, Filtering<String>> filters=new HashMap<>();

    public Filters(Map<String,String> filters) throws IllegalArgumentException{
        filters.forEach(this::addFilter);
    }
    public boolean isFilterExist(String key){
        return filters.containsKey(key);
    }
    public Filtering<String> getFilterIfExist(String key) throws IllegalArgumentException{
        if(isFilterExist(key)) {
            filters.get(key);
        }
        throw new IllegalArgumentException("FILTER "+key+" NOT EXIST");
    }

    void addKeyValToFilters(String key, Filtering<String> filter) throws IllegalArgumentException{
        if(filters.containsKey(key)){
            throw new  IllegalArgumentException("FILTER:"+key+"IS ALREADY EXIST");
        }
        if(key.isEmpty()){
            throw new  IllegalArgumentException("FILTER CANT BE EMPTY");
        }
        filters.put(key,filter);
    }

    void addFilter(String key, String val) throws IllegalArgumentException{
        if(key.endsWith(SQL.getName())){
            key= key.substring(0, key.length()-SQL.length());
            addKeyValToFilters(key,new SqlFilter(val));
            return;
        }
        else if(key.endsWith(OR.getName())){
            key=key.substring(0, key.length()-OR.length());
            addKeyValToFilters(key,new OrFilter(Arrays.stream(val.split("[|]")).toList()));
            return;
        }
        else if(key.endsWith(AND.getName())){
            key=key.substring(0, key.length()-AND.length());
            addKeyValToFilters(key,new AndFilter(Arrays.stream(val.split("[|]")).toList()));
            return;
        }
        else if(key.endsWith(CALL.getName())){
            key=key.substring(0, key.length()-CALL.length());
            addKeyValToFilters(key,new CallFilter(val));
            return;
        }
        throw new IllegalArgumentException("FILTER"+key +"MUST END ON "+
                Arrays.stream(values()).map(FilterNames::getName).collect(Collectors.joining(" OR ")));
    }


}
