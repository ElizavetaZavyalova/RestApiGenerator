package org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter;

import com.squareup.javapoet.CodeBlock;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.helpclass.CreateEndpoint;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filter;

import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.example.analize.helpclass.CreateEndpoint.*;
import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames.*;
@Slf4j
public class FilterTest {
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }
    String filterName="filterName";
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsFilters")
    void filter(Filter.FilterNames name, List<String> list){
        ListManyParamsFilter filter=new ListManyParamsFilter(name, list, filterName);
        log.info(filter.makeFilterMethod(CreateEndpoint.makeEndpoint()).toString());
        log.info(filter.makeFilter(funcName,"table", CodeBlock.builder().add("DSL.trueCondition()").build()).toString());
        //TODO for all filters
    }
    static public Stream<Arguments> constructorParamsFilters() {
        return Stream.of(
                Arguments.of(OR, List.of(fieldName1,fieldName2,fieldName3,fieldName4)),
                Arguments.of(OR, List.of()),
                Arguments.of(OR, List.of(fieldName1)));
    }

}
