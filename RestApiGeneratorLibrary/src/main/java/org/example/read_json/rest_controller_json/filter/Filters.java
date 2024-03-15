package org.example.read_json.rest_controller_json.filter;

import com.squareup.javapoet.CodeBlock;
import lombok.Getter;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filtering;

import org.example.read_json.rest_controller_json.filter.filters_vies.filters.CallFilter;

import org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter.ListManyParamsFilter;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter.ListOneParamFilter;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.example.read_json.rest_controller_json.filter.Filters.Regexp.*;
import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames;
import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames.*;

@Getter
public abstract class Filters {
    Map<String, Filtering<CodeBlock>> filtersMap = new HashMap<>();

    protected void initParent(Map<String, String> filters) throws IllegalArgumentException {
        filters.forEach(this::addFilter);
    }

    record Regexp() {
        static final String IS_CORRECT_FILTER_NAME = "[a-zA-Z_]\\w*";
        static final String SPLIT_PARAMS = "[|]";
        static final String SPLIT_NAME_IN_REQUEST = "[=]";
        static final int KEY_PORT = 0;
        static final int KEY_PORT_COUNT = 2;
        static final int NOT_DELETE_EMPTY_STRING = -1;
        static final int NAME_IN_REQUEST_PORT = 1;
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

    String nameInRequest(String[] keyPorts, String key) {
        return keyPorts.length == KEY_PORT_COUNT ? (keyPorts[NAME_IN_REQUEST_PORT]) : key;
    }

    void addFilter(String key, String val) throws IllegalArgumentException {
        String[] keyPorts = key.split(SPLIT_NAME_IN_REQUEST, NOT_DELETE_EMPTY_STRING);
        key = keyPorts[KEY_PORT];
        if (key.endsWith(OR.getName())) {
            key = key.substring(0, key.length() - OR.length());
            addKeyValToFilters(key, new ListManyParamsFilter(FilterNames.OR, key, Arrays.stream(val.split(SPLIT_PARAMS)).toList(), makeFilterVoidName(key), nameInRequest(keyPorts, key)));
            return;
        } else if (key.endsWith(AND.getName())) {
            key = key.substring(0, key.length() - AND.length());
            addKeyValToFilters(key, new ListManyParamsFilter(FilterNames.AND, key, Arrays.stream(val.split(SPLIT_PARAMS)).toList(), makeFilterVoidName(key), nameInRequest(keyPorts, key)));
            return;
        } else if (key.endsWith(NOT_OR.getName())) {
            key = key.substring(0, key.length() - NOT_OR.length());
            addKeyValToFilters(key, new ListManyParamsFilter(FilterNames.NOT_OR, key, Arrays.stream(val.split(SPLIT_PARAMS)).toList(), makeFilterVoidName(key), nameInRequest(keyPorts, key)));
            return;
        } else if (key.endsWith(NOT_AND.getName())) {
            key = key.substring(0, key.length() - NOT_AND.length());
            addKeyValToFilters(key, new ListManyParamsFilter(FilterNames.NOT_AND, key, Arrays.stream(val.split(SPLIT_PARAMS)).toList(), makeFilterVoidName(key), nameInRequest(keyPorts, key)));
            return;
        } else if (key.endsWith(ONE_OR.getName())) {
            key = key.substring(0, key.length() - ONE_OR.length());
            addKeyValToFilters(key, new ListOneParamFilter(FilterNames.ONE_OR, key, Arrays.stream(val.split(SPLIT_PARAMS)).toList(), makeFilterVoidName(key), nameInRequest(keyPorts, key)));
            return;
        } else if (key.endsWith(ONE_AND.getName())) {
            key = key.substring(0, key.length() - ONE_AND.length());
            addKeyValToFilters(key, new ListOneParamFilter(FilterNames.ONE_AND, key, Arrays.stream(val.split(SPLIT_PARAMS)).toList(), makeFilterVoidName(key), nameInRequest(keyPorts, key)));
            return;
        } else if (key.endsWith(ONE_NOT_OR.getName())) {
            key = key.substring(0, key.length() - ONE_NOT_OR.length());
            addKeyValToFilters(key, new ListOneParamFilter(FilterNames.ONE_NOT_OR, key, Arrays.stream(val.split(SPLIT_PARAMS)).toList(), makeFilterVoidName(key), nameInRequest(keyPorts, key)));
            return;
        } else if (key.endsWith(ONE_NOT_AND.getName())) {
            key = key.substring(0, key.length() - ONE_NOT_AND.length());
            addKeyValToFilters(key, new ListOneParamFilter(FilterNames.ONE_NOT_AND, key, Arrays.stream(val.split(SPLIT_PARAMS)).toList(), makeFilterVoidName(key), nameInRequest(keyPorts, key)));
            return;
        } else if (key.endsWith(CALL.getName())) {
            key = key.substring(0, key.length() - CALL.length());
            addKeyValToFilters(key, new CallFilter(val, key, makeFilterVoidName(key), nameInRequest(keyPorts, key)));
            return;
        }
        throw new IllegalArgumentException("filter " + key + "must be end " +
                Arrays.stream(values()).map(FilterNames::getName).collect(Collectors.joining(" or ")));
    }


}
