package org.example.analize.request.delete;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.info.VarInfo;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.analize.helpclass.CreateEndpoint.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
@Slf4j
public class DeleteTest {
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParams")
    void ConstructorParams(String req) {
        Endpoint endpoint = makeEndpoint();
        log.info(req);
        StringDeleteRequest request=new StringDeleteRequest(req,endpoint);
        log.info(request.delete.interpret().toString());
        List<VarInfo> list=new ArrayList<>();
        request.addParams(list);
        log.info(list.stream().map(v->v.toString()).collect(Collectors.joining("\n")));
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
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsThrow")
    void ConstructorParamsThrow(String req) {
        Endpoint endpoint = makeEndpoint();
        log.info(req);
        var ex = assertThrows(IllegalArgumentException.class, () ->new  StringDeleteRequest(req,endpoint));
        log.info(ex.getMessage());
    }
    static public Stream<Arguments> constructorParamsThrow() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("/"),
                Arguments.of("/{"+fieldName4+"}"),
                Arguments.of("/{"+fieldName4+"}/"+table2));
    }
}
