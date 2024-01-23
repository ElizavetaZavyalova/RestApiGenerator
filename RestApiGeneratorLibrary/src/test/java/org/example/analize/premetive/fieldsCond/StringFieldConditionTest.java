package org.example.analize.premetive.fieldsCond;

import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.Endpoint;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class StringFieldConditionTest {
    static final String realFieldName = "fieldName";
    static final String fieldName = "name";
    static final String tableName = "MyTable";


    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsString")
    void ConstructorTestString(String name) {
        StringFieldCondition stringField = make(name);
        log.info(stringField.interpret());
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsInteger")
    void ConstructorTestInteger(String name) {
        StringFieldCondition stringField = make(name);
        log.info(stringField.interpret());
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsNoInteger")
    void ConstructorTestNoInteger(String name) {
        var ex = assertThrows(IllegalArgumentException.class, () -> make(name));
        log.info(ex.getMessage());
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsBoolean")
    void ConstructorTestBoolean(String name) {
        StringFieldCondition stringField = make(name);
        log.info(stringField.interpret());
    }

    static StringFieldCondition make(String name) throws IllegalArgumentException {
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(realFieldName).when(endpoint).getRealFieldName(fieldName);
        return new StringFieldCondition(name, tableName, endpoint);
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsNoBoolean")
    void ConstructorTestNoBoolean(String name) {
        var ex = assertThrows(IllegalArgumentException.class, () -> make(name));
        log.info(ex.getMessage());
    }

    static public Stream<Arguments> constructorParamsString() {
        return Stream.of(
                Arguments.of("s:eq_" + fieldName),
                Arguments.of("s:ne_" + fieldName),
                Arguments.of("s:like_" + fieldName),
                Arguments.of("s:not_like_" + fieldName),
                Arguments.of("s:ge_" + fieldName),
                Arguments.of("s:gt_" + fieldName),
                Arguments.of("s:le_" + fieldName),
                Arguments.of("s:lt_" + fieldName));
    }

    static public Stream<Arguments> constructorParamsBoolean() {
        return Stream.of(
                Arguments.of("b:eq_" + fieldName),
                Arguments.of("b:ne_" + fieldName));
    }

    static public Stream<Arguments> constructorParamsNoBoolean() {
        return Stream.of(
                Arguments.of("b:like_" + fieldName),
                Arguments.of("b:not_like_" + fieldName),
                Arguments.of("b:ge_" + fieldName),
                Arguments.of("b:gt_" + fieldName),
                Arguments.of("b:le_" + fieldName),
                Arguments.of("b:lt_" + fieldName));
    }

    static public Stream<Arguments> constructorParamsInteger() {
        return Stream.of(
                Arguments.of("i:eq_" + fieldName),
                Arguments.of("i:ne_" + fieldName),
                Arguments.of("i:ge_" + fieldName),
                Arguments.of("i:gt_" + fieldName),
                Arguments.of("i:le_" + fieldName),
                Arguments.of("i:lt_" + fieldName));
    }

    static public Stream<Arguments> constructorParamsNoInteger() {
        return Stream.of(
                Arguments.of("i:like_" + fieldName),
                Arguments.of("i:not_like_" + fieldName));
    }

}
