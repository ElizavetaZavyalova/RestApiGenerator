package org.example.read_json.rest_controller_json;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.ReadJson;

import org.example.read_json.rest_controller_json.filter.RestJsonFilters;
import org.example.read_json.rest_controller_json.pseudonyms.RestJsonPseudonyms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import java.util.stream.Stream;

import static org.example.read_json.rest_controller_json.EndpointsTest.Constants.*;


@Slf4j
class EndpointsTest {
    static RestJson createParent() {

        RestJson rest = Mockito.mock(RestJson.class);


        RestJsonPseudonyms pseudonyms = Mockito.mock(RestJsonPseudonyms.class);

        Mockito.when(pseudonyms.getRealTableName(Mockito.any(String.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);
        Mockito.when(pseudonyms.isContainsTablePseudonym(Mockito.any(String.class)))
                .thenAnswer(invocation -> false);

        Mockito.when(pseudonyms.getRealFieldName(Mockito.any(String.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);
        Mockito.when(pseudonyms.isContainsFieldPseudonym(Mockito.any(String.class)))
                .thenAnswer(invocation -> false);

        Mockito.when(pseudonyms.getRealEntity(Mockito.any(String.class)))
                .thenAnswer(invocation -> List.of(invocation.getArguments()[0]));
        Mockito.when(pseudonyms.isContainsEntityPseudonym(Mockito.any(String.class)))
                .thenAnswer(invocation -> false);

        Mockito.when(pseudonyms.getRealJoinsName(Mockito.any(String.class)))
                .thenAnswer(invocation -> List.of(":", ":"));
        Mockito.when(pseudonyms.isContainsJoinPseudonym(Mockito.any(String.class)))
                .thenAnswer(invocation -> false);

        Mockito.when(pseudonyms.findPath(Mockito.any(String.class), Mockito.any(String.class),Mockito.any(boolean.class)))
                .thenAnswer(invocation -> List.of(invocation.getArguments()[0], invocation.getArguments()[1]));
        Mockito.doReturn(pseudonyms).when(rest).getPseudonyms();

        RestJsonFilters filters = Mockito.mock(RestJsonFilters.class);
        Mockito.when(filters.isFilterExist(Mockito.any(String.class)))
                .thenAnswer(invocation -> false);
        Mockito.doReturn(filters).when(rest).getFilters();
        return rest;
    }
    static Map<String,Object> json;
    static Map<String,Object> postJson;
    record Constants(){
        static final String controllerName="ControllerName";
        static final String repositoryName="RepositoryName";
        static final String repositoryPath="path.repository";
        static final String beanName="bean";
    }
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }
    @BeforeAll
    @SneakyThrows
    static void loadJson(){
        ReadJson readJson=new ReadJson();
        json=readJson.load("src\\test\\resources\\endpoints\\endpoints.json");
        postJson=readJson.load("src\\test\\resources\\endpoints\\postTest.json");
    }
    void test(Map<String, Object> object,String name){
        log.info(name);
        log.info(object.toString());
        Endpoints points=new Endpoints(object,createParent());
        points.generate();
        log.info("\n"+points.createRepository(repositoryName,beanName).toString());
        log.info("\n"+points.createController(controllerName,repositoryName,repositoryPath).toString());
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("endpointsTest")
    void endpointsTest(String name) {
        test((Map<String, Object>) json.get(name),name);
    }
    static public Stream<Arguments> endpointsTest() {
        return json.keySet().stream().filter(k->k.startsWith("http"))
                .map(Arguments::of).toList().stream();
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("postTest")
    void postTest(String name) {
        test((Map<String, Object>) postJson.get(name),name);
    }
    static public Stream<Arguments> postTest() {
        return postJson.keySet().stream()
                .map(Arguments::of).toList().stream();
    }
}