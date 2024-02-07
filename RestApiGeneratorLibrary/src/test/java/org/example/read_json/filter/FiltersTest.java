package org.example.read_json.filter;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.helpclass.CreateEndpoint;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filter;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter.ListStringFilter;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.example.analize.helpclass.CreateEndpoint.*;
import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames.*;
@Slf4j
public class FiltersTest {
    String filterName="filterName";
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsFilters")
    void filter(Filter.FilterNames name, List<String> list){
        ListStringFilter filter=new ListStringFilter(name, list, filterName);
        log.info(filter.makeFilterMethod(CreateEndpoint.makeEndpoint()).toString());
        log.info(filter.makeFilter(funcName,"table","DSL.trueCondition()"));
    }
    static public Stream<Arguments> constructorParamsFilters() {
        return Stream.of(
                Arguments.of(OR, List.of(fieldName1,fieldName2,fieldName3,fieldName4)),
                Arguments.of(OR, List.of()),
                Arguments.of(OR, List.of(fieldName1)));
    }

}
