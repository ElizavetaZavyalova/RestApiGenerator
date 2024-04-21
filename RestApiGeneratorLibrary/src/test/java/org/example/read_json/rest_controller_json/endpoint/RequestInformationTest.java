package org.example.read_json.rest_controller_json.endpoint;

import com.squareup.javapoet.MethodSpec;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.helpclass.CreateEndpoint;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.ReadJson;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RequestInformationTest {
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }
    static Map<String,Object> json;
    @BeforeAll
    @SneakyThrows
    static void loadJson(){
        ReadJson readJson=new ReadJson();
        json=readJson.load("src\\test\\resources\\requestInformation\\requestInformationTest.json");
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("typeTest")
    void generateTypeTest(String name) {
        Map<String,Object> object=(Map<String,Object>)json.get(name);
        log.info(object.toString());
        RequestInformation information=new RequestInformation(object,CreateEndpoint.creteEndpointReturnEntityName());
        Endpoint endpoint= CreateEndpoint.creteEndpoint(information);
        information.generateBd(endpoint);
        log.info("\n"+information.makeDBMethods(endpoint.getFuncName()).stream().map(MethodSpec::toString).collect(Collectors.joining("\n")));
        log.info("\n"+information.makeControllerMethods(endpoint.getFuncName(),"bean").stream().map(MethodSpec::toString).collect(Collectors.joining("\n")));
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("typeThrow")
    void generateTypeTestThrow(String name) {
        Map<String,Object> object=(Map<String,Object>)json.get(name);
        log.info(object.toString());
        var ex = assertThrows(IllegalArgumentException.class, () ->new RequestInformation(object,CreateEndpoint.creteEndpointReturnEntityName()));
        log.info(ex.getMessage());
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("typeThrowInRequest")
    void generateTypeTestThrowInRequest(String name) {
        Map<String, Object> object = (Map<String, Object>) json.get(name);
        log.info(object.toString());
        RequestInformation information = new RequestInformation(object, CreateEndpoint.creteEndpointReturnEntityName());
        Endpoint endpoint = CreateEndpoint.creteEndpoint(information);
        var ex = assertThrows(IllegalArgumentException.class, () -> information.generateBd(endpoint));
        log.info(ex.getMessage());
    }
    static public Stream<Arguments> typeTest() {
        return json.keySet().stream().filter(k->!k.startsWith("Throw"))
                .map(Arguments::of).toList().stream();
    }
    static public Stream<Arguments> typeThrow() {
        return json.keySet().stream().filter(k->k.startsWith("Throw")).filter(k->!k.startsWith("ThrowInRequestPostWhere"))
                .map(Arguments::of).toList().stream();
    }
    static public Stream<Arguments> typeThrowInRequest() {
        return json.keySet().stream().filter(k->k.startsWith("ThrowInRequest"))
                .map(Arguments::of).toList().stream();
    }

}