package org.example.analize.address;

import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.Endpoint;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.example.analize.helpclass.CreateEndpoint.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class StringAdressTest {


    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParams")
    void ConstructorParamsTest(String req) {
        Endpoint endpoint = makeEndpoint();
        log.info(req);
        StringAddress address=new StringAddress(req,endpoint);
        log.info(address.endUrl);
        log.info(address.interpret());
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsThrow")
    void ConstructorParamsThrow(String req) {
        Endpoint endpoint = makeEndpoint();
        log.info(req);
        var ex = assertThrows(IllegalArgumentException.class, () ->new StringAddress(req,endpoint));
        log.info(ex.getMessage());

    }
    static public Stream<Arguments> constructorParams() {
        return Stream.of(
                Arguments.of(table1),
                Arguments.of(table1+"/"+table2),
                Arguments.of(table1+"/"+table2+"/"+table3),
                Arguments.of(table1+"/{"+fieldName1+"}"),
                Arguments.of(table1+"/{"+fieldName1+"}"+"/"+table2+"/{"+fieldName4+"}"),
                Arguments.of(table1+"/{"+fieldName2+"}/{"+fieldName1+"}"+"/"+table2+"/{"+fieldName4+"}/{"+fieldName3+"}"));
    }
    static public Stream<Arguments> constructorParamsThrow() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("/"),
                Arguments.of("/{"+fieldName4+"}/"+table2));
    }


}
