package org.example.analize.where;

import com.squareup.javapoet.ParameterSpec;
import lombok.extern.slf4j.Slf4j;

import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.Endpoint;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.impl.DSL;
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
        log.info("\n" + where.requestInterpret());
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
