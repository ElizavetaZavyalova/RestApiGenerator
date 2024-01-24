package org.example.analize.request.get;

import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.Endpoint;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.example.analize.helpclass.CreateEndpoint.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
@Slf4j
public class GetTest {
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParams")
    void ConstructorParams(String req, List<String> par) {
        Endpoint endpoint = makeEndpoint();
        log.info(req);
        StringGetRequest request=new  StringGetRequest(req,par,endpoint);
        log.info(request.interpret().toString());
    }
    static public Stream<Arguments> constructorParams() {
        List<String> par=List.of(paramName1);
        List<String> par2=List.of();
        List<String> par3=List.of(paramName1,paramName2,paramName3);
        return Stream.of(
               Arguments.of(table1,par3),
               Arguments.of(table1+"/"+table2,par),
               Arguments.of(table1+"/"+table2+"/"+table3,par2),
                Arguments.of(table1+"/{"+fieldName4+"}",par3),
                Arguments.of(table2+"/"+table1+"/{"+fieldName4+"}",par3),
               Arguments.of(table1+"/{"+fieldName1+"}/"+table3,par2),
               Arguments.of(table1+"/{"+fieldName1+"}"+"/"+table2+"/",par3),
               Arguments.of(table1+"/{"+fieldName2+"}/{"+fieldName1+"}"+"/"+table2+"/",par));
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsThrow")
    void ConstructorParamsThrow(String req,List<String> par) {
        Endpoint endpoint = makeEndpoint();
        log.info(req);
        var ex = assertThrows(IllegalArgumentException.class, () ->new  StringGetRequest(req,par,endpoint));
        log.info(ex.getMessage());
    }
    static public Stream<Arguments> constructorParamsThrow() {
        List<String> par=List.of(paramName1);
        List<String> par2=List.of();
        List<String> par3=List.of(paramName1,paramName2,paramName3);
        return Stream.of(
                Arguments.of("",par),
                Arguments.of("/",par),
                Arguments.of("/{"+fieldName4+"}",par3),
                Arguments.of("/{"+fieldName4+"}/"+table2,par2));
    }
}
