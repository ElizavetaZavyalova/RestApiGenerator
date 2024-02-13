package org.example.analize.postfix_infix;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.example.read_json.rest_controller_json.pseudonyms.EndpointPseudonyms;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.analize.helpclass.CreateEndpoint.*;
import static org.example.analize.postfix_infix.Converter.RegExp.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class ConverterTest {

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("postfix")
    void testPostfix(String condition,String actual) {
        String result= String.join("", Converter.toPostfix(condition));
        assertEquals(result, actual, () -> condition);

    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("postfixThrow")
    void testPostfixThrow(String condition) {
       var ex=assertThrows(IllegalArgumentException.class, () -> Converter.toPostfix(condition));
       log.info(ex.getMessage());

    }
    static public Stream<Arguments> postfix() {
        return Stream.of(
                Arguments.of("A&B|C","AB&C|"),
                Arguments.of("(A|B)&(C&D)","AB|CD&&"),
                Arguments.of("A&(B&C|D&E)|F","ABC&DE&|&F|"),
                Arguments.of("(A|B)&C|(D|E)&F|G","AB|C&DE|F&|G|")
        );
    }
    static public Stream<Arguments> postfixThrow() {
        return Stream.of(
                Arguments.of("&A|B"),
                Arguments.of("A|B&"),
                Arguments.of("|A|B"),
                Arguments.of("A|B|"),
                Arguments.of("A|B)"),
                Arguments.of("(A|B"),
                Arguments.of("A|&B"),
                Arguments.of("A||B"),
                Arguments.of("A&|B"),
                Arguments.of("A&&B"));
    }


}
