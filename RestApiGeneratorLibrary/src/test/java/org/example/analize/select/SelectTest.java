package org.example.analize.select;

import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.Endpoint;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.example.analize.helpclass.CreateEndpoint.*;

@Slf4j
public class SelectTest {

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParams1Select")
    void ConstructorParams1SelectTest(String req) {
        Endpoint endpoint = makeEndpoint();
        StringSelect select=new StringSelect(req,null,endpoint);
        log.info(req);
        log.info(select.interpret().toString());
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParams2Select")
    void ConstructorParams2SelectTest(String req1,String req2) {
        Endpoint endpoint = makeEndpoint();
        log.info(req1+"/"+req2);
        StringSelect select1=new StringSelect(req1,null,endpoint);
        log.info(select1.interpret().toString());
        StringSelect select2=new StringSelect(req2,select1,endpoint);
        log.info(select2.interpret().toString());
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsNoId")
    void ConstructorParamsNoIdTest(String req1,String req2) {
        Endpoint endpoint = makeEndpoint();
        log.info(req1+"/"+req2);
        StringSelect select1=new StringSelect(req1,null,endpoint);
        log.info(select1.interpret().toString());
        StringSelect select2=new StringSelect(req2,select1,endpoint);
        log.info(select2.interpret().toString());
    }
    static public Stream<Arguments> constructorParams1Select() {
        return Stream.of(
                Arguments.of(table1),
                Arguments.of(table1+"/{"+fieldName1+"}"),
                Arguments.of(table1+"/{"+fieldName2+"}/"));
    }
    static public Stream<Arguments> constructorParams2Select() {
        return Stream.of(
                Arguments.of(table1,table2),
                Arguments.of(table1+"/{"+fieldName1+"}",table2),
                Arguments.of(table1+"/{"+fieldName2+"}/{"+fieldName1+"}/",table2),
                Arguments.of(table1,table2+"/{"+fieldName2+"}"),
                Arguments.of(table1+"/{"+fieldName1+"}",table2+"/{"+fieldName2+"}"),
                Arguments.of(table1+"/{"+fieldName2+"}/{"+fieldName1+"}/",table2+"/{"+fieldName2+"}"));
    }
    static public Stream<Arguments> constructorParamsNoId() {
        return Stream.of(
                Arguments.of(table2,table3),
                Arguments.of( table2+"/{"+fieldName2+"}",table3+"/{"+fieldName1+"}"),
                Arguments.of( table3+"/{"+fieldName2+"}",table2+"/{"+fieldName1+"}"),
                Arguments.of(table1,table3),
                Arguments.of( table1+"/{"+fieldName2+"}",table3+"/{"+fieldName1+"}"),
                Arguments.of( table3+"/{"+fieldName2+"}",table1+"/{"+fieldName1+"}"));
    }

}
