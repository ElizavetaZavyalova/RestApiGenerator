package org.example.analize.where;

import lombok.extern.slf4j.Slf4j;

import org.example.analize.premetive.info.VarInfo;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.analize.helpclass.CreateEndpoint.*;
import static org.jooq.impl.DSL.field;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class StringWhereTest {
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }


    Endpoint makeEndpoint() {
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(realFieldName1).when(endpoint).getRealFieldName(fieldName1);
        Mockito.doReturn(fieldName2).when(endpoint).getRealFieldName(fieldName2);
        Mockito.doReturn(realFieldName3).when(endpoint).getRealFieldName(fieldName3);
        Mockito.doReturn(realFieldName4).when(endpoint).getRealFieldName(fieldName4);
        return endpoint;
            }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParams")
    void ConstructorParamsTest(String req) {
        Endpoint endpoint = makeEndpoint();
        StringWhere where = new StringWhere(req, table1, endpoint);
        log.info(req);
        log.info("\n" + where.interpret().toString());
        List<VarInfo> list=new ArrayList<>();
        where.addParams(list);
        log.info(list.stream().map(v->v.toString()).collect(Collectors.joining("\n")));
    }

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsFail")
    void ConstructorParamsFailTest(String req) {
        Endpoint endpoint = makeEndpoint();
        var ex = assertThrows(IllegalArgumentException.class, () -> new StringWhere(req, table1, endpoint));
        log.info(ex.getMessage());
    }

    static public Stream<Arguments> constructorParams() {
        return Stream.of(
                Arguments.of("/{s:eq_"+fieldName1+"}"),
                Arguments.of("/"),
                Arguments.of("/{s:eq_"+fieldName1+"}&{s:eq_"+fieldName2+"}"),
                Arguments.of("/{s:eq_"+fieldName1+"}/{s:eq_"+fieldName2+"}"),
                Arguments.of("/{s:eq_"+fieldName1+"}/{s:eq_"+fieldName2+"}/{s:eq_"+fieldName3+"}/{s:eq_"+fieldName4+"}"),
                Arguments.of("/{s:eq_"+fieldName1+"}&{s:eq_"+fieldName2+"}/{s:eq_"+fieldName3+"}"),
                Arguments.of("/({s:eq_"+fieldName1+"}|{s:eq_"+fieldName2+"})&({s:eq_"+fieldName3+"}|{s:eq_"+fieldName4+"})"),
                Arguments.of("/({s:eq_"+fieldName1+"}|{s:eq_"+fieldName2+"})&({s:eq_"+fieldName3+"}|{s:eq_"+fieldName4+"})/{s:like_"+fieldName1+"}"));
    }

    static public Stream<Arguments> constructorParamsFail() {
        return Stream.of(
                Arguments.of("/s:eq_"+fieldName1+"}"),
                Arguments.of("/{b:like_"+fieldName1+"}"),
                Arguments.of("/{s:eq_"+fieldName1+"}&s:eq_"+fieldName2+"}"),
                Arguments.of("/(/{b:like_"+fieldName1+"}"));
    }
}
