package org.example.read_json.rest_controller_json.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.helpclass.CreateEndpoint;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;


import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.RequestType.*;
import static org.example.read_json.rest_controller_json.endpoint.TypeTest.Info.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class TypeTest {
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }
    public record Info() {
        static public Map<String, String> infoY = Map.of(ENTITY, "||");
        static public Map<String, String> info = Map.of(ENTITY, "");
        static public Map<String, String> info1F = Map.of(ENTITY, "|f1|f2");
        static public Map<String, String> infoE = Map.of(ENTITY, "entity1|entity2");

    }

    static public Stream<Arguments> constructorParams() {
        return Stream.of(
                Arguments.of(_GET, info),
                Arguments.of(_GET, info1F),
                Arguments.of(_GET, infoY),

                Arguments.of(_DELETE, info),
                Arguments.of(_DELETE, infoY),

                Arguments.of(_POST, info1F),


                Arguments.of(_GET, info1F),


                Arguments.of(_GET, info1F));
    }

    static public Stream<Arguments> constructorParamsThrow() {
        return Stream.of(
                Arguments.of(_DELETE,info1F),
                Arguments.of(_POST, info),
                Arguments.of(_PATCH, info),
                Arguments.of(_PUT, info),
                Arguments.of(_POST, infoY),
                Arguments.of(_PATCH, infoY),
                Arguments.of(_PUT, infoY)
        );
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParams")
    void type(String req, Map<String, String> info) {
        Type type = new Type(req, info, CreateEndpoint.creteEndpointReturnEntityName());
        log.info(type.toString());
    }
     @Test
    void typeEntity() {
        Type type = new Type(_GET, infoE, CreateEndpoint.creteEndpointReturnEntity());
        log.info(type.toString());
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsThrow")
    void typeThrow(String req, Map<String, String> info) {
        var ex = assertThrows(IllegalArgumentException.class, () -> new Type(req, info,CreateEndpoint.creteEndpointReturnEntityName()));
        log.info(ex.getMessage());
    }


}