package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import lombok.Getter;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.ReadJson;
import org.example.read_json.rest_controller_json.*;
import org.example.read_json.rest_controller_json.filter.EndpointFilters;
import org.example.read_json.rest_controller_json.filter.Filters;

import org.example.read_json.rest_controller_json.filter.filters_vies.Filtering;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter.ListStringFilter;
import org.example.read_json.rest_controller_json.pseudonyms.EndpointPseudonyms;
import org.example.read_json.rest_controller_json.pseudonyms.Pseudonyms;


import java.util.*;

import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.PERMS;
import static org.example.read_json.rest_controller_json.JsonKeyWords.FILTERS;
import static org.example.read_json.rest_controller_json.JsonKeyWords.PSEUDONYMS;

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
    private ReadJson readeJson = new ReadJson();

    public Endpoint(Map<String, Object> enpointMap, Endpoints parent, String funcName) throws IllegalArgumentException {
        this.funcName = funcName;
        this.parent = parent;
        filters = new EndpointFilters(readeJson.loadFilters(enpointMap), this);
        pseudonyms = new EndpointPseudonyms(readeJson.loadPseudonyms(enpointMap), this);
        requestInformation = new RequestInformation(enpointMap,this);

    }
    public List<String> findPath(String table1 ,String table2,boolean real){
        List<String> result=pseudonyms.findPath(table1,table2,real);
        if(result.size()!=2){
            return result;
        }
        return parent.getParent().getPseudonyms().findPath(table1,table2,real);
    }

    public List<MethodSpec> getDBMethods() throws IllegalArgumentException {
        this.requestInformation.generateBd(this);
        List<MethodSpec> list = new ArrayList<>();
        list.addAll(requestInformation.makeDBMethods(funcName));
        List<String> useFilters = requestInformation.getVarInfos().stream()
                .filter(VarInfo::isFilter)
                .map(VarInfo::getName).distinct().toList();
        for (String useFilter : useFilters) {
            Filtering<CodeBlock> filtering = getFilter(useFilter);
            if (filtering instanceof ListStringFilter) {
                ListStringFilter filter = (ListStringFilter) filtering;
                list.add(filter.makeFilterMethod(this));
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
