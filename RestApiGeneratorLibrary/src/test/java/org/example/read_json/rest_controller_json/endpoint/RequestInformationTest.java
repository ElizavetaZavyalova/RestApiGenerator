package org.example.read_json.rest_controller_json.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;


import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.RequestType.*;
import static org.example.read_json.rest_controller_json.endpoint.TypeTest.Info.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RequestInformationTest {

    @Test
    void generateBd() {
    }

    @Test
    void makeDBMethod() {
    }

    @Test
    void makeDBMethods() {
    }

    static public Stream<Arguments> constructorParamsType() {
        return Stream.of(
                Arguments.of(Map.of(TYPES, Map.of(_GET, info))),
                Arguments.of(Map.of(TYPES, Map.of(_DELETE, infoY, _GET, info))),
                Arguments.of(Map.of("ty", Map.of())),
                Arguments.of(Map.of(TYPES, Map.of(_POST, (Object) info1F, _GET, (Object) "gtr|gre|io"))));
    }

    static public Stream<Arguments> constructorParamsTypeThrow() {
        return Stream.of(
                Arguments.of(Map.of(TYPES, Map.of(_POST, (Object) info1F, _GET, (Object) 345))),
                Arguments.of(Map.of(TYPES, Map.of(_POST, info))));
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsType")
    void TypesTest(Map<String, Object> map) {
        log.info("Params:" + map.toString());
        RequestInformation.Types types = new RequestInformation.Types(map);
        log.info("Types:" + types.toString());
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsTypeThrow")
    void TypesTestThrow(Map<String, Object> map) {
        log.info("Params:" + map.toString());
        var ex = assertThrows(IllegalArgumentException.class, () -> new RequestInformation.Types(map));
        log.info(ex.getMessage());
    }

    @Test
    void addCode() {
    }

    @Test
    void addReturns() {
    }
}