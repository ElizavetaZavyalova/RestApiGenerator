package org.example.analize.select;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.info.FilterInfo;
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

@Slf4j
public class SelectTest {
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }
    static String max="max_";
    static String min="min_";

    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParams1Select")
    void ConstructorParams1SelectTest(String req) {
        Endpoint endpoint = makeEndpoint();
        Select select=new Select(req,null,endpoint);
        log.info(req);
        log.info(select.interpret().toString());
        List<VarInfo> list=new ArrayList<>();
        List<FilterInfo> filters=new ArrayList<>();
        select.addParams(list,filters);
        log.info(list.stream().map(VarInfo::toString).collect(Collectors.joining("\n")));
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParams2Select")
    void ConstructorParams2SelectTest(String req1,String req2) {
        Endpoint endpoint = makeEndpoint();
        log.info(req1+"/"+req2);
        Select select1=new Select(req1,null,endpoint);
        log.info(select1.interpret().toString());

        Select select2=new Select(req2,select1,endpoint);
        log.info(select2.interpret().toString());
        List<VarInfo> list=new ArrayList<>();
        List<FilterInfo> filters=new ArrayList<>();
        select2.addParams(list,filters);
        log.info(list.stream().map(VarInfo::toString).collect(Collectors.joining("\n")));
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsMaxMin")
    void ConstructorParamsMaxMin(String req1,String req2) {
        Endpoint endpoint = makeEndpoint();
        log.info(req1+"/"+req2);
        Select select1=new Select(req1,null,endpoint);
        log.info(select1.interpret().toString());
        Select select2=new Select(req2,select1,endpoint);
        log.info(select2.interpret().toString());
        List<VarInfo> list=new ArrayList<>();
        List<FilterInfo> filters=new ArrayList<>();
        select2.addParams(list,filters);
        log.info(list.stream().map(VarInfo::toString).collect(Collectors.joining("\n")));
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsNoId")
    void ConstructorParamsNoIdTest(String req1,String req2) {
        Endpoint endpoint = makeEndpoint();
        log.info(req1+"/"+req2);
        Select select1=new Select(req1,null,endpoint);
        log.info(select1.interpret().toString());
        Select select2=new Select(req2,select1,endpoint);
        log.info(select2.interpret().toString());
        List<VarInfo> list=new ArrayList<>();
        List<FilterInfo> filters=new ArrayList<>();
        select2.addParams(list,filters);
        log.info(list.stream().map(VarInfo::toString).collect(Collectors.joining("\n")));
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsManyToMany")
    void ConstructorParamsManyToMany(String req1,String req2) {
        Endpoint endpoint = makeEndpointManyToManyEndpoint();
        log.info(req1+"/"+req2);
        Select select1=new Select(req1,null,endpoint);
        log.info(select1.interpret().toString());
        Select select2=new Select(req2,select1,endpoint);
        log.info(select2.interpret().toString());
        List<VarInfo> list=new ArrayList<>();
        List<FilterInfo> filters=new ArrayList<>();
        select2.addParams(list,filters);
        log.info(list.stream().map(VarInfo::toString).collect(Collectors.joining("\n")));
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
    static public Stream<Arguments> constructorParamsMaxMin() {
        return Stream.of(
                Arguments.of(max+table1,min+table2),
                Arguments.of(table1,table2));
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
    static public Stream<Arguments> constructorParamsManyToMany() {
        return Stream.of(
                Arguments.of(mmTable1,mmTable3),
                Arguments.of(mmTable4,mmTable2)
              );
    }

}
