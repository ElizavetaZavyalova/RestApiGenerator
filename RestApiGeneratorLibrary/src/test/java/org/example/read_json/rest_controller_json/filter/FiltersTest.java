package org.example.read_json.rest_controller_json.filter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.ReadJson;
import org.example.read_json.rest_controller_json.RestJson;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.RequestInformation;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filtering;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class FiltersTest {
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }
    static Map<String,Object> json;
    @BeforeAll
    @SneakyThrows
    static void loadJson(){
        ReadJson readJson=new ReadJson();
        json=readJson.load("P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\RestApiGeneratorLibrary\\src\\test\\resources\\filters\\filters.json");
    }
    Endpoint createEndpoint(){
        Endpoint point= Mockito.mock(Endpoint.class);
        Mockito.when(point.getFuncName()).thenReturn("funcName");
        return point;
    }
    RestJson creteRestJson(){
        return Mockito.mock(RestJson.class);
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("typeTest")
    void filterTest(String name){
        Map<String,String>  map=(Map<String,String>)json.get(name);
        log.info(map.toString());
        Filters filters=new EndpointFilters(map,createEndpoint());
        log.info(filters.makeFilterVoidName("func"));
        log.info("isFilterExist(filter):"+filters.isFilterExist("filter"));
        log.info("isFilterExist(filterNo):"+filters.isFilterExist("filterNo"));
        if(filters.isFilterExist("filter")) {
            log.info(filters.getFilterIfExist("filter").toString());
        }
        var ex = assertThrows(IllegalArgumentException.class, ()->((EndpointFilters)filters).getFilterIfExist("filterNo"));
        log.info(ex.getMessage());
        Filters filtersRest=new RestJsonFilters(map,creteRestJson());
        log.info(filtersRest.makeFilterVoidName("func"));
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("typeTestThrow")
    void filterTestThrow(String name){
        Map<String,String>  map=(Map<String,String>)json.get(name);
        log.info(map.toString());
        var ex = assertThrows(IllegalArgumentException.class, ()->new EndpointFilters(map,createEndpoint()));
        log.info(ex.getMessage());

    }
    static public Stream<Arguments> typeTest() {
        return json.keySet().stream().filter(k->!k.startsWith("Throw"))
                .map(Arguments::of).toList().stream();
    }
    static public Stream<Arguments> typeTestThrow() {
        return json.keySet().stream().filter(k->k.startsWith("Throw"))
                .map(Arguments::of).toList().stream();
    }
}