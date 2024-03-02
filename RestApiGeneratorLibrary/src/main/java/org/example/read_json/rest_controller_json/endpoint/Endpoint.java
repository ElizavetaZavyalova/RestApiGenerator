package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import lombok.Getter;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.ReadJson;
import org.example.read_json.rest_controller_json.*;
import org.example.read_json.rest_controller_json.filter.EndpointFilters;
import org.example.read_json.rest_controller_json.filter.Filters;

import org.example.read_json.rest_controller_json.filter.RestJsonFilters;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filtering;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter.ListStringFilter;
import org.example.read_json.rest_controller_json.pseudonyms.EndpointPseudonyms;
import org.example.read_json.rest_controller_json.pseudonyms.Pseudonyms;
import org.example.read_json.rest_controller_json.pseudonyms.RestJsonPseudonyms;


import java.util.*;

import static org.example.read_json.rest_controller_json.JsonKeyWords.ADDRESS_PREFIX;

@Slf4j
public class Endpoint {
    @Getter
    RequestInformation requestInformation;
    @Getter
    String funcName;
    Filters filters;
    Pseudonyms pseudonyms;
    @Getter
    Endpoints parent;

    public Endpoint(Map<String, Object> enpointMap, Endpoints parent, String funcName) throws IllegalArgumentException {
        try {
            this.funcName = funcName;
            this.parent = parent;
            ReadJson readeJson = new ReadJson();
            filters = new EndpointFilters(readeJson.loadFilters(enpointMap), this);
            pseudonyms = new EndpointPseudonyms(readeJson.loadPseudonyms(enpointMap), this);
            requestInformation = new RequestInformation(enpointMap,this);
        }catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("In: "+funcName+" "+ex.getMessage());
        }

    }
    public List<String> findPath(String table1 ,String table2,boolean real){
        List<String> result=pseudonyms.findPath(table1,table2,real);
        if(result.size()!=2){
            return result;
        }
        return parent.getParent().getPseudonyms().findPath(table1,table2,real);
    }
    public void generate(){
        this.requestInformation.generateBd(this);
        this.requestInformation.generateVarInfos();
    }

    public List<MethodSpec> getDBMethods() throws IllegalArgumentException {
        List<MethodSpec> list = new ArrayList<>(requestInformation.makeDBMethods(funcName));
        List<String> useFilters = requestInformation.getFilterInfos().stream()
                .map(FilterInfo::getFilterName).distinct().toList();
        for (String useFilter : useFilters) {
            Filtering<CodeBlock> filtering = getFilter(useFilter);
            if (filtering instanceof ListStringFilter listStringFilter) {
                list.add(listStringFilter.makeFilterMethod(this));
            }
        }
        return list;
    }

    public List<MethodSpec> getControllerMethods(String beanName) throws IllegalArgumentException {
        return requestInformation.makeControllerMethods(funcName, beanName);
    }

    public Filtering<CodeBlock> getFilter(String key) throws IllegalArgumentException {
        if (filters.isFilterExist(key)) {
            return filters.getFilterIfExist(key);
        }
        return parent.getParent().getFilters().getFilterIfExist(key);
    }

    public String getRealTableName(String key) {
        if (pseudonyms.isContainsTablePseudonym(key)) {
            return pseudonyms.getRealTableName(key);
        }
        return parent.getParent().getPseudonyms().getRealTableName(key);
    }

    public List<String> getEntity(String key) {
        if (pseudonyms.isContainsEntityPseudonym(key)) {
            return pseudonyms.getRealEntity(key);
        }
        return parent.getParent().getPseudonyms().getRealEntity(key);
    }

    public String getRealFieldName(String key) {
        if (pseudonyms.isContainsFieldPseudonym(key)) {
            return pseudonyms.getRealFieldName(key);
        }
        return parent.getParent().getPseudonyms().getRealFieldName(key);
    }

    public List<String> getRealJoinName(String t1, String t2) {
        String key = t1 + ":" + t2;
        if (pseudonyms.isContainsJoinPseudonym(key)) {
            return pseudonyms.getRealJoinsName(key);
        }
        if (parent.getParent().getPseudonyms().isContainsJoinPseudonym(key)) {
            return parent.getParent().getPseudonyms().getRealJoinsName(key);
        }
        return new ArrayList<>();
    }


}
