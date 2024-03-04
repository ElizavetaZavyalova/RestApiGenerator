package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class TestOneRequestControllerTest extends RestTest {
    @BeforeEach
    void init() {
      super.init();
    }

    @AfterEach
    void clear() {
        super.clear();
    }
    static public Stream<Arguments> deleteParams() {
        return Stream.of(

        );
    }
    static public Stream<Arguments> getParams() {
        return Stream.of(
                Arguments.of("/table1/1","")
        );
    }
    static public Stream<Arguments> postParams() {
        return Stream.of(

        );
    }
    static public Stream<Arguments> putParams() {
        return Stream.of(

        );
    }
    static public Stream<Arguments> patchParams() {
        return Stream.of(

        );
    }



    @ParameterizedTest(name = "{arguments}")
    @MethodSource("deleteParams")
    void delete(String request,String params) {
        super.deleteTest(request,params);
    }

    @ParameterizedTest(name = "{arguments}")
    @MethodSource("getParams")
    void get(String request,String params) {
        super.getTest(request,params);
    }
    List<Map<String, Object>> selectAllFrom(String table){
        var result = dsl.select().from(table);
        List<Map<String, Object>> resultList = new ArrayList<>();
        result.fetch().forEach(r ->  {
            Map<String, Object> resultMap = new HashMap<>();
            Arrays.stream(r.fields()).forEach(field -> resultMap.put(field.getName(), r.getValue(field)));
            resultList.add(resultMap);
        });
        return resultList;
    }


    @ParameterizedTest(name = "{arguments}")
    @MethodSource("patchParams")
    void patch(String request, String params, String body) {
        super.patchTest(request,params,body);
    }

    @ParameterizedTest(name = "{arguments}")
    @MethodSource("postParams")
    void post(String request,String params,String body) {
        super.postTest(request,params,body);
    }

    @ParameterizedTest(name = "{arguments}")
    @MethodSource("putParams")
    void put(String request,String params,String body) {
        super.putTest(request,params,body);
    }
}