package org.example.read_json.rest_controller_json.filter;

import com.squareup.javapoet.CodeBlock;
import lombok.Getter;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filtering;

import org.example.read_json.rest_controller_json.filter.filters_vies.filters.CallFilter;

import org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter.ListStringFilter;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static org.example.read_json.rest_controller_json.filter.Filters.Regexp.IS_CORRECT_FILTER_NAME;
import static org.example.read_json.rest_controller_json.filter.Filters.Regexp.SPLIT_PARAMS;
import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames;
import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames.*;

public abstract class Filters {
    @Getter
    Map<String, Filtering<CodeBlock>> filtersMap = new HashMap<>();
    protected void initParent(Map<String, String> filters) throws IllegalArgumentException{
        filters.forEach(this::addFilter);
    }
    record Regexp(){
        static final String IS_CORRECT_FILTER_NAME = "[a-zA-Z_]\\w*";
        static final String SPLIT_PARAMS = "[|]";
    }
    void throwException(String filterName) throws IllegalArgumentException {
        if (filterName.isEmpty()) {
            throw new IllegalArgumentException("no filter name");
        }
        if (!Pattern.matches(IS_CORRECT_FILTER_NAME, filterName)) {
            throw new IllegalArgumentException(filterName + "must starts on later or _ and contains later or _ or digit");
        }
    }

    public boolean isFilterExist(String key) {
        return filtersMap.containsKey(key);
    }

    public Filtering<CodeBlock> getFilterIfExist(String key) throws IllegalArgumentException {
        if (isFilterExist(key)) {
           return filtersMap.get(key);
        }
        throw new IllegalArgumentException("filter " + key + " not exist");
    }

    void addKeyValToFilters(String key, Filtering<CodeBlock> filter) throws IllegalArgumentException {
        throwException(key);
        if (filtersMap.containsKey(key)) {
            throw new IllegalArgumentException("filter: " + key + "is already exist");
        }
        if (key.isEmpty()) {
            throw new IllegalArgumentException("filter can't be empty");
        }
        filtersMap.put(key, filter);
    }

    abstract String makeFilterVoidName(String key);

    void addFilter(String key, String val) throws IllegalArgumentException {
        if (key.endsWith(OR.getName())) {
            key = key.substring(0, key.length() - OR.length());
            addKeyValToFilters(key, new ListStringFilter(FilterNames.OR, Arrays.stream(val.split(SPLIT_PARAMS)).toList(), makeFilterVoidName(key)));
            return;
        } else if (key.endsWith(AND.getName())) {
            key = key.substring(0, key.length() - AND.length());
            addKeyValToFilters(key, new ListStringFilter(FilterNames.AND, Arrays.stream(val.split(SPLIT_PARAMS)).toList(), makeFilterVoidName(key)));
            return;
        } else if (key.endsWith(CALL.getName())) {
            key = key.substring(0, key.length() - CALL.length());
            addKeyValToFilters(key, new CallFilter(val, makeFilterVoidName(key)));
            return;
        }
        throw new IllegalArgumentException("filter " + key + "must be end " +
                Arrays.stream(values()).map(FilterNames::getName).collect(Collectors.joining(" or ")));
    }


}
