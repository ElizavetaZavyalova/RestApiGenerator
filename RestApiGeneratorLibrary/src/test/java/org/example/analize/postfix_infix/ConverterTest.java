package org.example.analize.postfix_infix;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.example.analize.postfix_infix.Converter.RegExp.*;

@Slf4j
public class ConverterTest {

    @Test
    @SneakyThrows
    void testRegexp() {
        String s = "[2|[4|[2|5]]&2|9]&9|3&[2\\|\\]|8]";
        log.info(Arrays.toString(s.split(FIND_OPERATOR_OR_BRACKET, -1)));
        log.info(Arrays.stream(s.split(FIND_OPERATOR_OR_BRACKET)).collect(Collectors.joining("\",\"")));
        try {
            log.info(Converter.toPostfix(s).stream().collect(Collectors.joining("")));
        } catch (IllegalArgumentException ex) {
            log.info(ex.getMessage());
        }

    }
}
