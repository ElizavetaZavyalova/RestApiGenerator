package org.example.read_json.rest_controller_json.pseudonyms;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.read_json.ReadJson;
import org.example.read_json.rest_controller_json.JsonKeyWords;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.filter.EndpointFilters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class PseudonymsTest {
    static Map<String, Object> json;

    @BeforeAll
    @SneakyThrows
    static void loadJson() {
        ReadJson readJson = new ReadJson();
        json = readJson.load("P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\RestApiGeneratorLibrary\\src\\test\\resources\\pseudonyms\\pseudonyms.json");
    }

    Endpoint create() {
        return Mockito.mock(Endpoint.class);
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("tablesTest")
    void testTables(String name) {
        Map<String, Map<String, List<String>>> object = (Map<String, Map<String, List<String>>>) json.get(name);
        log.info(object.toString());
        Pseudonyms pseudonyms = new EndpointPseudonyms(object, create());
        assertEquals(pseudonyms.getRealTableName("t1"), "t15", () -> "t1->t15");
        assertEquals(pseudonyms.getRealTableName("table1"), "t15", () -> "table1->t15");
        assertEquals(pseudonyms.getRealTableName("t21"), "t2", () -> "t21->t2");
        assertEquals(pseudonyms.getRealTableName("noPres"), "noPres", () -> "noPres->noPres");
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("fieldTest")
    void testFields(String name) {
        Map<String, Map<String, List<String>>> object = (Map<String, Map<String, List<String>>>) json.get(name);
        log.info(object.toString());
        Pseudonyms pseudonyms = new EndpointPseudonyms(object, create());
        assertEquals(pseudonyms.getRealFieldName("f1"), "f15", () -> "f1->f15");
        assertEquals(pseudonyms.getRealFieldName("field1"), "f15", () -> "field1->f15");
        assertEquals(pseudonyms.getRealFieldName("f21"), "f2", () -> "f21->f2");
        assertEquals(pseudonyms.getRealFieldName("noPres"), "noPres", () -> "noPres->noPres");
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("emptyTest")
    void testEmpty(String name) {
        Map<String, Map<String, List<String>>> object = (Map<String, Map<String, List<String>>>) json.get(name);
        log.info(object.toString());
        Pseudonyms pseudonyms = new EndpointPseudonyms(object, create());
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("throwTest")
    void testThrow(String name) {
        Map<String, Map<String, List<String>>> object = (Map<String, Map<String, List<String>>>) json.get(name);
        log.info(object.toString());
        var ex = assertThrows(IllegalArgumentException.class, () -> new EndpointPseudonyms(object, create()));
        log.info(ex.getMessage());
    }

    static public Stream<Arguments> tablesTest() {
        return json.keySet().stream().filter(k -> k.startsWith("tables"))
                .map(Arguments::of).toList().stream();
    }

    static public Stream<Arguments> joinsTest() {
        return json.keySet().stream().filter(k -> k.startsWith("joins"))
                .map(Arguments::of).toList().stream();
    }

    static public Stream<Arguments> throwTest() {
        return json.keySet().stream().filter(k -> k.startsWith("Throw"))
                .map(Arguments::of).toList().stream();
    }

    static public Stream<Arguments> fieldTest() {
        return json.keySet().stream().filter(k -> k.startsWith("fields"))
                .map(Arguments::of).toList().stream();
    }

    static public Stream<Arguments> emptyTest() {
        return json.keySet().stream().filter(k -> k.startsWith("Empty") || k.startsWith("All"))
                .map(Arguments::of).toList().stream();
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("joinsTest")
    void testJoins(String name) {
        Map<String, Map<String, List<String>>> object = (Map<String, Map<String, List<String>>>) json.get(name);
        log.info(object.toString());
        Pseudonyms pseudonyms = new EndpointPseudonyms(object, create());
        log.info("t1:t2");
        assertEquals(pseudonyms.getRealJoinsName("t1:t2"), List.of("t1", "t2"), () -> "t1:t2");
        assertEquals(pseudonyms.getRealJoinsName("t2:t1"), List.of("t2", "t1"), () -> "t2:t1");
        log.info("t2:t3");
        assertEquals(pseudonyms.getRealJoinsName("t3:t2"), List.of(":", "t2"), () -> "t3:t2");
        assertEquals(pseudonyms.getRealJoinsName("t2:t3"), List.of("t2", ":"), () -> "t2:t3");
        log.info("t3:t4");
        assertEquals(pseudonyms.getRealJoinsName("t3:t4"), List.of("t3", ":"), () -> "t3:t4");
        assertEquals(pseudonyms.getRealJoinsName("t4:t3"), List.of(":", "t3"), () -> "t4:t3");
        log.info("t4:t5");
        assertEquals(pseudonyms.getRealJoinsName("t5:t4"), List.of(":", ":"), () -> "t5:t4");
        assertEquals(pseudonyms.getRealJoinsName("t4:t5"), List.of(":", ":"), () -> "t4:t5");
        log.info("t5:t6");
        assertEquals(pseudonyms.getRealJoinsName("t5:t6"), List.of("t100"), () -> "t5:t4");
        assertEquals(pseudonyms.getRealJoinsName("t6:t5"), List.of("t100"), () -> "t4:t5");
        log.info("t6:t7");
        assertEquals(pseudonyms.getRealJoinsName("t6:t7"), List.of(">"), () -> "t6:t7");
        assertEquals(pseudonyms.getRealJoinsName("t7:t6"), List.of("<"), () -> "t7:t6");
        log.info("t7:t8");
        assertEquals(pseudonyms.getRealJoinsName("t7:t8"), List.of("<"), () -> "t6:t7");
        assertEquals(pseudonyms.getRealJoinsName("t8:t7"), List.of(">"), () -> "t7:t6");
        log.info("t8:t9");
        assertEquals(pseudonyms.getRealJoinsName("t8:t9"), List.of("="), () -> "t8:t9");
        assertEquals(pseudonyms.getRealJoinsName("t9:t8"), List.of("="), () -> "t9:t8");
        log.info("t9:t10");
        assertEquals(pseudonyms.getRealJoinsName("t10:t9"), List.of("-"), () -> "t10:t9");
        assertEquals(pseudonyms.getRealJoinsName("t9:t10"), List.of("-"), () -> "t9:t10");
        log.info("t11:t10");
        assertEquals(pseudonyms.getRealJoinsName("t11:t10"), List.of("-t"), () -> "t11:t10");
        assertThrows(IllegalArgumentException.class, () -> pseudonyms.getRealJoinsName("t10:t11"));
        log.info("t12:t11");
        assertEquals(pseudonyms.getRealJoinsName("t12:t11"), List.of(">t"), () -> "t12:t11");
        assertThrows(IllegalArgumentException.class, () -> pseudonyms.getRealJoinsName("t11:t12"));
        log.info("t13:t12");
        assertEquals(pseudonyms.getRealJoinsName("t13:t12"), List.of("<t"), () -> "t13:t12");
        assertThrows(IllegalArgumentException.class, () -> pseudonyms.getRealJoinsName("t12:t13"));
        log.info("t14:t13");
        assertEquals(pseudonyms.getRealJoinsName("t14:t13"), List.of("=t"), () -> "t14:t13");
        assertThrows(IllegalArgumentException.class, () -> pseudonyms.getRealJoinsName("t13:t14"));
    }

}