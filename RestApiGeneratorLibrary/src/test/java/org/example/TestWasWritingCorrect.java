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
    @Test
    void testJooq(){
        DSLContext context=new DefaultDSLContext(SQLDialect.POSTGRES);
        var c=context.select(DSL.field("info.name").as("user_name"), DSL.field("info.age"), DSL.field("info.id").as("user_id")).from("info").where(DSL.field("info.user_id").in(context.select(DSL.field("user.id")).from(DSL.table("users").as("user").where(DSL.field("user.id").eq(12)))));
        log.info(c.getSQL());
    }

    record RunTest() {
        static void test(RestJson rest) {
            try {
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
