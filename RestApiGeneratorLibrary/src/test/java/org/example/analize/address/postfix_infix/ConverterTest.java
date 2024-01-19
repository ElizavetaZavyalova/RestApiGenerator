package org.example.analize.address.postfix_infix;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.example.analize.postfix_infix.Converter;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
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
