package org.example.read_json.rest_controller_json.pseudonyms;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
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
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }
    static Map<String, Object> json;

    @BeforeAll
    @SneakyThrows
    static void loadJson() {
        ReadJson readJson = new ReadJson();
        json = readJson.load("src\\test\\resources\\pseudonyms\\pseudonyms.json");
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
    @MethodSource("entityTest")
    void entityTables(String name) {
        Map<String, Map<String, List<String>>> object = (Map<String, Map<String, List<String>>>) json.get(name);
        log.info(object.toString());
        Pseudonyms pseudonyms = new EndpointPseudonyms(object, create());
        assertEquals(pseudonyms.getRealEntity("entity1"),List.of("e11","e12","e13"),()->"entity1");
        assertEquals(pseudonyms.getRealEntity("entity2"),List.of("e21","e22","e23"),()->"entity2");
        assertEquals(pseudonyms.getRealEntity("entity3"),List.of("entity3"),()->"entity3");
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
    static public Stream<Arguments> entityTest() {
        return json.keySet().stream().filter(k -> k.startsWith("entity"))
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
        log.info("t18:t9");
        assertEquals(pseudonyms.getRealJoinsName("t8:t9"), List.of("t8","t9"), () -> "t8:t9");
        assertThrows(IllegalArgumentException.class, () -> pseudonyms.getRealJoinsName("t11:t10"));
        log.info(pseudonyms.findPath("t1","t9",true).toString());
        log.info(pseudonyms.findPath("t9","t1",true).toString());

    }

}