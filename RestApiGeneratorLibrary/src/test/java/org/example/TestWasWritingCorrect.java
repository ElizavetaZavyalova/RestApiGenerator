package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.read_json.ParseJson;
import org.example.read_json.rest_controller_json.RestJson;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDSLContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.stream.Stream;

@Slf4j
public class TestWasWritingCorrect {
    @ParameterizedTest(name = "{0} test")
    @MethodSource("typeTest")
    void typeTest(String name, RestJson rest) {
        log.info(name);
        RunTest.test(rest);
    }
    @ParameterizedTest(name = "{0} test")
    @MethodSource("typesTest")
    void typesTest(String name, RestJson rest) {
        log.info(name);
        RunTest.test(rest);
    }
    @ParameterizedTest(name = "{0} test")
    @MethodSource("filtersTest")
    void filtersTest(String name, RestJson rest) {
        log.info(name);
        RunTest.test(rest);
    }
    @ParameterizedTest(name = "{0} test")
    @MethodSource("pseudonimsTest")
    void pseudonimsTest(String name, RestJson rest) {
        log.info(name);
        RunTest.test(rest);
    }

    record RunTest() {
        static void test(RestJson rest) {
            try {
                rest.generate();
                log.info("\n" + rest.getJavaRepository(getRepository(rest.getLocationName()), getRepositoryPath()).toString());
                log.info("\n" + rest.getJavaController(getController(rest.getLocationName()), getControllerPath(), getRepository(rest.getLocationName()), getRepositoryPath()).toString());
            } catch (IllegalArgumentException ex) {
                log.info(ex.getMessage());
                assert false;
            }
        }

        static String getRepository(String location) {
            return location + "Repository";
        }

        static String getController(String location) {
            return location + "Controller";
        }

        static String getRepositoryPath() {
            return "path.repository";
        }

        static String getControllerPath() {
            return "path.controller";
        }
    }
    static public Stream<Arguments> filtersTest() {
        String fileName = "P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\RestApiGeneratorLibrary\\src\\test\\resources\\generationTest\\filtersTest.json";
        return new ParseJson(fileName).getRestsJson().stream()
                .map(rest -> Arguments.of(rest.getLocationName(), rest));
    }
    static public Stream<Arguments> pseudonimsTest() {
        String fileName = "P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\RestApiGeneratorLibrary\\src\\test\\resources\\generationTest\\generatePseudonimsTest.json";
        return new ParseJson(fileName).getRestsJson().stream()
                .map(rest -> Arguments.of(rest.getLocationName(), rest));
    }

    static public Stream<Arguments> typeTest() {
        String fileName = "P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\RestApiGeneratorLibrary\\src\\test\\resources\\generationTest\\generateTypeTest.json";
        return new ParseJson(fileName).getRestsJson().stream()
                .map(rest -> Arguments.of(rest.getLocationName(), rest));
    }
    static public Stream<Arguments> typesTest() {
        String fileName = "P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\RestApiGeneratorLibrary\\src\\test\\resources\\generationTest\\generateTypesTest.json";
        return new ParseJson(fileName).getRestsJson().stream()
                .map(rest -> Arguments.of(rest.getLocationName(), rest));
    }
}
