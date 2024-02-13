package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.MethodSpec;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.helpclass.CreateEndpoint;
import org.example.read_json.ReadJson;
import org.example.read_json.rest_controller_json.Endpoints;
import org.example.read_json.rest_controller_json.RestJson;
import org.example.read_json.rest_controller_json.filter.RestJsonFilters;
import org.example.read_json.rest_controller_json.pseudonyms.RestJsonPseudonyms;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Request.TableRef._ONE_TO_MANY;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class EndpointTest {

    static Endpoints createParent() {
        Endpoints points = Mockito.mock(Endpoints.class);
        RestJson rest = Mockito.mock(RestJson.class);
        Mockito.doReturn(rest).when(points).getParent();

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
                .thenAnswer(invocation -> true);

        Mockito.when(pseudonyms.findPath(Mockito.any(String.class), Mockito.any(String.class)))
                .thenAnswer(invocation -> List.of(invocation.getArguments()[0], invocation.getArguments()[1]));
        Mockito.doReturn(pseudonyms).when(rest).getPseudonyms();

        RestJsonFilters filters = Mockito.mock(RestJsonFilters.class);
        Mockito.when(filters.isFilterExist(Mockito.any(String.class)))
                .thenAnswer(invocation -> false);
        Mockito.doReturn(filters).when(rest).getFilters();
        return points;
    }

    static Map<String, Object> jsonRequest;
    String funcName = "funcName";
    static Map<String, Object> jsonEndpoint;

    @BeforeAll
    @SneakyThrows
    static void loadJson() {
        ReadJson readJson = new ReadJson();
        jsonEndpoint = readJson.load("P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\RestApiGeneratorLibrary\\src\\test\\resources\\endpoint\\endpoints.json");
        jsonRequest = readJson.load("P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\RestApiGeneratorLibrary\\src\\test\\resources\\requestInformation\\requestInformationTest.json");
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("requestTest")
    void generateRequestTest(String name) {
        Map<String, Object> object = (Map<String, Object>) jsonRequest.get(name);
        log.info(object.toString());
        Endpoint information = new Endpoint(object, createParent(), funcName);
        log.info("\n" + information.getDBMethods().stream().map(i -> i.toString()).collect(Collectors.joining("\n")));

    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("endpointFilter")
    void generateEndpointFilterTest(String name) {
        Map<String, Object> object = (Map<String, Object>) jsonEndpoint.get(name);
        log.info(object.toString());
        Endpoint information = new Endpoint(object, createParent(), funcName);
        log.info("\n" + information.getDBMethods().stream().map(MethodSpec::toString).collect(Collectors.joining("\n")));

    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("requestThrow")
    void generateRequestTestThrow(String name) {
        Map<String, Object> object = (Map<String, Object>) jsonRequest.get(name);
        log.info(object.toString());
        var ex = assertThrows(IllegalArgumentException.class, () -> new Endpoint(object, createParent(), funcName));
        log.info(ex.getMessage());
    }



    static public Stream<Arguments> requestTest() {
        return jsonRequest.keySet().stream().filter(k -> !k.startsWith("Throw"))
                .map(Arguments::of).toList().stream();
    }

    static public Stream<Arguments> requestThrow() {
        return jsonRequest.keySet().stream().filter(k -> k.startsWith("Throw")).filter(k -> !k.startsWith("ThrowInRequestPostWhere"))
                .map(Arguments::of).toList().stream();
    }

    static public Stream<Arguments> endpointFilter() {
        return jsonEndpoint.keySet().stream().filter(k -> k.startsWith("Filter"))
                .map(Arguments::of).toList().stream();
    }


}