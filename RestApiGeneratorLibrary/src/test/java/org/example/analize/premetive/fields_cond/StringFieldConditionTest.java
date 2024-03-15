package org.example.analize.premetive.fields_cond;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class StringFieldConditionTest {
    @BeforeAll
    static void setDebug() {
        DefaultsVariablesName.DEBUG = true;
    }

    static final String realFieldName = "fieldName";
    static final String fieldName = "name";
    static final String tableName = "MyTable";


    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsString")
    void ConstructorTestString(String name) {
        FieldCondition stringField = make(name);
        log.info(stringField.interpret().toString());
        List<VarInfo> list = new ArrayList<>();
        List<FilterInfo> filters = new ArrayList<>();
        stringField.addParams(list, filters);
        log.info(list.stream().map(v -> v.toString()).collect(Collectors.joining("\n")));
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsInteger")
    void ConstructorTestInteger(String name) {
        FieldCondition stringField = make(name);
        log.info(stringField.interpret().toString());
        List<VarInfo> list = new ArrayList<>();
        List<FilterInfo> filters = new ArrayList<>();
        stringField.addParams(list, filters);
        log.info(list.stream().map(v -> v.toString()).collect(Collectors.joining("\n")));
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsNoInteger")
    void ConstructorTestNoInteger(String name) {
        var ex = assertThrows(IllegalArgumentException.class, () -> make(name));
        log.info(ex.getMessage());
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsLong")
    void ConstructorTestLong(String name) {
        FieldCondition stringField = make(name);
        log.info(stringField.interpret().toString());
        List<VarInfo> list = new ArrayList<>();
        List<FilterInfo> filters = new ArrayList<>();
        stringField.addParams(list, filters);
        log.info(list.stream().map(v -> v.toString()).collect(Collectors.joining("\n")));
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsNoLong")
    void ConstructorTestNoLong(String name) {
        var ex = assertThrows(IllegalArgumentException.class, () -> make(name));
        log.info(ex.getMessage());
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsFloat")
    void ConstructorTestFloat(String name) {
        FieldCondition stringField = make(name);
        log.info(stringField.interpret().toString());
        List<VarInfo> list = new ArrayList<>();
        List<FilterInfo> filters = new ArrayList<>();
        stringField.addParams(list, filters);
        log.info(list.stream().map(v -> v.toString()).collect(Collectors.joining("\n")));
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsNoFloat")
    void ConstructorTestNoFloat(String name) {
        var ex = assertThrows(IllegalArgumentException.class, () -> make(name));
        log.info(ex.getMessage());
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsDouble")
    void ConstructorTestDouble(String name) {
        FieldCondition stringField = make(name);
        log.info(stringField.interpret().toString());
        List<VarInfo> list = new ArrayList<>();
        List<FilterInfo> filters = new ArrayList<>();
        stringField.addParams(list, filters);
        log.info(list.stream().map(v -> v.toString()).collect(Collectors.joining("\n")));
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsNoDouble")
    void ConstructorTestNoDouble(String name) {
        var ex = assertThrows(IllegalArgumentException.class, () -> make(name));
        log.info(ex.getMessage());
    }



    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsBoolean")
    void ConstructorTestBoolean(String name) {
        FieldCondition stringField = make(name);
        log.info(stringField.interpret().toString());
        List<VarInfo> list = new ArrayList<>();
        List<FilterInfo> filters = new ArrayList<>();
        stringField.addParams(list, filters);
        log.info(list.stream().map(v -> v.toString()).collect(Collectors.joining("\n")));
    }

    static FieldCondition make(String name) throws IllegalArgumentException {
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(realFieldName).when(endpoint).getRealFieldName(fieldName);
        return new FieldCondition(name, tableName, endpoint);
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsNoBoolean")
    void ConstructorTestNoBoolean(String name) {
        var ex = assertThrows(IllegalArgumentException.class, () -> make(name));
        log.info(ex.getMessage());
    }

    static public Stream<Arguments> constructorParamsString() {
        return Stream.of(
                Arguments.of("eq_" + fieldName + "-s"),
                Arguments.of("ne_" + fieldName + "-s"),
                Arguments.of("like_" + fieldName + "-s"),
                Arguments.of("not_like_" + fieldName + "-s"),
                Arguments.of("ge_" + fieldName + "-s"),
                Arguments.of("gt_" + fieldName + "-s"),
                Arguments.of("le_" + fieldName + "-s"),
                Arguments.of("lt_" + fieldName + "-s"),
                Arguments.of("reg_" + fieldName + "-s"),
                Arguments.of("not_reg_" + fieldName + "-s"));
    }

    static public Stream<Arguments> constructorParamsBoolean() {
        return Stream.of(
                Arguments.of("eq_" + fieldName + "-b"),
                Arguments.of("ne_" + fieldName + "-b"));
    }

    static public Stream<Arguments> constructorParamsNoBoolean() {
        return Stream.of(
                Arguments.of("like_" + fieldName + "-b"),
                Arguments.of("not_like_" + fieldName + "-b"),
                Arguments.of("reg_" + fieldName + "-b"),
                Arguments.of("not_reg_" + fieldName + "-b"),
                Arguments.of("ge_" + fieldName + "-b"),
                Arguments.of("gt_" + fieldName + "-b"),
                Arguments.of("le_" + fieldName + "-b"),
                Arguments.of("lt_" + fieldName + "-b"));
    }

    static public Stream<Arguments> constructorParamsInteger() {
        return Stream.of(
                Arguments.of("eq_" + fieldName + "-i"),
                Arguments.of("ne_" + fieldName + "-i"),
                Arguments.of("ge_" + fieldName + "-i"),
                Arguments.of("gt_" + fieldName + "-i"),
                Arguments.of("le_" + fieldName + "-i"),
                Arguments.of("lt_" + fieldName + "-i"));
    }

    static public Stream<Arguments> constructorParamsNoInteger() {
        return Stream.of(
                Arguments.of("like_" + fieldName + "-i"),
                Arguments.of("not_like_" + fieldName + "-i"),
                Arguments.of("reg_" + fieldName + "-i"),
                Arguments.of("not_reg_" + fieldName + "-i"));
    }
    static public Stream<Arguments> constructorParamsLong() {
        return Stream.of(
                Arguments.of("eq_" + fieldName + "-l"),
                Arguments.of("ne_" + fieldName + "-l"),
                Arguments.of("ge_" + fieldName + "-l"),
                Arguments.of("gt_" + fieldName + "-l"),
                Arguments.of("le_" + fieldName + "-l"),
                Arguments.of("lt_" + fieldName + "-l"));
    }

    static public Stream<Arguments> constructorParamsNoLong() {
        return Stream.of(
                Arguments.of("like_" + fieldName + "-l"),
                Arguments.of("not_like_" + fieldName + "-l"),
                Arguments.of("reg_" + fieldName + "-l"),
                Arguments.of("not_reg_" + fieldName + "-l"));
    }
    static public Stream<Arguments> constructorParamsFloat() {
        return Stream.of(
                Arguments.of("eq_" + fieldName + "-f"),
                Arguments.of("ne_" + fieldName + "-f"),
                Arguments.of("ge_" + fieldName + "-f"),
                Arguments.of("gt_" + fieldName + "-f"),
                Arguments.of("le_" + fieldName + "-f"),
                Arguments.of("lt_" + fieldName + "-f"));
    }

    static public Stream<Arguments> constructorParamsNoFloat() {
        return Stream.of(
                Arguments.of("like_" + fieldName + "-f"),
                Arguments.of("not_like_" + fieldName + "-f"),
                Arguments.of("reg_" + fieldName + "-f"),
                Arguments.of("not_reg_" + fieldName + "-f"));
    }
    static public Stream<Arguments> constructorParamsDouble() {
        return Stream.of(
                Arguments.of("eq_" + fieldName + "-d"),
                Arguments.of("ne_" + fieldName + "-d"),
                Arguments.of("ge_" + fieldName + "-d"),
                Arguments.of("gt_" + fieldName + "-d"),
                Arguments.of("le_" + fieldName + "-d"),
                Arguments.of("lt_" + fieldName + "-d"));
    }

    static public Stream<Arguments> constructorParamsNoDouble() {
        return Stream.of(
                Arguments.of("like_" + fieldName + "-d"),
                Arguments.of("not_like_" + fieldName + "-d"),
                Arguments.of("reg_" + fieldName + "-d"),
                Arguments.of("not_reg_" + fieldName + "-d"));
    }

}
