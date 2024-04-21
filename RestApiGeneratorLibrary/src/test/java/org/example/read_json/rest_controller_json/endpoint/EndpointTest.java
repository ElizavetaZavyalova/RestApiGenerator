package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.MethodSpec;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.helpclass.CreateEndpoint;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.ReadJson;
import org.example.read_json.rest_controller_json.Endpoints;
import org.example.read_json.rest_controller_json.RestJson;
import org.example.read_json.rest_controller_json.filter.RestJsonFilters;
import org.example.read_json.rest_controller_json.pseudonyms.RestJsonPseudonyms;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Request.TableRef._ONE_TO_MANY;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class EndpointTest {
    @BeforeAll
    static void setDebug() {
        DefaultsVariablesName.DEBUG = true;
    }

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
                .thenAnswer(invocation -> false);

        Mockito.when(pseudonyms.findPath(Mockito.any(String.class), Mockito.any(String.class), Mockito.any(boolean.class)))
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
    String beanName = "bean";
    static Map<String, Object> jsonEndpoint;
    static Map<String, Object> jsonPseudonyms;

    @BeforeAll
    @SneakyThrows
    static void loadJson() {
        ReadJson readJson = new ReadJson();
        jsonEndpoint = readJson.load("src\\test\\resources\\endpoint\\endpoint.json");
        jsonRequest = readJson.load("src\\test\\resources\\requestInformation\\requestInformationTest.json");
        jsonPseudonyms = readJson.load("src\\test\\resources\\pseudonyms\\pseudonyms.json");
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("requestTest")
    void requestTest(String name) {
        test((Map<String, Object>) jsonRequest.get(name), name);

    }


    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("requestThrow")
    void requestTestThrow(String name) {
        Map<String, Object> object = (Map<String, Object>) jsonRequest.get(name);
        log.info(object.toString());
        var ex = assertThrows(IllegalArgumentException.class, () -> new Endpoint(object, createParent(), funcName));
        log.info(ex.getMessage());
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("pseudonymsThrow")
    void pseudonymsTestThrow(String namePseudonyms, String nameEndpoint) {
        Map<String, Object> pseudonyms = (Map<String, Object>) jsonPseudonyms.get(namePseudonyms);
        Map<String, Object> object = (Map<String, Object>) jsonEndpoint.get(nameEndpoint);
        object.put("pseudonyms", pseudonyms);
        log.info(object.toString());
        var ex = assertThrows(IllegalArgumentException.class, () -> new Endpoint(object, createParent(), funcName));
        log.info(ex.getMessage());
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("endpointThrow")
    void pseudonymsTestThrow(String name) {
        Map<String, Object> object = (Map<String, Object>) jsonEndpoint.get(name);
        log.info(object.toString());
        var ex = assertThrows(IllegalArgumentException.class, () -> new Endpoint(object, createParent(), funcName));
        log.info(ex.getMessage());
    }

    static public Stream<Arguments> pseudonymsThrow() {
        return jsonPseudonyms.keySet().stream().filter(k -> k.startsWith("Throw"))
                .map(v -> Arguments.of(v, "EmptyEndpoint")).toList().stream();
    }

    static public Stream<Arguments> endpointThrow() {
        return jsonEndpoint.keySet().stream().filter(k -> k.startsWith("Throw"))
                .map(Arguments::of).toList().stream();
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

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("endpointFilter")
    void endpointFilterTest(String name) {
        test((Map<String, Object>) jsonEndpoint.get(name), name);

    }

    static public Stream<Arguments> endpointPseudonymsEntity() {
        return jsonEndpoint.keySet().stream().filter(k -> k.startsWith("PseudonymsEntity"))
                .map(Arguments::of).toList().stream();
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("endpointPseudonymsEntity")
    void endpointPseudonymsEntity(String name) {
        test((Map<String, Object>) jsonEndpoint.get(name), name);

    }

    static public Stream<Arguments> endpointPseudonymsTables() {
        return jsonEndpoint.keySet().stream().filter(k -> k.startsWith("PseudonymsTables"))
                .map(Arguments::of).toList().stream();
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("endpointPseudonymsTables")
    void endpointPseudonymsTables(String name) {
        test((Map<String, Object>) jsonEndpoint.get(name), name);
    }

    static public Stream<Arguments> endpointPseudonymsFields() {
        return jsonEndpoint.keySet().stream().filter(k -> k.startsWith("PseudonymsFields"))
                .map(Arguments::of).toList().stream();
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("endpointPseudonymsFields")
//PseudonymsJoins
    void endpointPseudonymsFields(String name) {
        test((Map<String, Object>) jsonEndpoint.get(name), name);

    }

    static public Stream<Arguments> endpointPseudonymsJoins() {
        return jsonEndpoint.keySet().stream().filter(k -> k.startsWith("PseudonymsJoins"))
                .map(Arguments::of).toList().stream();
    }

    void test(Map<String, Object> object, String name) {
        log.info(name);
        log.info(object.toString());
        Endpoint information = new Endpoint(object, createParent(), funcName);
        information.generate();
        log.info(information.pseudonyms.toString());
        log.info("\n" + information.getDBMethods().stream().map(MethodSpec::toString).collect(Collectors.joining("\n")));
        log.info("\n" + information.getControllerMethods(beanName).stream().map(MethodSpec::toString).collect(Collectors.joining("\n")));
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("endpointPseudonymsJoins")
    void endpointPseudonymsJoins(String name) {
        test((Map<String, Object>) jsonEndpoint.get(name), name);
    }


}